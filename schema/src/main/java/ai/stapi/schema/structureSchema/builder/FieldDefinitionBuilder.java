package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldDefinitionBuilder {

  private Integer min;
  private String max;
  private String name;
  private String description;
  private String parentDefinitionType;
  private List<FieldType> types = new ArrayList<>();

  public FieldDefinitionBuilder setMin(Integer min) {
    this.min = Objects.requireNonNullElse(min, 0);
    return this;
  }

  public FieldDefinitionBuilder setMax(String max) {
    this.max = Objects.requireNonNullElse(max, "*");
    return this;
  }

  public FieldDefinitionBuilder setParentDefinitionType(String parentDefinitionType) {
    this.parentDefinitionType = parentDefinitionType;
    return this;
  }

  public FieldDefinitionBuilder addType(FieldType fieldType) {
    if (this.types.stream().noneMatch(
        type -> type.getType().equals(fieldType.getType())
            && type.getOriginalType().equals(fieldType.getOriginalType()))) {
      this.types.add(fieldType);
    }
    return this;
  }

  public FieldDefinition build() {
    return new FieldDefinition(name, min, max, description, types, parentDefinitionType);
  }

  public FieldDefinitionBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public FieldDefinitionBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<FieldType> getTypes() {
    return types;
  }

  public FieldDefinitionBuilder setTypes(List<FieldType> types) {
    this.types = types;
    return this;
  }
}