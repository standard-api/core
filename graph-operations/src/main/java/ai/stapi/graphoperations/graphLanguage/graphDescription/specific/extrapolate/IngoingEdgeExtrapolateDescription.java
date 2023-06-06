package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.extrapolate;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.ExtrapolateGraphDescription;
import java.util.List;

public class IngoingEdgeExtrapolateDescription extends AbstractEdgeDescription
    implements ExtrapolateGraphDescription {

  public static final String SERIALIZATION_TYPE = "ecfd6af9-cea9-4284-9271-a6d7b78b5c10";
  private final GraphDescription extrapolatingInstructions;

  public IngoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      List<GraphDescription> childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public IngoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions
  ) {
    super(parameters, SERIALIZATION_TYPE);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public IngoingEdgeExtrapolateDescription(
      EdgeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      GraphDescription... childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  @Override
  public boolean isOutgoing() {
    return false;
  }

  public GraphDescription getExtrapolatingInstructions() {
    return extrapolatingInstructions;
  }
}
