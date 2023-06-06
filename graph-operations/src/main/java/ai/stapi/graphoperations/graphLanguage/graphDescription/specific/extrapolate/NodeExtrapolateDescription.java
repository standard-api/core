package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.extrapolate;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.ExtrapolateGraphDescription;
import java.util.List;

public class NodeExtrapolateDescription extends AbstractNodeDescription
    implements ExtrapolateGraphDescription {

  public static final String SERIALIZATION_TYPE = "9abac212-e2e6-4eb7-8707-026fcdf3fdc9";

  private final GraphDescription extrapolatingInstructions;

  public NodeExtrapolateDescription(
      NodeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, parameters, childDescriptions);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public NodeExtrapolateDescription(
      NodeDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, parameters, childDescriptions);
    this.extrapolatingInstructions = extrapolatingInstructions;
  }

  public GraphDescription getExtrapolatingInstructions() {
    return extrapolatingInstructions;
  }
}
