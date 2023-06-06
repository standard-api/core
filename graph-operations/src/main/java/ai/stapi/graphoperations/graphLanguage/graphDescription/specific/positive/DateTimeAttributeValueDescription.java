package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class DateTimeAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "DateTimeAttributeValueDescription";

  public DateTimeAttributeValueDescription() {
    super(SERIALIZATION_TYPE, DateTimeAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }


  public DateTimeAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DateTimeAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public DateTimeAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, DateTimeAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
