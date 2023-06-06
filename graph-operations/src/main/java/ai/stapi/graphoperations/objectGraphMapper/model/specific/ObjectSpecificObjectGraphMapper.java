package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import java.util.Optional;

public class ObjectSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  public ObjectSpecificObjectGraphMapper(GenericObjectGraphMapper genericGraphMapper) {
    super(genericGraphMapper);
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy resolvingStrategy
  ) {
    var objectMapping = (ObjectObjectGraphMapping) objectGraphMapping;
    var dtoObjectFields = this.convertObjectToFieldMap(fieldEntry.getValue(), fieldEntry.getKey());
    if (this.isObjectMissingAnyOfDefinedFields(dtoObjectFields, objectMapping.getFields(),
        resolvingStrategy)) {
      throw SpecificObjectGraphMapperException.becauseProvidedObjectDoesNotContainFieldWithGivenName(
          fieldEntry.getValue(),
          objectMapping.getFields().keySet().stream()
              .filter(key -> !dtoObjectFields.containsKey(key))
              .findAny()
              .orElseThrow(),
          objectMapping.getFields()
      );
    }
    var identifyingField =
        this.getEntityIdentifyingField(objectMapping, dtoObjectFields, resolvingStrategy);
    if (identifyingField.isPresent()) {
      if (builder.getDescriptionBuilder() != null) {
        builder = builder.createNewBranch();
      }
      this.resolveIdentifyingField(
          identifyingField.get().getValue().getFieldObjectGraphMapping(),
          dtoObjectFields.get(identifyingField.get().getKey()),
          identifyingField.get().getKey(),
          builder,
          resolvingStrategy
      );
    }
    var finishedObjectBuilderBranch = this.addGraphDescriptionCompositeToBuilder(
        objectMapping.getGraphDescription(),
        builder
    );
    var lastFinishedGraphElementBranch =
        finishedObjectBuilderBranch.getLastBranchWithGraphElementBuilder();
    objectMapping.getFields().forEach(
        (key, field) -> {
          if (resolvingStrategy.equals(MissingFieldResolvingStrategy.LENIENT) &&
              (!dtoObjectFields.containsKey(key) || dtoObjectFields.get(key) == null)
          ) {
            return;
          }
          if (!(field.getRelation() instanceof EntityIdentifier)) {
            var newField = this.copyFieldAndPushDeclarationToChild(field);
            this.genericGraphMapper.resolveInternally(
                newField.getFieldObjectGraphMapping(),
                Map.entry(key, dtoObjectFields.get(key)),
                lastFinishedGraphElementBranch,
                resolvingStrategy
            );
          }
        }
    );
    return builder;
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ObjectObjectGraphMapping;
  }

  private void resolveIdentifyingField(
      ObjectGraphMapping graphMapping,
      Object object,
      String parentFieldName,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    if (graphMapping instanceof ObjectObjectGraphMapping objectGraphMapping) {
      this.resolveObjectAsEntityIdentifier(
          objectGraphMapping,
          object,
          parentFieldName,
          builder,
          missingFieldResolvingStrategy
      );
    } else {
      this.genericGraphMapper.resolveInternally(
          graphMapping,
          Map.entry(parentFieldName, object),
          builder,
          missingFieldResolvingStrategy
      );
    }
  }

  private void resolveObjectAsEntityIdentifier(
      ObjectObjectGraphMapping objectGraphMapping,
      Object object,
      String parentFieldName,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var dtoObjectMap = this.convertObjectToFieldMap(object, parentFieldName);
    var identifyingField = this.getEntityIdentifyingField(objectGraphMapping, dtoObjectMap,
        missingFieldResolvingStrategy);
    if (identifyingField.isEmpty()) {
      throw SpecificObjectGraphMapperException.becauseThereIsNoIdentifyingFieldOnObject(
          parentFieldName);
    }
    builder = this.addGraphDescriptionCompositeToBuilder(objectGraphMapping.getGraphDescription(),
        builder);
    var objectIdentifyingField = dtoObjectMap.get(identifyingField.get().getKey());
    this.resolveIdentifyingField(
        identifyingField.get().getValue().getFieldObjectGraphMapping(),
        objectIdentifyingField,
        identifyingField.get().getKey(),
        builder,
        missingFieldResolvingStrategy
    );
  }

  private Optional<Map.Entry<String, ObjectFieldDefinition>> getEntityIdentifyingField(
      ObjectObjectGraphMapping objectGraphMapping,
      Map<String, Object> dtoObjectMap,
      MissingFieldResolvingStrategy resolvingStrategy
  ) {
    var fieldEntries = objectGraphMapping.getFields().entrySet().stream()
        .filter(entry -> entry.getValue().getRelation() instanceof EntityIdentifier)
        .toList();
    if (fieldEntries.size() > 1) {
      throw SpecificObjectGraphMapperException.becauseThereAreMultipleIdentifyingFieldsOnObject();
    }
    if (fieldEntries.isEmpty()) {
      return Optional.empty();
    }
    if (resolvingStrategy.equals(MissingFieldResolvingStrategy.LENIENT)
        && (!dtoObjectMap.containsKey(fieldEntries.get(0).getKey()))) {
      return Optional.empty();

    }
    return Optional.of(fieldEntries.get(0));
  }

  private Map<String, Object> convertObjectToFieldMap(Object object, String fieldName) {
    try {
      return this.jsonObjectMapper.convertValue(
          object,
          new TypeReference<>() {
          }
      );
    } catch (IllegalArgumentException error) {
      throw SpecificObjectGraphMapperException.becauseObjectCouldNotBeConverted(this, fieldName,
          object, error);
    }
  }

  private boolean isObjectMissingAnyOfDefinedFields(
      Map<String, Object> objectFieldMap,
      Map<String, ObjectFieldDefinition> mappingFieldDefinition,
      MissingFieldResolvingStrategy strategy
  ) {
    if (strategy.equals(MissingFieldResolvingStrategy.LENIENT)) {
      return false;
    }
    return mappingFieldDefinition.keySet().stream()
        .anyMatch(key -> !objectFieldMap.containsKey(key));
  }

  private ObjectFieldDefinition copyFieldAndPushDeclarationToChild(
      ObjectFieldDefinition fieldDefinition) {
    if (!(fieldDefinition.getRelation() instanceof GraphDescription)) {
      return fieldDefinition;
    }
    if (fieldDefinition.getRelation() instanceof NullGraphDescription) {
      return fieldDefinition;
    }
    var builder = new GraphDescriptionBuilder();
    var childDescription = fieldDefinition.getFieldObjectGraphMapping().getGraphDescription();
    var combinedDescription = builder.copyWithNewChildren(
        (GraphDescription) fieldDefinition.getRelation(),
        childDescription
    );
    var genericBuilder = new GenericOGMBuilder();
    var newChild = genericBuilder.copyGraphMappingAsBuilder(
        fieldDefinition.getFieldObjectGraphMapping()
    );
    newChild.setNewGraphDescription(combinedDescription);
    return new ObjectFieldDefinition(new NullGraphDescription(), newChild.build());
  }
}
