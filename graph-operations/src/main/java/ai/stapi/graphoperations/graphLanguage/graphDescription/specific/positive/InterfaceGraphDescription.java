package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.List;

public class InterfaceGraphDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "InterfaceGraphDescription";

  private InterfaceGraphDescriptionParameters parameters;

  private InterfaceGraphDescription() {
  }

  public InterfaceGraphDescription(
      InterfaceGraphDescriptionParameters parameters,
      List<GraphDescription> childDescriptions) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NULL_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  public InterfaceGraphDescription(
      InterfaceGraphDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NULL_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
