package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.OidAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class OidAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "OidAttributeValueDescription";

  public OidAttributeValueDescription() {
    super(SERIALIZATION_TYPE, OidAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }

  public OidAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, OidAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public OidAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, OidAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
