package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;

public class ReferenceObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "ReferenceObjectGraphMapping";

  private String referencedSerializationType;

  protected ReferenceObjectGraphMapping() {
    super();
  }

  public ReferenceObjectGraphMapping(
      GraphDescription graphDescription,
      String referencedSerializationType
  ) {
    super(graphDescription, SERIALIZATION_TYPE);
    this.referencedSerializationType = referencedSerializationType;
  }

  public ReferenceObjectGraphMapping(String referencedSerializationType) {
    super(SERIALIZATION_TYPE);
    this.referencedSerializationType = referencedSerializationType;
  }

  public String getReferencedSerializationType() {
    return referencedSerializationType;
  }
}
