package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;
import java.util.HashMap;
import java.util.Map;

public class ObjectGraphMappingBuilder implements SpecificObjectGraphMappingBuilder {

  private final Map<String, ObjectFieldDefinitionBuilder> fields = new HashMap<>();
  private GraphDescription graphDescription;

  public ObjectFieldDefinitionBuilder addField(String fieldName) {
    var newField = new ObjectFieldDefinitionBuilder();
    this.fields.put(fieldName, newField);
    return newField;
  }

  public ObjectFieldDefinitionBuilder addField(String fieldName,
      ObjectFieldDefinitionBuilder fieldBuilder) {
    this.fields.put(fieldName, fieldBuilder);
    return fieldBuilder;
  }

  @Override
  public void setNewGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
  }

  public ObjectGraphMapping build() {
    if (this.graphDescription == null) {
      this.graphDescription = new NullGraphDescription();
    }
    var resolvedFields = new HashMap<String, ObjectFieldDefinition>();
    this.fields.forEach(
        (key, value) -> resolvedFields.put(key, value.build())
    );
    return new ObjectObjectGraphMapping(
        this.graphDescription,
        resolvedFields
    );
  }

  @Override
  public SpecificObjectGraphMappingBuilder getEmptyCopy() {
    return new ObjectGraphMappingBuilder();
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ObjectObjectGraphMapping;
  }

  @Override
  public SpecificObjectGraphMappingBuilder copyObjectGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping, GenericOGMBuilder genericBuilder) {
    var objectMapping = (ObjectObjectGraphMapping) objectGraphMapping;
    var builder = new ObjectGraphMappingBuilder();
    builder.setGraphDescription(objectGraphMapping.getGraphDescription());
    objectMapping.getFields().forEach(
        (key, value) -> {
          var copiedField = new ObjectFieldDefinitionBuilder();
          copiedField.setRelation(value.getRelation());
          copiedField.setChild(
              genericBuilder.copyGraphMappingAsBuilder(value.getFieldObjectGraphMapping()));
          builder.addField(key, copiedField);
        }
    );
    return builder;
  }

  @Override
  public GraphDescription getGraphDescription() {
    return this.graphDescription;
  }

  public ObjectGraphMappingBuilder setGraphDescription(GraphDescription graphDescription) {
    this.graphDescription = graphDescription;
    return this;
  }

  public ObjectGraphMappingBuilder setGraphDescription(GraphDescriptionBuilder builder) {
    this.graphDescription = builder.build();
    return this;
  }
}
