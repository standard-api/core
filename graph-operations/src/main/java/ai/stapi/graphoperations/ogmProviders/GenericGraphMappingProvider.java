package ai.stapi.graphoperations.ogmProviders;

import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.exception.GraphMappingProviderException;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.serialization.SerializableObject;
import java.util.List;
import java.util.Map;

public class GenericGraphMappingProvider {

  private final List<SpecificGraphMappingProvider> mappingProviders;

  public GenericGraphMappingProvider(
      List<SpecificGraphMappingProvider> mappingProviders
  ) {
    this.mappingProviders = mappingProviders;
  }

  public ObjectGraphMapping provideGraphMapping(Map<String, Object> serializableObjectMap) {
    if (!serializableObjectMap.containsKey(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE)) {
      throw GraphMappingProviderException.becauseProvidedObjectMapDoesNotContainTypeField();
    }
    var serializationType = (String) serializableObjectMap.get(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE);
    return this.provideGraphMapping(serializationType);
  }

  public ObjectGraphMapping provideGraphMapping(Map<String, Object> serializableObjectMap,
      String fieldName) {
    if (!serializableObjectMap.containsKey(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE)) {
      throw GraphMappingProviderException.becauseProvidedObjectMapDoesNotContainTypeField();
    }
    var serializationType = (String) serializableObjectMap.get(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE);
    return this.provideGraphMapping(serializationType, fieldName);
  }

  public ObjectGraphMapping provideGraphMapping(SerializableObject object) {
    return this.provideGraphMapping(object.getSerializationType());
  }

  public ObjectGraphMapping provideGraphMapping(SerializableObject object, String fieldName) {
    return this.provideGraphMapping(object.getSerializationType(), fieldName);
  }

  public ObjectGraphMapping provideGraphMapping(String serializationType, String fieldName) {
    var provider = this.getSupportingProvider(serializationType);
    return provider.provideGraphMapping(serializationType, fieldName);
  }

  public ObjectGraphMapping provideGraphMapping(String serializationType) {
    var provider = this.getSupportingProvider(serializationType);
    return provider.provideGraphMapping(serializationType);
  }

  public boolean supports(String serializationType) {
    return this.mappingProviders.stream()
        .anyMatch(provider -> provider.supports(serializationType));
  }

  private SpecificGraphMappingProvider getSupportingProvider(String serializationType) {
    var listOfSupportingProviders = this.mappingProviders.stream()
        .filter(specificObjectGraphMapper -> specificObjectGraphMapper.supports(serializationType))
        .toList();
    if (listOfSupportingProviders.isEmpty()) {
      throw GraphMappingProviderException.becauseThereIsNoSupportingSpecificGraphMappingProvider(
          serializationType);
    }
    var staticProviders = listOfSupportingProviders.stream()
        .filter(provider -> (!(provider instanceof DynamicOgmProvider)))
        .toList();
    if (staticProviders.size() > 1) {
      throw GraphMappingProviderException.becauseThereAreMoreThanOneSpecificGraphMappingProviders(
          serializationType,
          listOfSupportingProviders
      );
    }
    if (staticProviders.size() == 1) {
      return staticProviders.get(0);
    }
    var dynamicProvider = listOfSupportingProviders.stream()
        .filter(DynamicOgmProvider.class::isInstance)
        .findAny()
        .orElse(null);
    if (dynamicProvider != null) {
      return dynamicProvider;
    }
    throw GraphMappingProviderException.becauseThereIsNoSupportingSpecificGraphMappingProvider(
        serializationType
    );
  }

}
