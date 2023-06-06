package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;

public class GraphElementTypeDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "GraphElementTypeDescription";

  public GraphElementTypeDescription() {
    super(
        SERIALIZATION_TYPE,
        GraphBaseTypes.NODE_TYPE,
        new ArrayList<>()
    );
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return null;
  }

}
