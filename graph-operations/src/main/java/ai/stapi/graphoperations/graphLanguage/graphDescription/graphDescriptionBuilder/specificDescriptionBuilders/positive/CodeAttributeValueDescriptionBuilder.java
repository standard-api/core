package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.CodeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.CodeAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeAttributeValueDescriptionBuilder extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected CodeAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new CodeAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof CodeAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return CodeAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public CodeAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new CodeAttributeValueDescription(newChildren);
  }

  @Override
  public CodeAttributeValueDescriptionBuilder getCopy() {
    var builder = new CodeAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
