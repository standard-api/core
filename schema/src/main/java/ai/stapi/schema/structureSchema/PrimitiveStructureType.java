package ai.stapi.schema.structureSchema;

public class PrimitiveStructureType extends AbstractStructureType {

  public static final String SERIALIZATION_TYPE = "PrimitiveStructureType";
  public static final String KIND = AbstractStructureType.PRIMITIVE_TYPE;

  protected PrimitiveStructureType() {
    super();
  }

  protected PrimitiveStructureType(
      String serializationType,
      String definitionType,
      String description,
      boolean isAbstract,
      String parent
  ) {
    super(serializationType, AbstractStructureType.PRIMITIVE_TYPE, definitionType, description,
        isAbstract, parent);
  }

  public PrimitiveStructureType(
      String definitionType,
      String description,
      boolean isAbstract,
      String parent
  ) {
    super(SERIALIZATION_TYPE, AbstractStructureType.PRIMITIVE_TYPE, definitionType, description,
        isAbstract, parent);
  }
}
