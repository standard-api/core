package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;

public class ListGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private GraphDescription graphDescription;
  private SpecificObjectGraphMappingBuilder child;

  public ObjectGraphMappingBuilder addObjectChildDefinition() {
    var builder = new ObjectGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public LeafGraphMappingBuilder addLeafChildDefinition() {
    var builder = new LeafGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public ListGraphMappingBuilder addListChildDefinition() {
    var builder = new ListGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public MapGraphMappingBuilder addMapChildDefinition() {
    var builder = new MapGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public InterfaceGraphMappingBuilder addInterfaceChildDefinition() {
    var builder = new InterfaceGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public ReferenceGraphMappingBuilder addReferenceChildDefinition() {
    var builder = new ReferenceGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public ObjectGraphMapping build() {
    if (this.graphDescription == null) {
      this.graphDescription = new NullGraphDescription();
    }
    return new ListObjectGraphMapping(
        this.graphDescription,
        this.child.build()
    );
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new ListGraphMappingBuilder();
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ListObjectGraphMapping;
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public ListGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
    return this;
  }

  public ListGraphMappingBuilder setGraphDescription(GraphDescriptionBuilder builder) {
    this.graphDescription = builder.build();
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
    var listMapping = (ListObjectGraphMapping) objectGraphMapping;
    var builder = new ListGraphMappingBuilder();
    builder.setGraphDescription(listMapping.getGraphDescription());
    builder.setChildDefinition(
        genericBuilder.copyGraphMappingAsBuilder(listMapping.getChildObjectGraphMapping()));
    return builder;
  }

  public SpecificObjectGraphMappingBuilder setChildDefinition(
      SpecificObjectGraphMappingBuilder builder) {
    this.child = builder;
    return builder;
  }
}
