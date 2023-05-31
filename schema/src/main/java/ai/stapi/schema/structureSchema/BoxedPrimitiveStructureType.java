package ai.stapi.schema.structureSchema;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class BoxedPrimitiveStructureType extends ComplexStructureType {

  public static final String SERIALIZATION_TYPE = "BoxedPrimitiveStructureType";
  public static final String KIND = PRIMITIVE_TYPE;

  private BoxedPrimitiveStructureType() {
    super();
  }

  public BoxedPrimitiveStructureType(
      String definitionType,
      String description,
      Map<String, FieldDefinition> fields,
      boolean isAbstract,
      String parent
  ) {
    super(
        SERIALIZATION_TYPE,
        KIND,
        definitionType,
        fields,
        description,
        parent,
        isAbstract
    );
  }

  @Override
  public BoxedPrimitiveStructureType copyWithNewFields(Map<String, FieldDefinition> newFields) {
    return new BoxedPrimitiveStructureType(
        definitionType,
        description,
        newFields,
        isAbstract,
        parent
    );
  }

  public String getOriginalDefinitionType() {
    return StringUtils.uncapitalize(this.definitionType.replace("Boxed", ""));
  }
}
