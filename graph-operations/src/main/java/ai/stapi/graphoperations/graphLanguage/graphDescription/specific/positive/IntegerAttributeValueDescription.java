package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class IntegerAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "IntegerAttributeValueDescription";

  public IntegerAttributeValueDescription() {
    super(SERIALIZATION_TYPE, IntegerAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public IntegerAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, IntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public IntegerAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, IntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
