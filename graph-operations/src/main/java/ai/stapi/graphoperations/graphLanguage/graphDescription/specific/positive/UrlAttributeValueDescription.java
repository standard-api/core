package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.UrlAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class UrlAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "UrlAttributeValueDescription";

  public UrlAttributeValueDescription() {
    super(SERIALIZATION_TYPE, UrlAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }

  public UrlAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UrlAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public UrlAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UrlAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
