package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.BoxedPrimitiveStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class BoxedStructureTypeBuilder extends AbstractComplexStructureTypeBuilder {

  protected String originalSerializationType = "";
  private String containedInNonAnonymousType;
  public BoxedStructureTypeBuilder() {
    this.kind = BoxedPrimitiveStructureType.KIND;
  }

  public AbstractStructureType build(StructureSchemaBuilder structureSchemaBuilder) {
    var builtFields = new HashMap<String, FieldDefinition>();
    this.getAllFields(structureSchemaBuilder).forEach((key, value) -> builtFields.put(
        key,
        value.build()
    ));

    return new BoxedPrimitiveStructureType(
        this.boxifySerializationType(serializationType),
        description,
        builtFields,
        isAbstract,
        parent
    );
  }

  public String getContainedInNonAnonymousType() {
    return containedInNonAnonymousType;
  }

  public BoxedStructureTypeBuilder setContainedInNonAnonymousType(
      String containedInNonAnonymousType) {
    this.containedInNonAnonymousType = containedInNonAnonymousType;
    return this;
  }

  @Override
  public AbstractStructureTypeBuilder specificMergeOverwrite(AbstractStructureTypeBuilder other) {
    return other;
  }

  public String getOriginalSerializationType() {
    return this.originalSerializationType;
  }

  public BoxedStructureTypeBuilder copyToBuilder(AbstractStructureType abstractStructureType) {
    if (!(abstractStructureType instanceof BoxedPrimitiveStructureType boxedPrimitiveStructureType)) {
      throw new RuntimeException("Trying to copy AbstractStructureType with wrong builder.");
    }
    var boxed = new BoxedStructureTypeBuilder()
        .setSerializationType(boxedPrimitiveStructureType.getOriginalDefinitionType())
        .setIsAbstract(boxedPrimitiveStructureType.isAbstract())
        .setDescription(boxedPrimitiveStructureType.getDescription())
        .setParent(boxedPrimitiveStructureType.getParent());

    boxedPrimitiveStructureType.getAllFields().forEach(
        (key, value) -> boxed.addField(key)
            .setDescription(value.getDescription())
            .setMax(value.getMax())
            .setMin(value.getMin())
            .setTypes(value.getTypes())
            .setName(value.getName())
            .setParentDefinitionType(value.getParentDefinitionType())
    );
    return boxed;
  }

  public BoxedStructureTypeBuilder setSerializationType(String serializationType) {
    this.serializationType = this.boxifySerializationType(serializationType);
    this.originalSerializationType = serializationType;

    this.addField("value")
        .setMin(0)
        .setMax("1")
        .setDescription("Primitive value for " + this.boxifySerializationType(serializationType))
        .addType(FieldType.asPlainType(originalSerializationType));
    return this;
  }

  public BoxedStructureTypeBuilder setParent(String parent) {
    if (parent != null && !parent.isBlank()) {
      if (parent.equals("Element")) {
        this.parent = parent;
      } else {
        this.parent = this.boxifySerializationType(parent);
      }
    } else {
      this.parent = "Element";
    }
    return this;
  }


  public BoxedStructureTypeBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public BoxedStructureTypeBuilder setIsAbstract(Boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  private String boxifySerializationType(String serializationType) {
    if (serializationType.startsWith("Boxed")) {
      return serializationType;
    }
    return "Boxed" + StringUtils.capitalize(serializationType);
  }
}