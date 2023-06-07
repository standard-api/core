package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.ActualValueCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;

public class FieldStructureTypeCommandViolation extends ActualValueCommandConstrainViolation {

  private final boolean shouldBeList;

  private FieldStructureTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue,
      boolean shouldBeList
  ) {
    super(commandName, fieldPath, Level.ERROR, actualValue);
    this.shouldBeList = shouldBeList;
  }

  public static FieldStructureTypeCommandViolation notList(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue
  ) {
    return new FieldStructureTypeCommandViolation(
        commandName,
        fieldPath,
        actualValue,
        true
    );
  }

  public static FieldStructureTypeCommandViolation notSingle(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualValue
  ) {
    return new FieldStructureTypeCommandViolation(
        commandName,
        fieldPath,
        actualValue,
        false
    );
  }

  @Override
  protected String getBecauseMessage() {
    return String.format(
        "Field had %s is required.",
        this.shouldBeList ? "single value, but list" : "list of values, but only single value"
    );
  }
}
