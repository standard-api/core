package ai.stapi.graphoperations.graphWriter;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;

public interface SpecificGraphWriter {

  GraphBuilder write(GraphDescription graphDescription, GraphBuilder builder);

  boolean supports(GraphDescription graphDescription);
}
