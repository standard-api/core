package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class NodeDescription extends AbstractNodeDescription {

  public static final String SERIALIZATION_TYPE = "NodeDescription";

  protected NodeDescription() {

  }

  public NodeDescription(
      NodeDescriptionParameters parameters,
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, childDeclarations);
    this.parameters = parameters;
  }

  public NodeDescription(NodeDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }

  public NodeDescription(
      NodeDescriptionParameters parameters,
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, parameters, childDeclarations);
  }

  public NodeDescription(
      String nodeType,
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, new NodeDescriptionParameters(nodeType), childDeclarations);
  }
}
