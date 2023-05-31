package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.BoxedPrimitiveStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchemaMapper.UnresolvableSerializationType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StructureSchemaBuilder {

  private LinkedHashMap<String, AbstractStructureTypeBuilder> definitions;

  public StructureSchemaBuilder(StructureSchema context) {
    this(context.getStructureTypes());
  }

  public StructureSchemaBuilder(Map<String, AbstractStructureType> definitions) {
    this.definitions = new LinkedHashMap<>();
    definitions.forEach((key, definition) -> {
      if (definition instanceof BoxedPrimitiveStructureType) {
        this.addStructureTypeBuilder(new BoxedStructureTypeBuilder().copyToBuilder(definition));
      } else if (definition instanceof ComplexStructureType) {
        this.addStructureTypeBuilder(new ComplexStructureTypeBuilder().copyToBuilder(definition));
      } else if (definition instanceof PrimitiveStructureType) {
        this.addStructureTypeBuilder(new PrimitiveStructureTypeBuilder().copyToBuilder(definition));
      } else {
        throw new RuntimeException(
            "Unrecognizable structure type. Type: '" + definition.getClass().getSimpleName()
                + "'.");
      }
    });
  }

  public StructureSchemaBuilder() {
    this.definitions = new LinkedHashMap<>();
  }

  public void addStructureTypeBuilder(AbstractStructureTypeBuilder structureTypeBuilder) {
    if (this.definitions.containsKey(structureTypeBuilder.getSerializationType())) {
      this.definitions.get(structureTypeBuilder.getSerializationType())
          .mergeOverwrite(structureTypeBuilder);
      if (structureTypeBuilder.isPrimitiveType()) {
        var builder = this.createBoxedEquivalent(structureTypeBuilder);
        this.definitions.get(builder.getSerializationType()).mergeOverwrite(builder);
      }
    } else {
      this.definitions.put(
          structureTypeBuilder.getSerializationType(),
          structureTypeBuilder
      );
      if (structureTypeBuilder.isPrimitiveType()) {
        var builder = this.createBoxedEquivalent(structureTypeBuilder);
        definitions.put(
            builder.getSerializationType(),
            builder
        );
      }
    }
  }

  private BoxedStructureTypeBuilder createBoxedEquivalent(
      AbstractStructureTypeBuilder structureTypeBuilder) {
    return new BoxedStructureTypeBuilder()
        .setSerializationType(structureTypeBuilder.getOriginalSerializationType())
        .setDescription(structureTypeBuilder.getDescription())
        .setIsAbstract(structureTypeBuilder.isAbstract())
        .setParent(structureTypeBuilder.getParent());
  }

  public void addStructureTypeBuilders(List<AbstractStructureTypeBuilder> structureTypeBuilders) {
    structureTypeBuilders.forEach(this::addStructureTypeBuilder);
  }

  public boolean containsType(String serializationType) {
    return this.definitions.containsKey(serializationType);
  }

  public StructureSchema build() {
    var unresolvableFields = this.getUnresolvableTypesWithFailingDependencyList().stream()
        .map(UnresolvableSerializationType::serializationType)
        .collect(Collectors.toList());

    var allUnresolvableFields = this.definitions.values().stream().filter(
        structureTypeBuilder ->
            unresolvableFields.contains(
                structureTypeBuilder.getSerializationType()
            ) || unresolvableFields.contains(
                structureTypeBuilder.getOriginalSerializationType()
            )
    ).map(AbstractStructureTypeBuilder::getSerializationType).toList();

    var builtStructureTypes = new ArrayList<AbstractStructureType>();
    var successfulDefinition = this.definitions.entrySet()
        .stream().filter(
            abstractStructureTypeBuilderEntrySet ->
                !allUnresolvableFields.contains(
                    abstractStructureTypeBuilderEntrySet.getValue().getSerializationType()
                )
        ).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            )
        );

    successfulDefinition.values().stream().forEach((abstractStructureTypeBuilder) -> {
      builtStructureTypes.add(abstractStructureTypeBuilder.build(this));
    });
    return StructureSchema.createChecked(builtStructureTypes);
  }

  public AbstractStructureTypeBuilder getStructureTypeBuilder(String type) {
    return definitions.get(type);
  }

  public List<UnresolvableSerializationType> getUnresolvableTypesWithFailingDependencyList() {
    var unresolvableTypes = this.definitions.values().stream()
        .map(
            abstractStructureTypeBuilder -> abstractStructureTypeBuilder.getDirectlyUnresolvableTypesWithFailingDependencyList(
                this))
        .flatMap(List::stream).distinct()
        .collect(Collectors.toMap(
            UnresolvableSerializationType::serializationType,
            unresolvableSerializationType -> unresolvableSerializationType,
            (x, y) -> y,
            HashMap::new
        ));

    var lastCount = 0;
    do {
      lastCount = unresolvableTypes.size();
      this.definitions.values().stream()
          .filter(abstractStructureTypeBuilder -> !unresolvableTypes.containsKey(
              abstractStructureTypeBuilder.getSerializationType()))
          .forEach(
              abstractStructureTypeBuilder -> {
                if (abstractStructureTypeBuilder instanceof ComplexStructureTypeBuilder complexStructureTypeBuilder) {
                  var failed = complexStructureTypeBuilder.getAllFieldTypes().stream()
                      .map(FieldType::getType)
                      .filter(unresolvableTypes::containsKey).collect(Collectors.toList());
                  if (
                      !abstractStructureTypeBuilder.getParent().isBlank()
                          && unresolvableTypes.containsKey(abstractStructureTypeBuilder.getParent()
                      )
                  ) {
                    failed.add(abstractStructureTypeBuilder.getParent());
                  }

                  if (failed.size() > 0) {
                    var missing = failed.stream()
                        .map(s -> {
                          var unresovableType = unresolvableTypes.get(s);
                          return unresovableType.missingDependencies();
                        })
                        .flatMap(List::stream).distinct().collect(Collectors.toList());
                    unresolvableTypes.put(
                        abstractStructureTypeBuilder.getSerializationType(),
                        new UnresolvableSerializationType(
                            abstractStructureTypeBuilder.getSerializationType(),
                            abstractStructureTypeBuilder.getOriginalSerializationType(),
                            missing
                        )
                    );
                  }
                } else if (abstractStructureTypeBuilder instanceof PrimitiveStructureTypeBuilder primitiveBuilder) {
                  unresolvableTypes.values().stream()
                      .filter(unresolvableSerializationType ->
                          unresolvableSerializationType.originalSerializationType()
                              .equals(primitiveBuilder.getSerializationType())
                      ).findAny()
                      .ifPresent(unresolvableSerializationType ->
                          unresolvableTypes.put(
                              primitiveBuilder.getSerializationType(),
                              new UnresolvableSerializationType(
                                  abstractStructureTypeBuilder.getSerializationType(),
                                  abstractStructureTypeBuilder.getOriginalSerializationType(),
                                  unresolvableSerializationType.missingDependencies()
                              )
                          ));
                } else if (abstractStructureTypeBuilder instanceof BoxedStructureTypeBuilder boxedStructureTypeBuilder) {
                  var primitiveDependency =
                      boxedStructureTypeBuilder.getOriginalSerializationType();
                  var parentDependency = boxedStructureTypeBuilder.getParent();
                  var missingDependencies = unresolvableTypes.values().stream()
                      .filter(unresolvable ->
                          unresolvable.serializationType().equals(primitiveDependency)
                              || unresolvable.serializationType().equals(parentDependency)
                      ).map(UnresolvableSerializationType::missingDependencies)
                      .flatMap(List::stream)
                      .collect(Collectors.toList());

                  if (missingDependencies.size() > 0) {
                    unresolvableTypes.put(
                        abstractStructureTypeBuilder.getOriginalSerializationType(),
                        new UnresolvableSerializationType(
                            abstractStructureTypeBuilder.getOriginalSerializationType(),
                            abstractStructureTypeBuilder.getOriginalSerializationType(),
                            missingDependencies
                        )
                    );
                    unresolvableTypes.put(
                        abstractStructureTypeBuilder.getSerializationType(),
                        new UnresolvableSerializationType(
                            abstractStructureTypeBuilder.getSerializationType(),
                            abstractStructureTypeBuilder.getOriginalSerializationType(),
                            missingDependencies
                        )
                    );
                  }
                }
              }
          );
    } while (lastCount != unresolvableTypes.size());

    return unresolvableTypes.values().stream().toList();
  }

  public List<String> getMissingFieldsOnType(String type) {
    var abstractStructureTypeBuilder = this.definitions.get(type);
    if (abstractStructureTypeBuilder == null) {
      return List.of();
    }
    return abstractStructureTypeBuilder.findMissingTypesForFields(this);
  }
}
