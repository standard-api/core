package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class UpdateResourceOperationMapper extends AbstractResourceOperationsMapper {

  public UpdateResourceOperationMapper(StructureSchemaFinder structureSchemaFinder) {
    super(structureSchemaFinder);
  }

  @Override
  public ResourceOperationsMapperResult map(ResourceStructureType resourceStructureType) {
    if (resourceStructureType.isAbstract()) {
      return new ResourceOperationsMapperResult();
    }
    var resourceName = resourceStructureType.getDefinitionType();
    var createCommandId = this.createCreateCommandId(resourceName);
    var createCommandName = this.createCreateCommandName(resourceName);
    return new ResourceOperationsMapperResult(
        new OperationDefinitionDTO(
            createCommandId,
            createCommandName,
            "draft",
            "operation",
            "Generated command for updating " + resourceName + " with only primitive fields.",
            createCommandName,
            List.of(resourceName),
            false,
            true,
            true,
            this.createParameters(resourceStructureType)
        )
    );
  }

  @Override
  public ResourceOperationsMapperResult mapNewFields(
      ResourceStructureType rootResourceType,
      ComplexStructureType modifiedStructureType,
      List<String> newFieldNames
  ) {
    if (rootResourceType.isAbstract()) {
      return new ResourceOperationsMapperResult();
    }
    var rootType = rootResourceType.getDefinitionType();
    var modifiedType = modifiedStructureType.getDefinitionType();
    if (!(this.structureSchemaFinder.isEqualOrInherits(rootType, modifiedType))) {
      return new ResourceOperationsMapperResult();
    }
    return newFieldNames.stream().map(fieldName -> {
          var newField = rootResourceType.getField(fieldName);
          if (newField.getName().equals("id")) {
            return new ResourceOperationsMapperResult();
          }
          if (!newField.getMax().equals("1")) {
            return new ResourceOperationsMapperResult();
          }
          if (!newField.getTypes().stream().allMatch(type -> type.isPrimitiveType() || type.isBoxed())) {
            return new ResourceOperationsMapperResult();
          }
          var definitionType = rootResourceType.getDefinitionType();
          var commandId = this.createCreateCommandId(definitionType);
          return new ResourceOperationsMapperResult(
              new OperationDefinitionParameters(
                  commandId,
                  this.createParametersForField(definitionType, newField).toList()
              )
          );
        }).reduce(ResourceOperationsMapperResult::merge)
        .orElse(new ResourceOperationsMapperResult());
  }

  @NotNull
  private List<ParameterDTO> createParameters(ResourceStructureType resourceStructureType) {
    var definitionType = resourceStructureType.getDefinitionType();
    return resourceStructureType.getAllFields().values().stream()
        .filter(field -> !field.getName().equals("id"))
        .filter(field -> field.getMax().equals("1"))
        .filter(field -> field.getTypes().stream().allMatch(type -> type.isPrimitiveType() || type.isBoxed()))
        .flatMap(field -> this.createParametersForField(definitionType, field))
        .toList();
  }

  private Stream<ParameterDTO> createParametersForField(
      String definitionType,
      FieldDefinition field
  ) {
    var generatedPath = String.format(
        "%s.%s",
        definitionType,
        field.getName()
    );
    return this.createFieldParameters(generatedPath, field, 0, field.getMax()).stream();
  }

  private String createCreateCommandId(String resourceType) {
    return "Update" + resourceType;
  }

  private String createCreateCommandName(String resourceType) {
    return "Update " + resourceType;
  }
}
