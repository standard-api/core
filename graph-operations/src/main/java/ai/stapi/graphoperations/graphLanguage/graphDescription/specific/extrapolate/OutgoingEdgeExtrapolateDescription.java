package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.extrapolate;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.ExtrapolateGraphDescription;
import java.util.List;

public class OutgoingEdgeExtrapolateDescription extends AbstractEdgeDescription
    implements ExtrapolateGraphDescription {

  public static final String SERIALIZATION_TYPE = "00fc4be6-984f-41ac-b156-7acb27d67591";
  private final GraphDescription extrapolatingInstructions;

  public OutgoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      List<GraphDescription> childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public OutgoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions
  ) {
    super(parameters, SERIALIZATION_TYPE);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public OutgoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      GraphDescription... childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  @Override
  public boolean isOutgoing() {
    return true;
  }

  public GraphDescription getExtrapolatingInstructions() {
    return extrapolatingInstructions;
  }
}
