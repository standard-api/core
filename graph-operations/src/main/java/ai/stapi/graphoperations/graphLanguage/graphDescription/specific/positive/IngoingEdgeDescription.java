package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public class IngoingEdgeDescription extends AbstractEdgeDescription {

  public static final String SERIALIZATION_TYPE = "IngoingEdgeDescription";

  private IngoingEdgeDescription() {

  }

  public IngoingEdgeDescription(
      EdgeDescriptionParameters parameters,
      List<GraphDescription> childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
  }

  public IngoingEdgeDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription... childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
  }

  public IngoingEdgeDescription(EdgeDescriptionParameters parameters) {
    super(parameters, SERIALIZATION_TYPE);
  }

  @Override
  public boolean isOutgoing() {
    return false;
  }
}
