package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;
import java.util.UUID;

public class InterfaceGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private String uuid;
  private GraphDescription graphDescription;

  public InterfaceGraphMappingBuilder setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public InterfaceGraphMappingBuilder setUuid(UUID uuid) {
    this.uuid = uuid.toString();
    return this;
  }

  public ObjectGraphMapping build() {
    var graphDescription = this.graphDescription;
    if (this.graphDescription == null) {
      graphDescription = new NullGraphDescription();
    }
    return new InterfaceObjectGraphMapping(this.uuid, graphDescription);
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new InterfaceGraphMappingBuilder();
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof InterfaceObjectGraphMapping;
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public InterfaceGraphMappingBuilder setGraphDescription(
      GraphDescriptionBuilder graphDescription) {
    this.graphDescription = graphDescription.build();
    return this;
  }

  public InterfaceGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
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
    var interfaceMapping = (InterfaceObjectGraphMapping) objectGraphMapping;
    var copy = new InterfaceGraphMappingBuilder();
    copy.setUuid(interfaceMapping.getInterfaceUuid());
    copy.setGraphDescription(objectGraphMapping.getGraphDescription());
    return copy;
  }
}
