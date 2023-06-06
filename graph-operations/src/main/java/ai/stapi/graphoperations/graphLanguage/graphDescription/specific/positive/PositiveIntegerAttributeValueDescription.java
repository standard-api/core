package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.PositiveIntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class PositiveIntegerAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "PositiveIntegerAttributeValueDescription";

  public PositiveIntegerAttributeValueDescription() {
    super(SERIALIZATION_TYPE, PositiveIntegerAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public PositiveIntegerAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, PositiveIntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public PositiveIntegerAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, PositiveIntegerAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
