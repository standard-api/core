package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;

public class MapObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "MapObjectGraphMapping";

  private ObjectGraphMapping keyObjectGraphMapping;
  private ObjectGraphMapping valueObjectGraphMapping;

  private MapObjectGraphMapping() {

  }

  public MapObjectGraphMapping(
      GraphDescription graphDescription,
      ObjectGraphMapping keyObjectGraphMapping,
      ObjectGraphMapping valueObjectGraphMapping
  ) {
    super(graphDescription, SERIALIZATION_TYPE);
    this.keyObjectGraphMapping = keyObjectGraphMapping;
    this.valueObjectGraphMapping = valueObjectGraphMapping;
  }

  public ObjectGraphMapping getKeyObjectGraphMapping() {
    return keyObjectGraphMapping;
  }

  public ObjectGraphMapping getValueObjectGraphMapping() {
    return valueObjectGraphMapping;
  }
}
