package ai.stapi.graphoperations.ogmProviders.specific;

import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;

public interface SpecificGraphMappingProvider {
  
  default ObjectGraphMapping provideGraphMapping(String serializationType) {
    return this.provideGraphMapping(serializationType, "");
  }

  ObjectGraphMapping provideGraphMapping(
      String serializationType,
      String fieldName
  );
  
  boolean supports(String serializationType);
}
