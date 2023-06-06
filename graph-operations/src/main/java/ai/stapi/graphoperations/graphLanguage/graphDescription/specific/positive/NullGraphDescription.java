package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;

public class NullGraphDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "NullGraphDescription";

  public NullGraphDescription() {
    super(SERIALIZATION_TYPE, GraphBaseTypes.NULL_TYPE, new ArrayList<>());
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return new GraphDescriptionParameters() {
    };
  }
}
