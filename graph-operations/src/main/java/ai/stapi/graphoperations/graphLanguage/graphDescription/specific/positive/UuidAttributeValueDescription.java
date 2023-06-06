package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.UuidAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class UuidAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "UuidAttributeValueDescription";

  public UuidAttributeValueDescription() {
    super(SERIALIZATION_TYPE, UuidAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }
  public UuidAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UuidAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public UuidAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, UuidAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
