package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;

public class ListObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "ListObjectGraphMapping";
  private ObjectGraphMapping childObjectGraphMapping;

  private ListObjectGraphMapping() {

  }

  public ListObjectGraphMapping(
      GraphDescription graphDescription,
      ObjectGraphMapping childObjectGraphMapping
  ) {
    super(
        graphDescription,
        SERIALIZATION_TYPE
    );
    this.childObjectGraphMapping = childObjectGraphMapping;
  }

  public ObjectGraphMapping getChildObjectGraphMapping() {
    return childObjectGraphMapping;
  }
}
