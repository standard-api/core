package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotAddToAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.operationdefinition.model.FieldDefinitionWithSource;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class AddAggregateGraphStateModificator implements AggregateGraphStateModificator {

  private final StructureSchemaFinder structureSchemaFinder;
  private final DynamicOgmProvider dynamicOgmProvider;
  private final GenericObjectGraphMapper objectGraphMapper;

  public AddAggregateGraphStateModificator(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider,
      GenericObjectGraphMapper objectGraphMapper
  ) {
    this.structureSchemaFinder = structureSchemaFinder;
    this.dynamicOgmProvider = dynamicOgmProvider;
    this.objectGraphMapper = objectGraphMapper;
  }

  @Override
  public GraphMappingResult modify(
      String aggregateType,
      Graph currentAggregateState,
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var inputValueParameterName = modificationDefinition.getInputValueParameterName();
    if (inputValueParameterName == null) {
      throw CannotAddToAggregateState.becauseModificationDefinitionDoesNotHaveInputValueParameterNameSpecified(
          modificationDefinition,
          operationStructureType
      );
    }
    var inputValue = command.getData().get(inputValueParameterName);
    if (inputValue == null) {
      return new GraphMappingResult(
          new Graph(),
          List.of()
      );
    }
    var resourceStructureType = (ResourceStructureType) this.structureSchemaFinder.getStructureType(
        aggregateType
    );
    var modificationPath = modificationDefinition.getModificationPath();
    var splitPath = modificationPath.split("\\.");
    var closestListNodePaths = this.findClosestListNodePaths(
        resourceStructureType,
        new String[] {aggregateType},
        Arrays.copyOfRange(splitPath, 1, splitPath.length),
        operationStructureType,
        modificationDefinition
    );
    if (closestListNodePaths instanceof ErrorFindClosestListNodePathsOutput error) {
      throw error.getError();
    }
    var successOutput = (HappyFindClosestListNodePathsOutput) closestListNodePaths;
    var closestListPath = successOutput.getPaths().toList().get(0);
    var aggregateRepo = currentAggregateState.traversable();

    TraversableNode traversingStartNode;
    if (closestListPath.length <= 1) {
      var aggregateId = command.getTargetIdentifier();
      if (aggregateRepo.nodeExists(aggregateId, aggregateType)) {
        traversingStartNode = aggregateRepo.loadNode(aggregateId, aggregateType);
      } else {
        traversingStartNode = new TraversableNode(aggregateId, aggregateType);
      }
    } else {
      traversingStartNode = this.getTraversingStart(
          command,
          modificationDefinition,
          operationStructureType,
          closestListPath,
          aggregateRepo
      );
    }

    var modifiedNode = this.traverseToModifiedNode(
        traversingStartNode,
        Arrays.copyOfRange(
            splitPath,
            closestListPath.length,
            splitPath.length
        ),
        Arrays.stream(closestListPath).toList(),
        operationStructureType,
        modificationDefinition
    );
    var inputValueSchema = operationStructureType.getField(inputValueParameterName);

    var objectOgm = new ObjectGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(modifiedNode.getType())
    );
    objectOgm
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());

    var dataField = objectOgm.addField("data");

    var fieldName = splitPath[splitPath.length - 1];
    var edge = new GraphDescriptionBuilder().addOutgoingEdge(fieldName);
    if (inputValueSchema.isUnionType()) {
      if (inputValueSchema.isList()) {
        dataField
            .addListAsObjectFieldMapping()
            .addInterfaceChildDefinition()
            .setGraphDescription(edge);
      } else {
        dataField
            .setRelation(edge)
            .addInterfaceAsObjectFieldMapping();
      }
    } else {
      var type = inputValueSchema.getTypes().get(0).getType();
      var inputValueType = this.structureSchemaFinder.getStructureType(type);
      if (inputValueType instanceof ComplexStructureType) {
        var inputOgm = this.dynamicOgmProvider.provideGraphMapping(type);
        var inputOgmBuilder = new GenericOGMBuilder()
            .copyGraphMappingAsBuilder(inputOgm);
        if (inputValueSchema.isList()) {
          dataField
              .addListAsObjectFieldMapping()
              .setGraphDescription(edge)
              .setChildDefinition(inputOgmBuilder);
        } else {
          dataField
              .setRelation(edge)
              .setOgmBuilder(inputOgmBuilder);
        }
      } else {
        var primitiveOgm = this.dynamicOgmProvider.provideOgmForPrimitive(
            type,
            new FieldDefinition(
                fieldName,
                inputValueSchema.getMin(),
                inputValueSchema.getMax(),
                inputValueSchema.getDescription(),
                inputValueSchema.getTypes(),
                inputValueSchema.getParentDefinitionType()
            )
        );
        var primitiveOgmBuilder = new GenericOGMBuilder()
            .copyGraphMappingAsBuilder(primitiveOgm);
        dataField.setOgmBuilder(primitiveOgmBuilder);
      }
    }

    var fakedObject = new HashMap<>(Map.of(
        "id", modifiedNode.getId().getId(),
        "data", inputValue
    ));
    return this.objectGraphMapper.mapToGraph(
        objectOgm.build(),
        fakedObject,
        missingFieldResolvingStrategy
    );
  }

  @Override
  public boolean supports(EventFactoryModification modificationDefinition) {
    return modificationDefinition.getKind().equals(EventFactoryModification.ADD);
  }

  private FindClosestListNodePathsOutput findClosestListNodePaths(
      ComplexStructureType currentStructure,
      String[] lastPathToList,
      String[] pathToTraverse,
      ComplexStructureType operationDefinition,
      EventFactoryModification modificationDefinition
  ) {
    var currentLastPathToList = lastPathToList;
    if (pathToTraverse.length < 2) {
      var result = new ArrayList<String[]>();
      result.add(lastPathToList);
      return new HappyFindClosestListNodePathsOutput(result.stream());
    }
    var fieldName = pathToTraverse[0];
    var restOfPath = Arrays.copyOfRange(pathToTraverse, 1, pathToTraverse.length);
    var fieldDefinition = currentStructure.getAllFields().get(fieldName);
    if (fieldDefinition == null) {
      return new ErrorFindClosestListNodePathsOutput(
          CannotAddToAggregateState.becauseModificationPathContainsFieldNameNotPresentInSchema(
              modificationDefinition,
              operationDefinition
          )
      );
    }
    if (fieldDefinition.isList()) {
      var wholePath = modificationDefinition.getModificationPath().split("\\.");
      currentLastPathToList = Arrays.copyOfRange(
          wholePath,
          0,
          wholePath.length - restOfPath.length
      );
    }
    var finalCurrentLastPathToList = currentLastPathToList;
    var typeResults = fieldDefinition.getTypes().stream().map(fieldType -> {
      if (fieldType.isPrimitiveType()) {
        return new ErrorFindClosestListNodePathsOutput(
            CannotAddToAggregateState.becauseModificationPathContainsFieldNameWhichIsPrimitiveButItShouldNot(
                modificationDefinition,
                operationDefinition
            )
        );
      }
      var type = fieldType.getType();
      var structureType = (ComplexStructureType) this.structureSchemaFinder.getStructureType(
          type
      );
      return this.findClosestListNodePaths(
          structureType,
          finalCurrentLastPathToList,
          restOfPath,
          operationDefinition,
          modificationDefinition
      );
    }).toList();
    if (typeResults.stream().allMatch(ErrorFindClosestListNodePathsOutput.class::isInstance)) {
      return new ErrorFindClosestListNodePathsOutput(
          new CannotAddToAggregateState(
              typeResults.stream()
                  .map(ErrorFindClosestListNodePathsOutput.class::cast)
                  .map(ErrorFindClosestListNodePathsOutput::getError)
                  .toList()
          )
      );
    }
    return new HappyFindClosestListNodePathsOutput(
        typeResults.stream()
            .filter(HappyFindClosestListNodePathsOutput.class::isInstance)
            .map(HappyFindClosestListNodePathsOutput.class::cast)
            .flatMap(HappyFindClosestListNodePathsOutput::getPaths)
    );
  }

  private TraversableNode traverseToModifiedNode(
      TraversableNode currentNode,
      String[] pathToTraverse,
      List<String> alreadyTraversedPath,
      ComplexStructureType operationDefinition,
      EventFactoryModification modificationDefinition
  ) {
    if (pathToTraverse.length < 2) {
      return currentNode;
    }
    var fieldName = pathToTraverse[0];
    var edges = currentNode.getEdges(fieldName);
    var newAlreadyTraversedPath = new ArrayList<>(alreadyTraversedPath);
    newAlreadyTraversedPath.add(fieldName);
    if (edges.size() > 1) {
      throw CannotAddToAggregateState.becauseThereAreEdgesOnPathEvenThoughtThereShouldBeMaxOne(
          modificationDefinition,
          operationDefinition,
          String.join(".", newAlreadyTraversedPath)
      );
    }
    if (edges.isEmpty()) {
      throw CannotAddToAggregateState.becauseThereAreIsNodeEdgeOnPathEvenThoughtThereShouldBeOne(
          modificationDefinition,
          operationDefinition,
          String.join(".", newAlreadyTraversedPath)
      );
    }
    return this.traverseToModifiedNode(
        edges.get(0).getNodeTo(),
        Arrays.copyOfRange(pathToTraverse, 1, pathToTraverse.length),
        newAlreadyTraversedPath,
        operationDefinition,
        modificationDefinition
    );
  }

  private TraversableNode getTraversingStart(
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      String[] closestListPath,
      InMemoryGraphRepository aggregateRepo
  ) {
    var sourcePathOfId = String.format(
        "%s.id",
        String.join(".", closestListPath)
    );
    var idParametersWithSameSourcePath = operationStructureType.getAllFields()
        .values()
        .stream()
        .map(FieldDefinitionWithSource.class::cast)
        .filter(fieldDefinition -> fieldDefinition.getSource().equals(sourcePathOfId))
        .toList();

    if (idParametersWithSameSourcePath.isEmpty()) {
      throw CannotAddToAggregateState.becauseThereAreIndistinguishableNodes(
          modificationDefinition,
          operationStructureType,
          sourcePathOfId
      );
    }
    if (idParametersWithSameSourcePath.size() > 1) {
      throw CannotAddToAggregateState.becauseThereAreMoreWaysToDistinguisNodes(
          modificationDefinition,
          operationStructureType,
          sourcePathOfId
      );
    }
    var idParameter = idParametersWithSameSourcePath.get(0);
    var idValue = command.getData().get(idParameter.getName());
    if (!(idValue instanceof String stringIdValue)) {
      throw new CannotAddToAggregateState(List.of());
    }
    var id = new UniqueIdentifier(stringIdValue);
    if (aggregateRepo.nodeExists(id)) {
      return aggregateRepo.loadNode(id);
    } else {
      throw CannotAddToAggregateState.becauseThereIsNoNodeWithIdSpecifiedAtSourcePath(
          modificationDefinition,
          operationStructureType,
          String.join(".", closestListPath),
          id
      );
    }
  }

  private record ClosestJoinInfo(String nodeId, String nodeType, String fieldName) {

  }

  private interface FindClosestListNodePathsOutput {

  }

  private static class HappyFindClosestListNodePathsOutput
      implements FindClosestListNodePathsOutput {

    private final Stream<String[]> paths;

    public HappyFindClosestListNodePathsOutput(Stream<String[]> paths) {
      this.paths = paths;
    }

    public Stream<String[]> getPaths() {
      return paths;
    }
  }

  private static class ErrorFindClosestListNodePathsOutput
      implements FindClosestListNodePathsOutput {

    private final CannotAddToAggregateState error;

    public ErrorFindClosestListNodePathsOutput(CannotAddToAggregateState error) {
      this.error = error;
    }

    public CannotAddToAggregateState getError() {
      return error;
    }
  }
}
