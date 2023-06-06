package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.CodeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class CodeAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "CodeAttributeValueDescription";

  public CodeAttributeValueDescription() {
    super(SERIALIZATION_TYPE, CodeAttributeValue.SERIALIZATION_TYPE, new ArrayList<>());
  }
  public CodeAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, CodeAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }

  public CodeAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, CodeAttributeValue.SERIALIZATION_TYPE, childDeclarations);
  }
}
