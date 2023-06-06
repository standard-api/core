package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class TimeAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "TimeAttributeValueDescription";

  public TimeAttributeValueDescription() {
    super(SERIALIZATION_TYPE, TimeAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }


  public TimeAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, TimeAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public TimeAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, TimeAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
