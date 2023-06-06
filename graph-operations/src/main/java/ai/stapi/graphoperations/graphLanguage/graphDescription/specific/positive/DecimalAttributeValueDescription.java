package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class DecimalAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "DecimalAttributeValueDescription";

  public DecimalAttributeValueDescription() {
    super(SERIALIZATION_TYPE, DecimalAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public DecimalAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DecimalAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public DecimalAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DecimalAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
