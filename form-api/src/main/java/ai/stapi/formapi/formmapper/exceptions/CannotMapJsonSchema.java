package ai.stapi.formapi.formmapper.exceptions;

public class CannotMapJsonSchema extends RuntimeException {

  private CannotMapJsonSchema(String becauseMessage) {
    super("Cannot map JSON Schema, because " + becauseMessage);
  }
  
  public static CannotMapJsonSchema becauseUnknownPrimitiveTypeEncountered(String type) {
    return new CannotMapJsonSchema(
        String.format("unknown primitive type encountered.%nType: '%s'", type)
    );
  }
}
