package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;

public class MapGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private GraphDescription graphDescription;
  private SpecificObjectGraphMappingBuilder keyObjectGraphMapping;
  private SpecificObjectGraphMappingBuilder valueObjectGraphMapping;

  public ObjectGraphMappingBuilder addObjectObjectValueMapping() {
    var builder = new ObjectGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public ReferenceGraphMappingBuilder addReferenceValueMapping() {
    var builder = new ReferenceGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public ObjectGraphMappingBuilder addObjectObjectKeyMapping() {
    var builder = new ObjectGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public InterfaceGraphMappingBuilder addInterfaceObjectValueMapping() {
    var builder = new InterfaceGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public InterfaceGraphMappingBuilder addInterfaceObjectKeyMapping() {
    var builder = new InterfaceGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public ReferenceGraphMappingBuilder addReferenceKeyMapping() {
    var builder = new ReferenceGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public LeafGraphMappingBuilder addLeafValueMapping() {
    var builder = new LeafGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public LeafGraphMappingBuilder addLeafKeyMapping() {
    var builder = new LeafGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public ListGraphMappingBuilder addListValueMapping() {
    var builder = new ListGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public ListGraphMappingBuilder addListKeyMapping() {
    var builder = new ListGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public MapGraphMappingBuilder addMapValueMapping() {
    var builder = new MapGraphMappingBuilder();
    this.valueObjectGraphMapping = builder;
    return builder;
  }

  public MapGraphMappingBuilder addMapKeyMapping() {
    var builder = new MapGraphMappingBuilder();
    this.keyObjectGraphMapping = builder;
    return builder;
  }

  public ObjectGraphMapping build() {
    if (this.graphDescription == null) {
      this.graphDescription = new NullGraphDescription();
    }
    return new MapObjectGraphMapping(
        this.graphDescription,
        this.keyObjectGraphMapping.build(),
        this.valueObjectGraphMapping.build()
    );
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new MapGraphMappingBuilder();
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof MapObjectGraphMapping;
  }

  @Override
  public SpecificObjectGraphMappingBuilder copyObjectGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping,
      GenericOGMBuilder genericBuilder
  ) {
    var mapMapping = (MapObjectGraphMapping) objectGraphMapping;
    var copy = new MapGraphMappingBuilder();
    copy.setGraphDescription(mapMapping.getGraphDescription());
    copy.setObjectKeyMapping(
        genericBuilder.copyGraphMappingAsBuilder(mapMapping.getKeyObjectGraphMapping()));
    copy.setObjectValueMapping(
        genericBuilder.copyGraphMappingAsBuilder(mapMapping.getValueObjectGraphMapping()));
    return copy;
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public MapGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
    return this;
  }

  public MapGraphMappingBuilder setGraphDescription(GraphDescriptionBuilder builder) {
    this.graphDescription = builder.build();
    return this;
  }

  @Override
  public void setNewGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
  }

  private SpecificObjectGraphMappingBuilder setObjectValueMapping(
      SpecificObjectGraphMappingBuilder objectGraphMappingBuilder) {
    this.valueObjectGraphMapping = objectGraphMappingBuilder;
    return objectGraphMappingBuilder;
  }

  private SpecificObjectGraphMappingBuilder setObjectKeyMapping(
      SpecificObjectGraphMappingBuilder objectGraphMappingBuilder) {
    this.keyObjectGraphMapping = objectGraphMappingBuilder;
    return objectGraphMappingBuilder;
  }
}
