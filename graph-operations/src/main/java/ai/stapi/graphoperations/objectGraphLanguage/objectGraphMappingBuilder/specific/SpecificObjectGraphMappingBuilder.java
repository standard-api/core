package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;

public interface SpecificObjectGraphMappingBuilder {

  ObjectGraphMapping build();

  SpecificObjectGraphMappingBuilder getEmptyCopy();

  boolean supports(ObjectGraphMapping objectGraphMapping);

  SpecificObjectGraphMappingBuilder copyObjectGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping,
      GenericOGMBuilder genericBuilder
  );

  GraphDescription getGraphDescription();

  void setNewGraphDescription(GraphDescription graphDescription);
}
