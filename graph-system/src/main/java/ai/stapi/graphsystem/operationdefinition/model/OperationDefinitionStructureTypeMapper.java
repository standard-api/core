package ai.stapi.graphsystem.operationdefinition.model;

import ai.stapi.graphsystem.operationdefinition.exceptions.CannotMapOperationDefinitionToStructureType;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class OperationDefinitionStructureTypeMapper {

  private final StructureSchemaProvider structureSchemaProvider;

  public OperationDefinitionStructureTypeMapper(StructureSchemaProvider structureSchemaProvider) {
    this.structureSchemaProvider = structureSchemaProvider;
  }

  public List<ComplexStructureType> map(List<OperationDefinitionDTO> operationDefinitions) {
    return operationDefinitions.stream().map(this::map).toList();
  }

  public ComplexStructureType map(OperationDefinitionDTO operationDefinition) {
    var operationName = operationDefinition.getId();
    var parameterGroups = this.createParameterGroups(operationDefinition);
    var fakeFields = new HashMap<String, FieldDefinition>();
    parameterGroups.forEach((fieldName, parameters) -> {
      var anyParameter = parameters.get(0);
      var finalFieldName = parameters.size() == 1 ? anyParameter.getName() : fieldName;
      var fakeField = new FieldDefinitionWithSource(
          finalFieldName,
          anyParameter.getMin(),
          anyParameter.getMax(),
          "",
          parameters.stream().flatMap(
              parameter -> this.fakeFieldType(
                  operationName,
                  parameter,
                  parameters.size() > 1
              )
          ).toList(),
          operationName,
          anyParameter.getSingleReferencedFrom().getSource()
      );
      fakeFields.put(finalFieldName, fakeField);
    });

    return new ComplexStructureType(
        operationName,
        fakeFields,
        operationDefinition.getDescription(),
        null,
        false
    );
  }

  private Map<String, List<OperationDefinitionDTO.ParameterDTO>> createParameterGroups(
      OperationDefinitionDTO operationDefinition
  ) {
    var parameterGroups = new HashMap<String, List<OperationDefinitionDTO.ParameterDTO>>();
    operationDefinition.getParameter().forEach(parameter -> {
      if (!parameter.getUse().equals("in")) {
        return;
      }
      var parameterName = parameter.getName();
      var fieldName = this.createFieldName(
          parameterName,
          StringUtils.capitalize(parameter.getType())
      );
      var parameters = parameterGroups.getOrDefault(fieldName, new ArrayList<>());
      parameters.add(parameter);
      parameterGroups.put(fieldName, parameters);
    });
    return parameterGroups;
  }

  private String createFieldName(
      String parameterName,
      String parameterType
  ) {
    int suffixLength = parameterType.length();
    if (parameterName.length() < suffixLength + 1) {
      return parameterName;
    }
    if (parameterName.equals(parameterType)) {
      return parameterName;
    }
    if (!parameterName.endsWith(parameterType)) {
      return parameterName;
    }
    return parameterName.substring(0, parameterName.length() - suffixLength);
  }

  @NotNull
  private Stream<FieldType> fakeFieldType(
      String operationName,
      OperationDefinitionDTO.ParameterDTO parameterDTO,
      boolean isInUnion
  ) {
    var type = parameterDTO.getType();
    if (type.equals("Reference")) {
      return parameterDTO.getTargetProfileReference().stream().map(
          targetProfile -> new FieldType(targetProfile.getId(), type)
      );
    }
    AbstractStructureType structure;
    try {
      structure = this.structureSchemaProvider.provideSpecific(type);
    } catch (CannotProvideStructureSchema e) {
      throw new CannotMapOperationDefinitionToStructureType(
          operationName,
          parameterDTO.getName(),
          type, e
      );
    }
    if (structure instanceof PrimitiveStructureType && isInUnion) {
      return Stream.of(
          new FieldType(
              String.format("Boxed%s", StringUtils.capitalize(type)),
              type
          )
      );
    }
    return Stream.of(
        new FieldType(type, type)
    );
  }
}
