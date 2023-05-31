package ai.stapi.schema.structureSchema;

import java.util.Map;

public class ResourceStructureType extends ComplexStructureType {

  public static final String SERIALIZATION_TYPE = "ResourceStructureType";
  public static final String KIND = AbstractStructureType.RESOURCE;

  private ResourceStructureType() {
    super();
  }

  public ResourceStructureType(
      String definitionType,
      Map<String, FieldDefinition> fields,
      String description,
      String parent,
      boolean isAbstract
  ) {
    super(
        SERIALIZATION_TYPE,
        AbstractStructureType.RESOURCE,
        definitionType,
        fields,
        description,
        parent,
        isAbstract
    );
  }

  @Override
  public ResourceStructureType copyWithNewFields(Map<String, FieldDefinition> newFields) {
    return new ResourceStructureType(
        definitionType,
        newFields,
        description,
        parent,
        isAbstract
    );
  }
}
