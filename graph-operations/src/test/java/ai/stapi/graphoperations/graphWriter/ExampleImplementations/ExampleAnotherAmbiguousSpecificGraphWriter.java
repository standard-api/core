package ai.stapi.graphoperations.graphWriter.ExampleImplementations;

import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import org.springframework.stereotype.Service;

@Service
public class ExampleAnotherAmbiguousSpecificGraphWriter implements SpecificGraphWriter {

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof ExampleAmbiguousGraphDescription;
  }

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    return null;
  }
}
