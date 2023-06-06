package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.UnsignedIntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class UnsignedIntegerAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "UnsignedIntegerAttributeValueDescription";

  public UnsignedIntegerAttributeValueDescription() {
    super(SERIALIZATION_TYPE, UnsignedIntegerAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public UnsignedIntegerAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UnsignedIntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public UnsignedIntegerAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UnsignedIntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
