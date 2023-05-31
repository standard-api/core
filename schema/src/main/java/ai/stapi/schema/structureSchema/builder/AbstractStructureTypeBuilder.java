package ai.stapi.schema.structureSchema.builder;

import static ai.stapi.schema.structureSchema.AbstractStructureType.PRIMITIVE_TYPE;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.FieldTypeGroup;
import ai.stapi.schema.structureSchemaMapper.UnresolvableSerializationType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStructureTypeBuilder {

  protected boolean isAbstract = false;
  protected String description = "";
  protected String serializationType = "";
  protected String parent = "";
  protected String kind = "";
  public AbstractStructureTypeBuilder() {
  }

  public abstract AbstractStructureType build(StructureSchemaBuilder structureSchemaBuilder);

  public abstract AbstractStructureTypeBuilder copyToBuilder(
      AbstractStructureType abstractStructureType);

  public boolean isAbstract() {
    return this.isAbstract;
  }

  public boolean isPrimitiveType() {
    return this.kind.equals(PRIMITIVE_TYPE);
  }

  public String getDescription() {
    return this.description;
  }

  public String getSerializationType() {
    return this.serializationType;
  }

  public String getOriginalSerializationType() {
    return this.serializationType;
  }

  public String getParent() {
    return parent;
  }

  public AbstractStructureTypeBuilder setParent(String parent) {
    this.parent = parent;
    return this;
  }

  public String getKind() {
    return kind;
  }

//    public abstract boolean isResolvableIn(StructureSchemaBuilder structureSchemaBuilder);

  public List<UnresolvableSerializationType> getDirectlyUnresolvableTypesWithFailingDependencyList(
      StructureSchemaBuilder structureSchemaBuilder) {
    var missingDependencies =
        new ArrayList<>(this.findMissingTypesForFields(structureSchemaBuilder));

    if (missingDependencies.size() == 0) {
      return List.of();
    }
    return List.of(new UnresolvableSerializationType(
            this.getSerializationType(),
            this.getOriginalSerializationType(),
            missingDependencies
        )
    );
  }

  @NotNull
  protected ArrayList<String> findMissingDependenciesForParent(
      StructureSchemaBuilder structureSchemaBuilder) {
    var missingFieldsOnParent = new ArrayList<String>();
    if (this.getParent() != null && !this.getParent().isBlank()) {
      var parentBuilder = structureSchemaBuilder.getStructureTypeBuilder(this.getParent());
      if (parentBuilder == null) {
        String originalParentName =
            structureSchemaBuilder.getStructureTypeBuilder(this.getSerializationType()).getParent();
        if (originalParentName == null || originalParentName.isBlank()) {
          originalParentName = this.getParent();
        }
        missingFieldsOnParent.add(
            originalParentName
        );
      } else {
        var missingOnParents = parentBuilder.findMissingTypesForFields(structureSchemaBuilder);
        missingFieldsOnParent.addAll(missingOnParents);
      }
    }
    return missingFieldsOnParent;
  }

  public List<String> findMissingTypesForFields(StructureSchemaBuilder structureSchemaBuilder) {
    var allMissingFields = new ArrayList<String>();
    var allFieldTypes = this.getAllFieldTypes();

    var missingFieldsOnCurrent = allFieldTypes.stream()
        .filter(fieldType -> !structureSchemaBuilder.containsType(fieldType.getType()))
        .map(fieldType1 -> fieldType1.getTypeGroup().equals(FieldTypeGroup.BOXED)
            ? fieldType1.getOriginalType()
            : fieldType1.getType()
        ).toList();

    var allAnonymousTypesOnFields = this.getAllFields(structureSchemaBuilder).values().stream()
        .map(FieldDefinitionBuilder::getTypes)
        .flatMap(List::stream).distinct()
        .filter(type -> structureSchemaBuilder.getStructureTypeBuilder(type.getType()) != null
            ? structureSchemaBuilder.getStructureTypeBuilder(type.getType())
            .getContainedInNonAnonymousType() != null
            : false
        ).toList();

    var missingFieldsOnAllAnonymousTypesOnFields = allAnonymousTypesOnFields.stream()
        .filter(fieldType -> !fieldType.isContentReferenced())
        .map(fieldType -> structureSchemaBuilder.getMissingFieldsOnType(fieldType.getType()))
        .flatMap(List::stream).distinct()
        .collect(Collectors.toList());

    ArrayList<String> missingFieldsOnParent =
        findMissingDependenciesForParent(structureSchemaBuilder);

    allMissingFields.addAll(missingFieldsOnCurrent);
    allMissingFields.addAll(missingFieldsOnAllAnonymousTypesOnFields);
    allMissingFields.addAll(missingFieldsOnParent);

    return allMissingFields;
  }

  public abstract List<FieldType> getAllFieldTypes();

  public abstract String getContainedInNonAnonymousType();

  public abstract AbstractStructureTypeBuilder setContainedInNonAnonymousType(
      String containedInNonAnonymousType);

  public AbstractStructureTypeBuilder mergeOverwrite(AbstractStructureTypeBuilder other) {
    if (!other.getClass().equals(this.getClass())) {
      throw new RuntimeException(
          String.format(
              """
                  Merging incompatible builders!
                  This builder: %s
                  Other builder: %s
                  """,
              this.getClass().getSimpleName(),
              other.getClass().getSimpleName()
          )
      );
    }
    this.specificMergeOverwrite(other);
    if (other.getDescription() != null && !other.getDescription().isBlank()) {
      this.description = other.getDescription();
    }
    if (other.getParent() != null && !other.getParent().isBlank()) {
      this.parent = other.getParent();
    }
    if (other.getKind() != null && !other.getParent().isBlank()) {
      this.kind = other.getKind();
    }
    if (other.getSerializationType() != null && !other.getSerializationType().isBlank()) {
      this.serializationType = other.getSerializationType();
    }
    this.isAbstract = other.isAbstract();

    return this;
  }

  protected abstract AbstractStructureTypeBuilder specificMergeOverwrite(
      AbstractStructureTypeBuilder other);


  public abstract Map<String, FieldDefinitionBuilder> getAllFields(
      StructureSchemaBuilder structureSchemaBuilder);
}
