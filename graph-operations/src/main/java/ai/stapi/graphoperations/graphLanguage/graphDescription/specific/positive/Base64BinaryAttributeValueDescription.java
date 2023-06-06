package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.Base64BinaryAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class Base64BinaryAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "Base64BinaryAttributeValueDescription";

  public Base64BinaryAttributeValueDescription() {
    super(
        SERIALIZATION_TYPE, 
        Base64BinaryAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>()
    );
  }

  public Base64BinaryAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, Base64BinaryAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public Base64BinaryAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, Base64BinaryAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
