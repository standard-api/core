package ai.stapi.schema.structureSchema.exception;

public class FieldDoesNotExist extends RuntimeException {

  public FieldDoesNotExist(String message) {
    super(message);
  }
}
