package ai.stapi.schema.structureSchema;

import org.apache.commons.lang3.StringUtils;

public class FieldType {

  private final String type;
  private final String originalType;
  private final FieldTypeGroup typeGroup;

  private final boolean isContentReferenced;

  public FieldType(
      String type,
      String originalType
  ) {
    this(type, originalType, FieldTypeGroup.STANDARD);
  }

  public FieldType(
      String type,
      String originalType,
      FieldTypeGroup fieldTypeGroup
  ) {
    this(type, originalType, fieldTypeGroup, false);
  }

  private FieldType(
      String type,
      String originalType,
      FieldTypeGroup fieldTypeGroup,
      boolean isContentReferenced
  ) {
    this.type = type;
    this.originalType = originalType;
    this.typeGroup = fieldTypeGroup;
    this.isContentReferenced = isContentReferenced;
  }

  public static FieldType asReferenceType(String type) {
    return new FieldType(type, "Reference", FieldTypeGroup.REFERENCE);
  }

  public static FieldType asContentReferenced(String type, String originalName) {
    return new FieldType(
        type,
        originalName,
        FieldTypeGroup.ANONYMOUS,
        true
    );
  }

  public static FieldType asAnonymousType(String type, String originalName) {
    if (!originalName.equals("Element") && !originalName.equals("BackboneElement")) {
      throw new RuntimeException(
          "Anonymous type's original name must be either Element or BackboneElement");
    }
    return new FieldType(type, originalName, FieldTypeGroup.ANONYMOUS);
  }

  public static FieldType asBoxedType(String type) {
    String typeName;
    String originalName;
    if (type.startsWith("Boxed")) {
      typeName = type;
      originalName = type.replace("Boxed", "").toLowerCase();
    } else {
      typeName = "Boxed" + StringUtils.capitalize(type);
      originalName = type;
    }
    return new FieldType(typeName, originalName, FieldTypeGroup.BOXED);
  }

  public static FieldType asPlainType(String type) {
    return new FieldType(type, type);
  }

  public String getType() {
    return type;
  }

  public String getOriginalType() {
    return originalType;
  }

  public FieldTypeGroup getTypeGroup() {
    return typeGroup;
  }

  public boolean isReference() {
    return this.originalType.equals("Reference");
  }

  public boolean isAnonymous() {
    return this.originalType.equals("Element") || this.originalType.equals("BackboneElement");
  }

  public boolean isBoxed() {
    return this.type.startsWith("Boxed") && !this.type.equals(this.originalType);
  }

  public boolean isPrimitiveType() {
    return this.type.equals(this.originalType) && Character.isLowerCase(this.type.charAt(0));
  }

  public boolean isContentReferenced() {
    return isContentReferenced;
  }
}
