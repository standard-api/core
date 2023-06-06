package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public class BooleanAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "BooleanAttributeValueDescription";

  private BooleanAttributeValueDescription() {

  }

  public BooleanAttributeValueDescription(
      GraphDescription... childDescriptions
  ) {
    super(
        SERIALIZATION_TYPE, 
        BooleanAttributeValue.SERIALIZATION_TYPE,
        childDescriptions
    );
  }

  public BooleanAttributeValueDescription(
      List<GraphDescription> childDescriptions
  ) {
    super(
        SERIALIZATION_TYPE, 
        BooleanAttributeValue.SERIALIZATION_TYPE,
        childDescriptions
    );
  }
}
