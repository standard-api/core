package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class StringAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "StringAttributeValueDescription";

  public StringAttributeValueDescription() {
    super(SERIALIZATION_TYPE, StringAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }


  public StringAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, StringAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public StringAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, StringAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
