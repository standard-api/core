package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class RemovalNodeDescription extends AbstractRemovalGraphDescription {

  public static final String SERIALIZATION_TYPE = "896d20da-c93a-4454-bbd9-7561dd86be24";
  private RemovalNodeDescriptionParameters parameters;

  private RemovalNodeDescription() {

  }

  public RemovalNodeDescription(
      RemovalNodeDescriptionParameters parameters,
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NODE_TYPE, childDeclarations);
    this.parameters = parameters;
  }

  public RemovalNodeDescription(RemovalNodeDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NODE_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }

  public RemovalNodeDescription(
      RemovalNodeDescriptionParameters parameters,
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NODE_TYPE, childDeclarations);
    this.parameters = parameters;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
