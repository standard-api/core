package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;

public class InterfaceObjectGraphMapping extends AbstractObjectGraphMapping {

  public static final String SERIALIZATION_TYPE = "InterfaceObjectGraphMapping";
  private String interfaceUuid;

  public InterfaceObjectGraphMapping() {
    super(SERIALIZATION_TYPE);
  }

  public InterfaceObjectGraphMapping(
      String interfaceUuid,
      GraphDescription graphDescription
  ) {
    super(
        graphDescription,
        SERIALIZATION_TYPE
    );
    this.interfaceUuid = interfaceUuid;
  }

  public String getInterfaceUuid() {
    return interfaceUuid;
  }
}
