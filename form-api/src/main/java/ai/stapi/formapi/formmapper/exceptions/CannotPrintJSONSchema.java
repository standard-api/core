package ai.stapi.formapi.formmapper.exceptions;

public class CannotPrintJSONSchema extends RuntimeException {

  public CannotPrintJSONSchema(Throwable cause) {
    super("Something went wrong when printing JSON Schema.", cause);
  }
}
