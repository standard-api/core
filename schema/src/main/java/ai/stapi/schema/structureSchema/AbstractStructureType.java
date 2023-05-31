package ai.stapi.schema.structureSchema;

import ai.stapi.serialization.AbstractSerializableObject;

public abstract class AbstractStructureType extends AbstractSerializableObject {

  public static final String COMPLEX_TYPE = "complex-type";
  public static final String PRIMITIVE_TYPE = "primitive-type";
  public static final String RESOURCE = "resource";

  protected String description;
  protected String kind;
  protected String definitionType;
  protected boolean isAbstract;
  protected String parent;

  protected AbstractStructureType() {
    super();
  }

  public AbstractStructureType(
      String structureTypeSerializationType,
      String kind,
      String definitionType,
      String description,
      boolean isAbstract,
      String parent
  ) {
    super(structureTypeSerializationType);
    this.kind = kind;
    this.definitionType = definitionType;
    this.description = description;
    this.isAbstract = isAbstract;
    this.parent = parent == null ? "" : parent;
  }

  public static boolean isComplexTypeOrResource(String kind) {
    return kind.equals(RESOURCE) || kind.equals(COMPLEX_TYPE);
  }

  public static boolean isPrimitiveType(String kind) {
    return kind.equals(PRIMITIVE_TYPE);
  }

  public String getKind() {
    return kind;
  }

  public String getDefinitionType() {
    return definitionType;
  }

  public String getDescription() {
    return description;
  }

  public boolean isAbstract() {
    return isAbstract;
  }

  public String getParent() {
    return parent;
  }
}
