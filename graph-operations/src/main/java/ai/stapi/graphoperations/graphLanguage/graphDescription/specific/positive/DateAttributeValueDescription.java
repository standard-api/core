package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class DateAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "DateAttributeValueDescription";

  public DateAttributeValueDescription() {
    super(SERIALIZATION_TYPE, DateAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }

  public DateAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DateAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public DateAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DateAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
