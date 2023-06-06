package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.extrapolate;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractPositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeValueDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.ExtrapolateGraphDescription;

public class AttributeExtrapolatingDescription extends AbstractPositiveGraphDescription
    implements ExtrapolateGraphDescription {

  public static final String SERIALIZATION_TYPE = "1c2b8eb5-ab89-4893-bd35-64b9463ad80c";
  private final GraphDescription extrapolatingInstructions;
  private AttributeValueDescriptionParameters parameters;

  public AttributeExtrapolatingDescription(
      AttributeValueDescriptionParameters parameters,
      GraphDescription extrapolatingInstructions,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE, childDescriptions);
    this.extrapolatingInstructions = extrapolatingInstructions;
    this.parameters = parameters;
  }

  public GraphDescription getExtrapolatingInstructions() {
    return extrapolatingInstructions;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return this.parameters;
  }
}
