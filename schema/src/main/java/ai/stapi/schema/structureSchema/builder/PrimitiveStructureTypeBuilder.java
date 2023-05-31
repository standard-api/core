package ai.stapi.schema.structureSchema.builder;

import static ai.stapi.schema.structureSchema.AbstractStructureType.PRIMITIVE_TYPE;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimitiveStructureTypeBuilder extends AbstractStructureTypeBuilder {

  private String containedInNonAnonymousType;

  public PrimitiveStructureTypeBuilder() {
    super();
    this.kind = PRIMITIVE_TYPE;
  }

  public PrimitiveStructureTypeBuilder copyToBuilder(AbstractStructureType primitiveStructureType) {
    if (!(primitiveStructureType instanceof PrimitiveStructureType)) {
      throw new RuntimeException("Trying to copy AbstractStructureType with wrong builder.");
    }
    return new PrimitiveStructureTypeBuilder()
        .setSerializationType(primitiveStructureType.getDefinitionType())
        .setIsAbstract(primitiveStructureType.isAbstract())
        .setDescription(primitiveStructureType.getDescription())
        .setParent(primitiveStructureType.getParent());
  }

  public PrimitiveStructureTypeBuilder setParent(String parent) {
    this.parent = parent;
    return this;
  }

  @Override
  public AbstractStructureTypeBuilder setContainedInNonAnonymousType(
      String containedInNonAnonymousType) {
    this.containedInNonAnonymousType = containedInNonAnonymousType;
    return this;
  }

  @Override
  public List<String> findMissingTypesForFields(StructureSchemaBuilder structureSchemaBuilder) {
    return findMissingDependenciesForParent(structureSchemaBuilder);
  }

  @Override
  public List<FieldType> getAllFieldTypes() {
    return new ArrayList<>();
  }

  @Override
  public String getContainedInNonAnonymousType() {
    return this.containedInNonAnonymousType;
  }

  @Override
  protected AbstractStructureTypeBuilder specificMergeOverwrite(
      AbstractStructureTypeBuilder other) {
    return this;
  }

  @Override
  public Map<String, FieldDefinitionBuilder> getAllFields(
      StructureSchemaBuilder structureSchemaBuilder) {
    return new HashMap<>();
  }

  public PrimitiveStructureTypeBuilder setSerializationType(String serializationType) {
    this.serializationType = serializationType;
    return this;
  }

  public PrimitiveStructureTypeBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public PrimitiveStructureTypeBuilder setIsAbstract(Boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  public PrimitiveStructureType build(StructureSchemaBuilder structureSchemaBuilder) {
    return new PrimitiveStructureType(serializationType, description, isAbstract, parent);
  }
}