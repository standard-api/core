package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeValueDescription;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;

public class AttributeValueSpecificGraphWriter implements SpecificGraphWriter {

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    return builder;
  }

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof AbstractAttributeValueDescription;
  }
}
