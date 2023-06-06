package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class InstantAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "InstantAttributeValueDescription";

  public InstantAttributeValueDescription() {
    super(SERIALIZATION_TYPE, InstantAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }

  public InstantAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, InstantAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public InstantAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, InstantAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
