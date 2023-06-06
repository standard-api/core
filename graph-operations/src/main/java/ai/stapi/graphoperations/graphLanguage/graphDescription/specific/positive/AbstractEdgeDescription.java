package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEdgeDescription extends AbstractPositiveGraphDescription {

  public static final String TRAVERSING_TYPE = GraphBaseTypes.EDGE_TYPE;
  private EdgeDescriptionParameters parameters;

  protected AbstractEdgeDescription() {
  }

  protected AbstractEdgeDescription(
      EdgeDescriptionParameters parameters,
      String serializationType,
      List<GraphDescription> childDeclarations
  ) {
    super(
        serializationType,
        TRAVERSING_TYPE,
        childDeclarations
    );
    this.parameters = parameters;
  }

  protected AbstractEdgeDescription(
      EdgeDescriptionParameters parameters,
      String serializationType
  ) {
    super(serializationType, TRAVERSING_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }

  protected AbstractEdgeDescription(
      EdgeDescriptionParameters parameters,
      String serializationType,
      GraphDescription... childDeclarations
  ) {
    super(serializationType, TRAVERSING_TYPE, childDeclarations);
    this.parameters = parameters;
  }

  public abstract boolean isOutgoing();

  public boolean isIngoing() {
    return !this.isOutgoing();
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
