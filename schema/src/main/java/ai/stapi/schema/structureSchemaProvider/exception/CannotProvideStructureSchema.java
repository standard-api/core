package ai.stapi.schema.structureSchemaProvider.exception;

import ai.stapi.schema.structureSchemaMapper.UnresolvableType;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class CannotProvideStructureSchema extends Exception {

  private final String missingSerializationType;

  public CannotProvideStructureSchema(
      String missingSerializationType,
      List<UnresolvableType> unresolvableTypes
  ) {
    super(createMessage(missingSerializationType, unresolvableTypes).toString());
    this.missingSerializationType = missingSerializationType;
  }

  @NotNull
  private static StringBuilder createMessage(
      String missingSerializationType,
      List<UnresolvableType> unresolvableTypes
  ) {
    var errorMessage = new StringBuilder()
        .append("Structure schema for serialization type '")
        .append(missingSerializationType)
        .append("' not found.")
        .append(System.lineSeparator())
        .append("Current pending unresolvable types: ");
    unresolvableTypes.forEach(unresolvable -> {
      errorMessage
          .append(System.lineSeparator())
          .append("Failed type: '")
          .append(unresolvable.structureDefinitionData().getId())
          .append("', ")
          .append("missing dependencies: [")
          .append(StringUtils.join(unresolvable.missingDependencies(), ", "))
          .append("]");
    });
    return errorMessage;
  }

  public String getMissingSerializationType() {
    return missingSerializationType;
  }
}
