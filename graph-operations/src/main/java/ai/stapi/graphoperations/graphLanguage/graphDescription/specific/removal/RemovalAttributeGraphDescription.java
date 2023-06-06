package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class RemovalAttributeGraphDescription extends AbstractRemovalGraphDescription {

  public static final String SERIALIZATION_TYPE = "b4e6fc19-078a-46f0-848d-4ba7556919a4";
  private RemovalAttributeDescriptionParameters parameters;

  private RemovalAttributeGraphDescription() {

  }

  public RemovalAttributeGraphDescription(
      RemovalAttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  public RemovalAttributeGraphDescription(RemovalAttributeDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }


  public RemovalAttributeGraphDescription(
      RemovalAttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE, childDescriptions);
    this.parameters = parameters;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
