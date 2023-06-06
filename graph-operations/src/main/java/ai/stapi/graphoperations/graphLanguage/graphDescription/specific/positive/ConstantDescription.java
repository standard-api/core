package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;

public class ConstantDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "ConstantDescription";
  private ConstantDescriptionParameters parameters;

  private ConstantDescription() {
  }

  public ConstantDescription(ConstantDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, GraphBaseTypes.CONSTANT_TYPE, new ArrayList<>());
    this.parameters = parameters;
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }
}
