package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;

public class ObjectFieldDefinitionBuilder {

  private Declaration declaration;
  private SpecificObjectGraphMappingBuilder child;

  public ObjectFieldDefinitionBuilder setRelation(Declaration declaration) {
    this.declaration = declaration;
    return this;
  }

  public ObjectFieldDefinitionBuilder setRelation(GraphDescriptionBuilder builder) {
    this.declaration = builder.build();
    return this;
  }

  public ObjectGraphMappingBuilder addObjectAsObjectFieldMapping() {
    var builder = new ObjectGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public LeafGraphMappingBuilder addLeafAsObjectFieldMapping() {
    var builder = new LeafGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public ListGraphMappingBuilder addListAsObjectFieldMapping() {
    var builder = new ListGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public MapGraphMappingBuilder addMapAsObjectFieldMapping() {
    var builder = new MapGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public InterfaceGraphMappingBuilder addInterfaceAsObjectFieldMapping() {
    var builder = new InterfaceGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public ReferenceGraphMappingBuilder addReferenceOgm() {
    var builder = new ReferenceGraphMappingBuilder();
    this.child = builder;
    return builder;
  }

  public <T extends SpecificObjectGraphMappingBuilder> T setOgmBuilder(T ogmBuilder) {
    this.child = ogmBuilder;
    return ogmBuilder;
  }

  public SpecificObjectGraphMappingBuilder setChild(
      SpecificObjectGraphMappingBuilder childBuilder
  ) {
    this.child = childBuilder;
    return childBuilder;
  }


  public ObjectFieldDefinition build() {
    if (this.declaration == null) {
      this.declaration = new NullGraphDescription();
    }
    return new ObjectFieldDefinition(
        this.declaration,
        this.child.build()
    );
  }

}
