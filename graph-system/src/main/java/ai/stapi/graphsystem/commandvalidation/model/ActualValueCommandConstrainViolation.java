package ai.stapi.graphsystem.commandvalidation.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public abstract class ActualValueCommandConstrainViolation
    extends AbstractCommandConstrainViolation {

  private final Object actualValue;

  private final ObjectWriter objectWriter;

  protected ActualValueCommandConstrainViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Level level,
      Object actualValue
  ) {
    super(commandName, fieldPath, level);
    this.actualValue = actualValue;
    this.objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
  }

  @Override
  public String getMessage() {
    return super.getMessage() + "\nActual value: " + this.renderActualValue();
  }

  private String renderActualValue() {
    try {
      return this.objectWriter.writeValueAsString(this.actualValue);
    } catch (JsonProcessingException e) {
      return this.actualValue.toString();
    }
  }

  public Object getActualValue() {
    return actualValue;
  }
}
