package ai.stapi.schema.structureSchemaMapper.exception;

public class UnresolvableTypeException extends RuntimeException {

  private final String fieldType;

  public UnresolvableTypeException(String fieldType) {
    super("Could not resolve field of type " + fieldType);
    this.fieldType = fieldType;
  }

  public String getFieldType() {
    return fieldType;
  }
}
