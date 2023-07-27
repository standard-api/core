package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class NullGraphDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "NullGraphDescription";

  public NullGraphDescription() {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NULL_TYPE, new ArrayList<>());
  }

  public NullGraphDescription(List<GraphDescription> childDescriptions) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NULL_TYPE, childDescriptions);
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return new GraphDescriptionParameters() {
    };
  }
}
