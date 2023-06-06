package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public class OutgoingEdgeDescription extends AbstractEdgeDescription {

  public static final String SERIALIZATION_TYPE = "OutgoingEdgeDescription";

  protected OutgoingEdgeDescription() {

  }

  public OutgoingEdgeDescription(
      EdgeDescriptionParameters parameters,
      List<GraphDescription> childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
  }

  public OutgoingEdgeDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription... childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
  }

  public OutgoingEdgeDescription(EdgeDescriptionParameters parameters) {
    super(parameters, SERIALIZATION_TYPE);
  }

  @Override
  public boolean isOutgoing() {
    return true;
  }
}
