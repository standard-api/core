package ai.stapi.formapi.formmapper;

import ai.stapi.formapi.formmapper.exceptions.CannotPrintJSONSchema;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.springframework.stereotype.Service;

@Service
public class FormMapper {

  private final StructureSchemaFinder structureSchemaFinder;

  public FormMapper(StructureSchemaFinder structureSchemaFinder) {
    this.structureSchemaFinder = structureSchemaFinder;
  }

  public Map<String, Object> map(OperationDefinitionDTO operationDefinitionDTO) {
    var builder = new ObjectSchema.Builder();
    operationDefinitionDTO.getParameter()
        .stream()
        .filter(parameter -> parameter.getUse().equals("in"))
        .forEach(parameter -> this.mapParameter(parameter, builder));

    var schema = builder.build();
    return this.printSchema(schema);
  }

  private void mapParameter(
      OperationDefinitionDTO.ParameterDTO parameter,
      ObjectSchema.Builder builder
  ) {
    var parameterName = parameter.getName();
    if (parameter.getMin() > 0) {
      builder.addRequiredProperty(parameterName);
    }
    if (parameter.getType().equals("string")) {
      builder.addPropertySchema(parameterName, new StringSchema());
    }
    if (parameter.getType().equals("integer")) {
      builder.addPropertySchema(parameterName, new NumberSchema());
    }
    if (parameter.getType().equals("boolean")) {
      builder.addPropertySchema(parameterName, new BooleanSchema.Builder().build());
    }
  }

  private Map<String, Object> printSchema(ObjectSchema schema) {
    try {
      return new ObjectMapper().readValue(schema.toString(), new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new CannotPrintJSONSchema(e);
    }
  }
}
