package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.UriAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UriAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UriAttributeValueDescriptionBuilder extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected UriAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new UriAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof UriAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return UriAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public UriAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new UriAttributeValueDescription(newChildren);
  }

  @Override
  public UriAttributeValueDescriptionBuilder getCopy() {
    var builder = new UriAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
