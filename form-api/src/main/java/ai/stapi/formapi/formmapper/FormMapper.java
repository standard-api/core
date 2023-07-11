package ai.stapi.formapi.formmapper;

import ai.stapi.formapi.formmapper.exceptions.CannotPrintJSONSchema;
import ai.stapi.graph.attribute.attributeValue.Base64BinaryAttributeValue;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.CanonicalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.CodeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.MarkdownAttributeValue;
import ai.stapi.graph.attribute.attributeValue.OidAttributeValue;
import ai.stapi.graph.attribute.attributeValue.PositiveIntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UnsignedIntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UriAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UrlAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UuidAttributeValue;
import ai.stapi.graph.attribute.attributeValue.XhtmlAttributeValue;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Set;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.springframework.stereotype.Service;

@Service
public class FormMapper {
  
  public static Set<String> STRING_LIKE_PRIMITIVES = Set.of(
      Base64BinaryAttributeValue.SERIALIZATION_TYPE,
      CanonicalAttributeValue.SERIALIZATION_TYPE,
      CodeAttributeValue.SERIALIZATION_TYPE,
      DateAttributeValue.SERIALIZATION_TYPE,
      DateTimeAttributeValue.SERIALIZATION_TYPE,
      IdAttributeValue.SERIALIZATION_TYPE,
      InstantAttributeValue.SERIALIZATION_TYPE,
      MarkdownAttributeValue.SERIALIZATION_TYPE,
      OidAttributeValue.SERIALIZATION_TYPE,
      TimeAttributeValue.SERIALIZATION_TYPE,
      UriAttributeValue.SERIALIZATION_TYPE,
      UrlAttributeValue.SERIALIZATION_TYPE,
      UuidAttributeValue.SERIALIZATION_TYPE,
      XhtmlAttributeValue.SERIALIZATION_TYPE,
      StringAttributeValue.SERIALIZATION_TYPE
  );

  public static Set<String> NUMBER_LIKE_PRIMITIVES = Set.of(
      DecimalAttributeValue.SERIALIZATION_TYPE,
      PositiveIntegerAttributeValue.SERIALIZATION_TYPE,
      UnsignedIntegerAttributeValue.SERIALIZATION_TYPE,
      IntegerAttributeValue.SERIALIZATION_TYPE
  );

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
    if (STRING_LIKE_PRIMITIVES.contains(parameter.getType())) {
      builder.addPropertySchema(parameterName, new StringSchema());
    }
    if (NUMBER_LIKE_PRIMITIVES.contains(parameter.getType())) {
      builder.addPropertySchema(parameterName, new NumberSchema());
    }
    if (parameter.getType().equals(BooleanAttributeValue.SERIALIZATION_TYPE)) {
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
