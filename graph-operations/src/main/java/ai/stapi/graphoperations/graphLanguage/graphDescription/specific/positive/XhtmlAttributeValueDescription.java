package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.XhtmlAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class XhtmlAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "XhtmlAttributeValueDescription";

  public XhtmlAttributeValueDescription() {
    super(SERIALIZATION_TYPE, XhtmlAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public XhtmlAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, XhtmlAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public XhtmlAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, XhtmlAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
