package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO.ReferencedFrom;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractResourceOperationsMapper implements ResourceOperationsMapper {

  protected final StructureSchemaFinder structureSchemaFinder;

  protected AbstractResourceOperationsMapper(StructureSchemaFinder structureSchemaFinder) {
    this.structureSchemaFinder = structureSchemaFinder;
  }

  protected abstract ResourceOperationsMapperResult mapNewFields(
      ResourceStructureType rootResourceType,
      ComplexStructureType modifiedStructureType,
      List<String> newFieldNames
  );

  @Override
  public Map<String, ResourceOperationsMapperResult> mapNewFields(
      ComplexStructureType modifiedStructureType,
      List<String> newFieldNames
  ) {
    var resources = this.structureSchemaFinder.getAllResources()
        .stream()
        .toList();

    var result = new HashMap<String, ResourceOperationsMapperResult>();
    resources.forEach(resourceStructureType -> {
      var resourceResult = this.mapNewFields(
          resourceStructureType,
          modifiedStructureType,
          newFieldNames
      );
      if (!resourceResult.isEmpty()) {
        result.put(
            String.format("%sAggregate", resourceStructureType.getDefinitionType()),
            resourceResult
        );
      }
    });
    return result;
  }

  @NotNull
  protected List<ParameterDTO> createFieldParameters(
      String resourceSourcePath,
      FieldDefinition field,
      Integer defaultMin,
      String defaultMax
  ) {
    if (field.getTypes().isEmpty()) {
      var message = String.format(
          "Field at path '%s' has no types.%n%s%n%s",
          resourceSourcePath,
          "Therefore it will not be included in Operation Definition for it.",
          "Probably is caused by bug in resolving contentReference in its Structure Definition."
      );
      Logger.getLogger(this.getClass().getCanonicalName()).log(
          Level.WARNING,
          message
      );
      return List.of();
    }
    if (field.isUnionType()) {
      var references = field.getTypes().stream()
          .filter(FieldType::isReference)
          .toList();

      var rest = field.getTypes().stream()
          .filter(fieldType -> !fieldType.isReference())
          .map(fieldType -> fieldType.isBoxed() ? fieldType.getOriginalType() : fieldType.getType())
          .map(fieldType ->
              new ParameterDTO(
                  String.format("%s%s", field.getName(), StringUtils.capitalize(fieldType)),
                  "in",
                  defaultMin,
                  defaultMax,
                  fieldType,
                  new ReferencedFrom(resourceSourcePath),
                  List.of()
              )
          ).collect(Collectors.toList());

      if (!references.isEmpty()) {
        rest.add(
            new ParameterDTO(
                rest.isEmpty() ? field.getName() : String.format("%sReference", field.getName()),
                "in",
                defaultMin,
                defaultMax,
                "Reference",
                new ReferencedFrom(resourceSourcePath),
                references.stream()
                    .map(FieldType::getType)
                    .map(StructureDefinitionId::new)
                    .toList()
            )
        );
      }

      return rest;
    }
    var type = field.getTypes().get(0);
    return List.of(
        new ParameterDTO(
            field.getName(),
            "in",
            defaultMin,
            defaultMax,
            type.isReference() ? type.getOriginalType() : type.getType(),
            new ReferencedFrom(resourceSourcePath),
            type.isReference() ? List.of(new StructureDefinitionId(type.getType())) : List.of()
        )
    );
  }

  @NotNull
  protected List<ParameterDTO> createFieldParameters(
      String resourceSourcePath,
      FieldDefinition field
  ) {
    return this.createFieldParameters(
        resourceSourcePath,
        field,
        field.getMin(),
        field.getMax()
    );
  }
}
