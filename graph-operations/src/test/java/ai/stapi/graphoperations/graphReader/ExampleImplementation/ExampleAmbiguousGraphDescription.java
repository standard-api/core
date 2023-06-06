package ai.stapi.graphoperations.graphReader.ExampleImplementation;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;

public class ExampleAmbiguousGraphDescription implements PositiveGraphDescription {

  @Override
  public String getSerializationType() {
    return null;
  }

  @Override
  public String getDeclarationType() {
    return null;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return null;
  }

  @Override
  public String getGraphTraversingType() {
    return null;
  }

  @Override
  public List<GraphDescription> getChildGraphDescriptions() {
    return new ArrayList<>();
  }
}
