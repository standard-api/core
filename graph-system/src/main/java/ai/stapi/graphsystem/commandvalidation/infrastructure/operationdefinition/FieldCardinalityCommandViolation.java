package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.AbstractCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public class FieldCardinalityCommandViolation extends AbstractCommandConstrainViolation {

  private final Integer actualSize;
  private final Integer requiredMin;
  private final String requiredMax;

  public FieldCardinalityCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Integer actualSize,
      Integer requiredMin,
      String requiredMax
  ) {
    super(commandName, fieldPath, Level.ERROR);
    this.actualSize = actualSize;
    this.requiredMin = requiredMin;
    this.requiredMax = requiredMax;
  }

  @Override
  protected String getBecauseMessage() {
    return String.format(
        "Wrong cardinality. Required size should be %s. Actual size was: %s.",
        this.getRequiredSizeMessage(),
        this.actualSize
    );
  }

  public Integer getActualSize() {
    return actualSize;
  }

  public Integer getRequiredMin() {
    return requiredMin;
  }

  public String getRequiredMax() {
    return requiredMax;
  }

  private String getRequiredSizeMessage() {
    if (this.requiredMax.equals("*")) {
      return String.format("higher than %s", this.requiredMin);
    }
    return String.format("between %s-%s", this.requiredMin, this.requiredMax);
  }
}
