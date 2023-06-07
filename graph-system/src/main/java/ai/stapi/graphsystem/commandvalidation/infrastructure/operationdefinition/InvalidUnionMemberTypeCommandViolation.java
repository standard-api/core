package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.commandvalidation.model.AbstractCommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;
import java.util.List;

public class InvalidUnionMemberTypeCommandViolation extends AbstractCommandConstrainViolation {

  private final Object actualMemberType;
  private final List<String> possibleMemberTypes;

  public InvalidUnionMemberTypeCommandViolation(
      String commandName,
      CommandViolationFieldPath fieldPath,
      Object actualMemberType,
      List<String> possibleMemberTypes
  ) {
    super(commandName, fieldPath, Level.ERROR);
    this.actualMemberType = actualMemberType;
    this.possibleMemberTypes = possibleMemberTypes;
  }

  @Override
  protected String getBecauseMessage() {
    var message = "Union member type is not allowed in union. " +
        "Actual member type: %s\n" +
        "Possible member types: %s";

    return String.format(
        message,
        this.actualMemberType,
        this.possibleMemberTypes
    );
  }

  public Object getActualMemberType() {
    return actualMemberType;
  }

  public List<String> getPossibleMemberTypes() {
    return possibleMemberTypes;
  }
}
