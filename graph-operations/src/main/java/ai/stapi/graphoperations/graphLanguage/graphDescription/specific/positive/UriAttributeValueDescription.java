package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.UriAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class UriAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "UriAttributeValueDescription";

  public UriAttributeValueDescription() {
    super(SERIALIZATION_TYPE, UriAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }
  public UriAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UriAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public UriAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UriAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
