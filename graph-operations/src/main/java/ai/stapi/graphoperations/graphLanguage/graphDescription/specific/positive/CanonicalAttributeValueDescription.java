package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.CanonicalAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class CanonicalAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "CanonicalAttributeValueDescription";
  
  public CanonicalAttributeValueDescription() {
    super(
        SERIALIZATION_TYPE, 
        CanonicalAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>()
    );
  }

  public CanonicalAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, CanonicalAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public CanonicalAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, CanonicalAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

}
