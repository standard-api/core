package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.List;

public class AllAttributesDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "AllAttributesDescription";

  private AllAttributesDescriptionParameters allAttributesDescriptionParameters;

  protected AllAttributesDescription() {
  }

  public AllAttributesDescription(List<GraphDescription> childDescriptions) {
    super(AllAttributesDescription.SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE,
        childDescriptions);
  }

  public AllAttributesDescription(GraphDescription... childDescriptions) {
    super(AllAttributesDescription.SERIALIZATION_TYPE, GraphBaseTypes.VALUE_TYPE,
        childDescriptions);
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return this.allAttributesDescriptionParameters;
  }
}
