package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class InterfaceGraphDescriptionParameters implements GraphDescriptionParameters {

  private String interfaceId;

  private InterfaceGraphDescriptionParameters() {
  }

  public InterfaceGraphDescriptionParameters(String interfaceId) {
    this.interfaceId = interfaceId;
  }

  public String getInterfaceId() {
    return interfaceId;
  }
}
