package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ReferenceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;

public class ReferenceGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private String referencedSerializationType;
  private GraphDescription graphDescription;

  public ObjectGraphMapping build() {
    var graphDescription = this.graphDescription;
    if (this.graphDescription == null) {
      graphDescription = new NullGraphDescription();
    }
    return new ReferenceObjectGraphMapping(graphDescription, referencedSerializationType);
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new ReferenceGraphMappingBuilder();
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ReferenceObjectGraphMapping;
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public ReferenceGraphMappingBuilder setGraphDescription(
      GraphDescriptionBuilder graphDescription) {
    this.graphDescription = graphDescription.build();
    return this;
  }

  public ReferenceGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
    return this;
  }

  @Override
  public void setNewGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
  }

  @Override
  public SpecificObjectGraphMappingBuilder copyObjectGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping,
      GenericOGMBuilder genericBuilder
  ) {
    var referenceMapping = (ReferenceObjectGraphMapping) objectGraphMapping;
    var copy = new ReferenceGraphMappingBuilder();
    copy.setReferencedSerializationType(referenceMapping.getReferencedSerializationType());
    copy.setGraphDescription(referenceMapping.getGraphDescription());
    return copy;
  }

  public String getReferencedSerializationType() {
    return referencedSerializationType;
  }

  public ReferenceGraphMappingBuilder setReferencedSerializationType(
      String referencedSerializationType) {
    this.referencedSerializationType = referencedSerializationType;
    return this;
  }
}
