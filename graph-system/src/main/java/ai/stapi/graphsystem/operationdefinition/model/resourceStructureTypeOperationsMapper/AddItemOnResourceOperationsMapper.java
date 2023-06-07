package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO.ReferencedFrom;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AddItemOnResourceOperationsMapper extends AbstractResourceOperationsMapper {

  public AddItemOnResourceOperationsMapper(StructureSchemaFinder structureSchemaFinder) {
    super(structureSchemaFinder);
  }

  @Override
  public ResourceOperationsMapperResult map(ResourceStructureType resourceStructureType) {
    var resourceType = resourceStructureType.getDefinitionType();
    return new ResourceOperationsMapperResult(
        this.createOperationsRecursively(
            new MapperContext(resourceType, List.of(resourceType)),
            resourceStructureType
        )
    );
  }

  @Override
  public ResourceOperationsMapperResult mapNewFields(
      ResourceStructureType rootResourceType,
      ComplexStructureType modifiedStructureType,
      List<String> newFieldNames
  ) {
    return newFieldNames.stream().map(fieldName -> {
          var newField = modifiedStructureType.getField(fieldName);
          if (newField.getName().equals("id")) {
            return new ResourceOperationsMapperResult();
          }
          var type = rootResourceType.getDefinitionType();
          var mapperContext = new MapperContext(type, List.of(type));

          return new ResourceOperationsMapperResult(
              this.processNewFieldOnComplexRecursively(
                  mapperContext,
                  rootResourceType,
                  modifiedStructureType,
                  newField
              )
          );
        }).reduce(ResourceOperationsMapperResult::merge)
        .orElse(new ResourceOperationsMapperResult());
  }

  @NotNull
  private List<OperationDefinitionDTO> createOperationsRecursively(
      MapperContext mapperContext,
      ComplexStructureType structure
  ) {
    var fields = structure.getOwnFields();
    var operations = fields.values().stream()
        .filter(FieldDefinition::isList)
        .map(fieldDefinition -> this.createAddItemCommand(mapperContext, fieldDefinition))
        .collect(Collectors.toList());

    fields.values().forEach(fieldDefinition -> operations.addAll(
        this.createDeeperOperations(
            mapperContext,
            fieldDefinition
        )
    ));

    return operations;
  }

  private List<OperationDefinitionDTO> processNewFieldOnComplexRecursively(
      MapperContext mapperContext,
      ComplexStructureType currentStructureType,
      ComplexStructureType modifiedStructureType,
      FieldDefinition newFieldDefinition
  ) {
    var currentType = currentStructureType.getDefinitionType();
    var modifiedType = modifiedStructureType.getDefinitionType();
    if (currentType.equals(modifiedType)) {
      var resultOperations = new ArrayList<OperationDefinitionDTO>();
      if (newFieldDefinition.isList()) {
        var newOperation = this.createAddItemCommand(
            mapperContext,
            newFieldDefinition
        );
        resultOperations.add(newOperation);
      }
      resultOperations.addAll(
          this.createDeeperOperations(
              mapperContext,
              newFieldDefinition
          )
      );
      return resultOperations;
    }
    return currentStructureType.getOwnFields()
        .values()
        .stream()
        .flatMap(
            fieldDefinition -> fieldDefinition.getTypes().stream()
                .filter(type -> !type.isReference())
                .filter(type -> !type.isContentReferenced())
                .map(type -> this.structureSchemaFinder.getStructureType(type.getType()))
                .filter(ComplexStructureType.class::isInstance)
                .map(ComplexStructureType.class::cast)
                .flatMap(
                    type -> this.doNewFieldRecursion(
                        mapperContext,
                        fieldDefinition,
                        type,
                        modifiedStructureType,
                        newFieldDefinition
                    ).stream()
                )
        ).toList();
  }

  @NotNull
  private List<OperationDefinitionDTO> createDeeperOperations(
      MapperContext mapperContext,
      FieldDefinition fieldDefinition
  ) {
    return fieldDefinition.getTypes().stream()
        .filter(type -> !type.isReference())
        .filter(type -> !type.isContentReferenced())
        .map(type -> this.structureSchemaFinder.getStructureType(type.getType()))
        .filter(ComplexStructureType.class::isInstance)
        .map(ComplexStructureType.class::cast)
        .flatMap(type -> this.doRecursion(mapperContext, fieldDefinition, type).stream())
        .collect(Collectors.groupingBy(OperationDefinitionDTO::getId))
        .values()
        .stream()
        .map(grouped -> grouped.stream().reduce(OperationDefinitionDTO::merge).get())
        .toList();
  }

  private List<OperationDefinitionDTO> doRecursion(
      MapperContext mapperContext,
      FieldDefinition fieldDefinition,
      ComplexStructureType type
  ) {
    var newContext = mapperContext.addPath(fieldDefinition.getName());
    if (fieldDefinition.isList()) {
      return this.createOperationsRecursively(
          newContext.setNewListParent(fieldDefinition.getName(), newContext.getPath()),
          type
      );
    }
    return this.createOperationsRecursively(newContext, type);
  }

  private List<OperationDefinitionDTO> doNewFieldRecursion(
      MapperContext mapperContext,
      FieldDefinition fieldDefinition,
      ComplexStructureType type,
      ComplexStructureType modifiedStructureType,
      FieldDefinition newFieldDefinition
  ) {
    var newContext = mapperContext.addPath(fieldDefinition.getName());
    if (fieldDefinition.isList()) {
      return this.processNewFieldOnComplexRecursively(
          newContext.setNewListParent(fieldDefinition.getName(), newContext.getPath()),
          type,
          modifiedStructureType,
          newFieldDefinition
      );
    }
    return this.processNewFieldOnComplexRecursively(
        newContext,
        type,
        modifiedStructureType,
        newFieldDefinition
    );
  }

  private OperationDefinitionDTO createAddItemCommand(
      MapperContext mapperContext,
      FieldDefinition fieldDefinition
  ) {
    var fieldName = fieldDefinition.getName();
    var commandId = this.createAddItemCommandId(mapperContext, fieldName);
    var commandName = this.createAddItemCommandName(mapperContext, fieldName);
    var generatedSourcePath = String.format(
        "%s.%s",
        String.join(".", mapperContext.getPath()),
        fieldDefinition.getName()
    );
    var parameters = this.createFieldParameters(
        generatedSourcePath,
        fieldDefinition,
        1,
        "*"
    );
    var description = String.format(
        "Generated command for adding %s(%s) on %s Aggregate",
        fieldName,
        parameters.stream().map(ParameterDTO::getType).collect(Collectors.joining(" | ")),
        mapperContext.getResourceType()
    );
    if (mapperContext.getLastParentListPath() != null) {
      parameters = new ArrayList<>(parameters);
      var idParameter = new ParameterDTO(
          String.format("%sId", mapperContext.getLastParentListName()),
          "in",
          1,
          "1",
          IdAttributeValue.SERIALIZATION_TYPE,
          new ReferencedFrom(
              String.format("%s.id", String.join(".", mapperContext.getLastParentListPath()))
          ),
          List.of()
      );
      parameters.add(idParameter);
    }
    return new OperationDefinitionDTO(
        commandId,
        commandName,
        "draft",
        "operation",
        description,
        commandId,
        List.of(mapperContext.getResourceType()),
        false,
        false,
        true,
        parameters
    );
  }

  private String createAddItemCommandId(
      MapperContext mapperContext,
      String fieldName
  ) {
    return String.format(
        "Add%sOn%s",
        StringUtils.capitalize(fieldName),
        mapperContext.getPath().stream().map(StringUtils::capitalize).collect(Collectors.joining())
    );
  }

  private String createAddItemCommandName(
      MapperContext mapperContext,
      String fieldName
  ) {
    return String.format(
        "Add %s on %s",
        StringUtils.capitalize(fieldName),
        String.join(".", mapperContext.getPath())
    );
  }

  private static class MapperContext {

    private final String resourceType;
    private final List<String> path;
    private final @Nullable String lastParentListName;
    private final @Nullable List<String> lastParentListPath;

    public MapperContext(
        String resourceType,
        List<String> path,
        @Nullable String lastParentListName,
        @Nullable List<String> lastParentListPath
    ) {
      this.resourceType = resourceType;
      this.path = path;
      this.lastParentListName = lastParentListName;
      this.lastParentListPath = lastParentListPath;
    }

    public MapperContext(String resourceType, List<String> path) {
      this(resourceType, path, null, null);
    }

    public MapperContext addPath(String pathPart) {
      var newPath = new ArrayList<>(this.path);
      newPath.add(pathPart);
      return new MapperContext(
          this.resourceType,
          newPath,
          this.lastParentListName,
          this.lastParentListPath
      );
    }

    public MapperContext setNewListParent(
        String lastParentListName,
        List<String> lastParentListPath
    ) {
      return new MapperContext(
          this.resourceType,
          this.path,
          lastParentListName,
          lastParentListPath
      );
    }

    public String getResourceType() {
      return resourceType;
    }

    public List<String> getPath() {
      return path;
    }

    @Nullable
    public String getLastParentListName() {
      return lastParentListName;
    }

    @Nullable
    public List<String> getLastParentListPath() {
      return lastParentListPath;
    }
  }
}
