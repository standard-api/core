package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.attributeValue.MarkdownAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class MarkdownAttributeValueDescription extends AbstractAttributeValueDescription {

  public static final String SERIALIZATION_TYPE = "MarkdownAttributeValueDescription";

  public MarkdownAttributeValueDescription() {
    super(SERIALIZATION_TYPE, MarkdownAttributeValue.SERIALIZATION_TYPE,
        new ArrayList<>());
  }
  
  public MarkdownAttributeValueDescription(
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, MarkdownAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }

  public MarkdownAttributeValueDescription(
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, MarkdownAttributeValue.SERIALIZATION_TYPE,
        childDeclarations);
  }
}
