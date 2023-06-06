package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class RemovalEdgeDescription extends AbstractRemovalGraphDescription {

  public static final String SERIALIZATION_TYPE = "35df32b4-d654-4db4-abf5-fec3c8c48236";
  private RemovalEdgeDescriptionParameters parameters;

  private RemovalEdgeDescription() {

  }

  public RemovalEdgeDescription(
      RemovalEdgeDescriptionParameters parameters,
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NODE_TYPE, childDeclarations);
    this.parameters = parameters;
  }

  public RemovalEdgeDescription(RemovalEdgeDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NODE_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }

  public RemovalEdgeDescription(
      RemovalEdgeDescriptionParameters parameters,
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
