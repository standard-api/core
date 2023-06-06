package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.XhtmlAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.XhtmlAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XhtmlAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected XhtmlAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new XhtmlAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof XhtmlAttributeValueDescription;
  }


  @Override
  public String getSupportedDataTypeId() {
    return XhtmlAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public XhtmlAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new XhtmlAttributeValueDescription(newChildren);
  }

  @Override
  public XhtmlAttributeValueDescriptionBuilder getCopy() {
    var builder = new XhtmlAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
