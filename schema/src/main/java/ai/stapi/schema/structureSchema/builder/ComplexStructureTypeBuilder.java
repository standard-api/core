package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import java.util.HashMap;

public class ComplexStructureTypeBuilder extends AbstractComplexStructureTypeBuilder {

  private String containedInNonAnonymousType;

  public ComplexStructureTypeBuilder() {
    super();
  }

  public ComplexStructureTypeBuilder setSerializationType(String serializationType) {
    this.serializationType = serializationType;
    return this;
  }


  public ComplexStructureTypeBuilder copyToBuilder(AbstractStructureType abstractStructureType) {
    if (!(abstractStructureType instanceof ComplexStructureType complexStructureType)) {
      throw new RuntimeException("Trying to copy AbstractStructureType with wrong builder.");
    }
    var complex = new ComplexStructureTypeBuilder()
        .setSerializationType(complexStructureType.getDefinitionType())
        .setIsAbstract(complexStructureType.isAbstract())
        .setKind(complexStructureType.getKind())
        .setDescription(complexStructureType.getDescription())
        .setParent(complexStructureType.getParent());
    complexStructureType.getAllFields().forEach(
        (key, value) -> complex.addField(key)
            .setDescription(value.getDescription())
            .setMax(value.getMax())
            .setMin(value.getMin())
            .setTypes(value.getTypes())
            .setName(value.getName())
            .setParentDefinitionType(value.getParentDefinitionType())
    );
    return complex;
  }

  public AbstractStructureType build(StructureSchemaBuilder structureSchemaBuilder) {
    var builtFields = new HashMap<String, FieldDefinition>();
    this.getAllFields(structureSchemaBuilder).forEach((key, value) -> builtFields.put(
        key,
        value.build()
    ));
    switch (kind) {
      case ResourceStructureType.KIND -> {
        return new ResourceStructureType(
            serializationType,
            builtFields,
            description,
            parent,
            isAbstract
        );
      }
      case ComplexStructureType.KIND -> {
        return new ComplexStructureType(
            serializationType,
            builtFields,
            description,
            parent,
            isAbstract
        );
      }
      default -> throw new RuntimeException("FHIR kind '" + kind + "' is not recognizable.");
    }
  }

  public ComplexStructureTypeBuilder setParent(String parent) {
    this.parent = parent;
    return this;
  }

  public ComplexStructureTypeBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public ComplexStructureTypeBuilder setIsAbstract(Boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  public ComplexStructureTypeBuilder setKind(String kind) {
    this.kind = kind;
    return this;
  }

  public String getContainedInNonAnonymousType() {
    return containedInNonAnonymousType;
  }

  public ComplexStructureTypeBuilder setContainedInNonAnonymousType(
      String containedInNonAnonymousType) {
    this.containedInNonAnonymousType = containedInNonAnonymousType;
    return this;
  }

  @Override
  protected AbstractStructureTypeBuilder specificMergeOverwrite(
      AbstractStructureTypeBuilder other) {
    if (!(other instanceof ComplexStructureTypeBuilder otherComplex)) {
      throw new RuntimeException("Merging incompatible builders!");
    }
    if (otherComplex.getContainedInNonAnonymousType() != null) {
      this.containedInNonAnonymousType = otherComplex.getContainedInNonAnonymousType();
    }
    this.fields.putAll(otherComplex.fields);
    return this;
  }
}