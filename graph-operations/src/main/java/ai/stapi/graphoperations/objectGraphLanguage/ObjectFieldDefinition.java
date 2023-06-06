package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.serialization.AbstractSerializableObject;

public class ObjectFieldDefinition extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "ObjectFieldDefinition";
  private Declaration relation;
  private ObjectGraphMapping fieldObjectGraphMapping;

  private ObjectFieldDefinition() {
  }

  public ObjectFieldDefinition(
      Declaration relation,
      ObjectGraphMapping fieldObjectGraphMapping
  ) {
    super(SERIALIZATION_TYPE);
    this.fieldObjectGraphMapping = fieldObjectGraphMapping;
    this.relation = relation;
  }

  public ObjectFieldDefinition(
      Declaration relation,
      ObjectGraphMapping fieldObjectGraphMapping,
      String serializationType
  ) {
    super(serializationType);
    this.fieldObjectGraphMapping = fieldObjectGraphMapping;
    this.relation = relation;
  }

  public ObjectGraphMapping getFieldObjectGraphMapping() {
    return fieldObjectGraphMapping;
  }

  public Declaration getRelation() {
    return relation;
  }
}
