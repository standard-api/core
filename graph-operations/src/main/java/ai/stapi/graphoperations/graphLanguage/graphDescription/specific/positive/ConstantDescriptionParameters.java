package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;

public class ConstantDescriptionParameters implements GraphDescriptionParameters {

  private final Object value;

  public ConstantDescriptionParameters(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }
}
