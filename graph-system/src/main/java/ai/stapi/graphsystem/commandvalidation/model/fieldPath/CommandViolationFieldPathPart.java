package ai.stapi.graphsystem.commandvalidation.model.fieldPath;

import org.jetbrains.annotations.Nullable;

public class CommandViolationFieldPathPart {

  private final String fieldName;

  @Nullable
  private String typeName;

  public CommandViolationFieldPathPart(String fieldName) {
    this.fieldName = fieldName;
  }

  public CommandViolationFieldPathPart(String fieldName, @Nullable String typeName) {
    this.fieldName = fieldName;
    this.typeName = typeName;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Nullable
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(@Nullable String typeName) {
    this.typeName = typeName;
  }

  public String render() {
    var type = this.isTypeNameSet() ? String.format("<%s>", this.getTypeName()) : "";
    var fieldName = this.getFieldName();
    try {
      Integer.parseInt(fieldName);
    } catch (IllegalArgumentException e) {
      return String.format(".%s%s", fieldName, type);
    }
    return String.format("[%s]%s", fieldName, type);
  }

  private boolean isTypeNameSet() {
    return this.getTypeName() != null && !this.getTypeName().isBlank();
  }
}
