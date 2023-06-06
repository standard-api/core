package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;

public class LeafObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "LeafObjectGraphMapping";

  private LeafObjectGraphMapping() {

  }

  public LeafObjectGraphMapping(GraphDescription graphDescription) {
    super(
        graphDescription,
        SERIALIZATION_TYPE
    );
  }
}
