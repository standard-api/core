package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.Map;

public class ObjectObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "ObjectObjectGraphMapping";
  private Map<String, ObjectFieldDefinition> fields;

  private ObjectObjectGraphMapping() {

  }

  public ObjectObjectGraphMapping(
      GraphDescription graphDescription,
      Map<String, ObjectFieldDefinition> fields
  ) {
    super(
        graphDescription,
        SERIALIZATION_TYPE
    );
    this.fields = fields;
  }

  public ObjectObjectGraphMapping(
      GraphDescription graphDescription,
      Map<String, ObjectFieldDefinition> fields,
      String serializationType
  ) {
    super(
        graphDescription,
        serializationType
    );
    this.fields = fields;
  }

  public Map<String, ObjectFieldDefinition> getFields() {
    return fields;
  }

}
