package ai.stapi.schema.structureSchema;

import ai.stapi.serialization.AbstractSerializableObject;
import java.util.Comparator;
import java.util.List;

public class FieldDefinition extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "FieldDefinition";
  public static final String LIST_STRUCTURE_TYPE = "list";
  public static final String LEAF_STRUCTURE_TYPE = "leaf";
  private String name;
  private Integer min;
  private String max;
  private String description;
  private List<FieldType> types;

  private String parentDefinitionType;

  private FieldDefinition() {
    super(SERIALIZATION_TYPE);
  }

  public FieldDefinition(
      String name,
      Integer min,
      String max,
      String description,
      List<FieldType> types,
      String parentDefinitionType
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.min = min;
    this.max = max;
    this.description = description;
    this.types = types.stream()
        .sorted(Comparator.comparing(FieldType::getType))
        .toList();
    this.parentDefinitionType = parentDefinitionType;
  }

  public Integer getMin() {
    return min;
  }

  public String getMax() {
    return max;
  }

  public Float getFloatMax() {
    if (this.max.equals("*")) {
      return Float.POSITIVE_INFINITY;
    }
    return Float.parseFloat(this.max);
  }

  public List<FieldType> getTypes() {
    return types;
  }

  public String getStructureType() {
    return isList() ? LIST_STRUCTURE_TYPE : LEAF_STRUCTURE_TYPE;
  }

  public boolean isList() {
    try {
      var value = Integer.parseInt(this.max);
      return value > 1;
    } catch (NumberFormatException ignored) {

    }
    return this.max.equals("*");
  }

  public boolean isRequired() {
    return this.min > 0;
  }

  public boolean isUnionType() {
    return this.types.size() > 1;
  }

  public boolean isAnyType() {
    return this.types.isEmpty();
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public String getParentDefinitionType() {
    return parentDefinitionType;
  }
}
