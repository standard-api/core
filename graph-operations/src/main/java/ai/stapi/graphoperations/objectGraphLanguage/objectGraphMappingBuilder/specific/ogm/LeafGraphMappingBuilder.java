package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;

public class LeafGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private GraphDescription graphDescription;

  public ObjectGraphMapping build() {
    if (this.graphDescription == null) {
      this.graphDescription = new NullGraphDescription();
    }
    return new LeafObjectGraphMapping(
        this.graphDescription
    );
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new LeafGraphMappingBuilder();
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public LeafGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
    return this;
  }

  public LeafGraphMappingBuilder setGraphDescription(GraphDescriptionBuilder builder) {
    this.graphDescription = builder.build();
    return this;
  }

  @Override
  public void setNewGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof LeafObjectGraphMapping;
  }

  @Override
  public SpecificObjectGraphMappingBuilder copyObjectGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping,
      GenericOGMBuilder genericBuilder
  ) {
    var leaf = (LeafObjectGraphMapping) objectGraphMapping;
    var leafCopy = new LeafGraphMappingBuilder();
    leafCopy.setGraphDescription(leaf.getGraphDescription());
    return leafCopy;
  }
}
