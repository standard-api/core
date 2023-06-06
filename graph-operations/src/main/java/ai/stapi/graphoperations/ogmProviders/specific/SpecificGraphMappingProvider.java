package ai.stapi.graphoperations.ogmProviders.specific;

import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;

public interface SpecificGraphMappingProvider {

  ObjectGraphMapping provideGraphMapping(
      String serializationType
  );

  boolean supports(String serializationType);
}
