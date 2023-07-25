package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.EventFactoryModification;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotModifyAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAggregateGraphStateModificator implements AggregateGraphStateModificator {

  protected final StructureSchemaFinder structureSchemaFinder;
  protected final DynamicOgmProvider dynamicOgmProvider;
  protected final GenericObjectGraphMapper objectGraphMapper;

  public AbstractAggregateGraphStateModificator(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider,
      GenericObjectGraphMapper objectGraphMapper
  ) {
    this.structureSchemaFinder = structureSchemaFinder;
    this.dynamicOgmProvider = dynamicOgmProvider;
    this.objectGraphMapper = objectGraphMapper;
  }

  @NotNull
  protected Map<String, Object> getMappedObject(
      Object inputValue,
      TraversableNode modifiedNode,
      String fieldName
  ) {
    return new HashMap<>(Map.of(
        "id", modifiedNode.getId().getId(),
        fieldName, inputValue
    ));
  }

  @NotNull
  protected ObjectGraphMapping getOgm(
      TraversableNode modifiedNode,
      FieldDefinition inputValueSchema,
      String fieldName
  ) {
    var objectOgm = new ObjectGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(modifiedNode.getType())
    );

    objectOgm
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());


    var dataField = objectOgm.addField(fieldName);

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
        var primitiveOgmBuilder = new GenericOGMBuilder().copyGraphMappingAsBuilder(primitiveOgm);
        dataField.setOgmBuilder(primitiveOgmBuilder);
      }
    }
    return objectOgm.build();
  }

  protected TraversableNode getTraversingStartNode(
      String aggregateType,
      DynamicCommand command,
      EventFactoryModification modificationDefinition,
      ComplexStructureType operationStructureType,
      InMemoryGraphRepository aggregateRepo
  ) {
    var startIdParameterName = modificationDefinition.getStartIdParameterName();
    if (startIdParameterName == null) {
      var aggregateId = command.getTargetIdentifier();
      if (aggregateRepo.nodeExists(aggregateId, aggregateType)) {
        return aggregateRepo.loadNode(aggregateId, aggregateType);
      } else {
        return new TraversableNode(aggregateId, aggregateType);
      }
    } else {
      var startIdValue = command.getData().get(startIdParameterName);
      if (!(startIdValue instanceof String stringStartId)) {
        throw CannotModifyAggregateState.becauseThereIsNoIdInCommandAtStartIdParameterName(
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      if (!stringStartId.contains("/")) {
        throw CannotModifyAggregateState.becauseStartIdIsNotOfCorrectFormat(
            stringStartId,
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      var splitId = stringStartId.split("/");
      if (splitId.length != 2) {
        throw CannotModifyAggregateState.becauseStartIdIsNotOfCorrectFormat(
            stringStartId,
            startIdParameterName,
            modificationDefinition,
            operationStructureType
        );
      }
      try {
        return aggregateRepo.loadNode(
            new UniqueIdentifier(splitId[1]),
            splitId[0]
        );
      } catch (NodeNotFound nodeNotFound) {
        throw CannotModifyAggregateState.becauseThereIsNoNodeWithIdSpecifiedAtStartIdParameterName(
            modificationDefinition,
            operationStructureType,
            new UniqueIdentifier(stringStartId),
            startIdParameterName
        );
      }
    }
  }

  protected TraversableNode traverseToModifiedNode(
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
      throw CannotModifyAggregateState.becauseThereAreEdgesOnPathEvenThoughtThereShouldBeMaxOne(
          modificationDefinition,
          operationDefinition,
          String.join(".", newAlreadyTraversedPath)
      );
    }
    if (edges.isEmpty()) {
      throw CannotModifyAggregateState.becauseThereAreIsNodeEdgeOnPathEvenThoughtThereShouldBeOne(
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
}
