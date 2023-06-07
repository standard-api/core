package ai.stapi.graphsystem.commandvalidation.model.fieldPath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandViolationFieldPath {

  private final List<CommandViolationFieldPathPart> commandViolationFieldPathParts;

  public CommandViolationFieldPath() {
    this.commandViolationFieldPathParts = new ArrayList<>();
  }

  private CommandViolationFieldPath(
      List<CommandViolationFieldPathPart> commandViolationFieldPathParts
  ) {
    this.commandViolationFieldPathParts = commandViolationFieldPathParts;
  }

  @NotNull
  public CommandViolationFieldPath add(CommandViolationFieldPathPart newPart) {
    var newFieldPath = new ArrayList<>(this.commandViolationFieldPathParts);
    newFieldPath.add(newPart);
    return new CommandViolationFieldPath(newFieldPath);
  }

  @NotNull
  public CommandViolationFieldPath add(String fieldName, @Nullable String typeName) {
    return this.add(
        new CommandViolationFieldPathPart(fieldName, typeName)
    );
  }

  @NotNull
  public CommandViolationFieldPath add(String fieldName) {
    return this.add(fieldName, null);
  }

  public CommandViolationFieldPath setTypeNameToLast(String typeName) {
    var last = this.commandViolationFieldPathParts.size() - 1;
    this.commandViolationFieldPathParts.get(last).setTypeName(typeName);
    return this;
  }

  public String renderPath() {
    return this.commandViolationFieldPathParts.stream()
        .map(CommandViolationFieldPathPart::render)
        .collect(Collectors.joining());
  }
}
