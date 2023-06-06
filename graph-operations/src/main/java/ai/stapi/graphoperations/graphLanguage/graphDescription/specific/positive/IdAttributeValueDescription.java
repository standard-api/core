package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class IdAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "IdAttributeValueDescription";

  public IdAttributeValueDescription() {
    super(SERIALIZATION_TYPE, IdAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }

  public IdAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, IdAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public IdAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, IdAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
