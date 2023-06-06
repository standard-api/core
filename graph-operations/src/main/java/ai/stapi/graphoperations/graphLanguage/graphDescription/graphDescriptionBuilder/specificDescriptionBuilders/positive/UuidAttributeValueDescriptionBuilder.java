package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.UuidAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UuidAttributeValueDescriptionBuilder extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected UuidAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new UuidAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof UuidAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return UuidAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public UuidAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new UuidAttributeValueDescription(newChildren);
  }

  @Override
  public UuidAttributeValueDescriptionBuilder getCopy() {
    var builder = new UuidAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
