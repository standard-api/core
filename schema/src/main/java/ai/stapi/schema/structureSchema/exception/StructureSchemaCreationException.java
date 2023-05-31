package ai.stapi.schema.structureSchema.exception;

import ai.stapi.schema.structureSchemaMapper.UnresolvableSerializationType;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class StructureSchemaCreationException extends RuntimeException {

  private StructureSchemaCreationException(String message) {
    super(message);
  }

  public static StructureSchemaCreationException becauseItContainsUnresolvableTypes(
      List<UnresolvableSerializationType> unresolvableTypes
  ) {
    var errorMessage = new StringBuilder();
    errorMessage
        .append("Structure schema cannot be created because it has missing dependencies. Details:'")
        .append(System.lineSeparator());
    unresolvableTypes.forEach(unresolvableSerializationType -> {
      errorMessage
          .append(System.lineSeparator())
          .append("Failed type: '")
          .append(unresolvableSerializationType.serializationType())
          .append("', ")
          .append("missing dependencies: [")
          .append(StringUtils.join(unresolvableSerializationType.missingDependencies(), ", "))
          .append("]");
    });

    return new StructureSchemaCreationException(errorMessage.toString());
  }

}
