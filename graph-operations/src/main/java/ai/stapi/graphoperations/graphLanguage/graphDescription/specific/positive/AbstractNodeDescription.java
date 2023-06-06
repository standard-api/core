package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.List;

public class AbstractNodeDescription extends AbstractPositiveGraphDescription {

  protected NodeDescriptionParameters parameters;

  protected AbstractNodeDescription() {

  }

  public AbstractNodeDescription(
      String serializationType,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.NODE_TYPE, childDescriptions);
  }

  public AbstractNodeDescription(
      String serializationType,
      NodeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.NODE_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  public AbstractNodeDescription(
      String serializationType,
      NodeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.NODE_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
