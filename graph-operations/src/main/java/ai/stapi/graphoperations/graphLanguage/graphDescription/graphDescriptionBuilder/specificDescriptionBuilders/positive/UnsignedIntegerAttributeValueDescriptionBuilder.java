package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.UnsignedIntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UnsignedIntegerAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnsignedIntegerAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected UnsignedIntegerAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new UnsignedIntegerAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof UnsignedIntegerAttributeValueDescription;
  }

  @Override
  public UnsignedIntegerAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new UnsignedIntegerAttributeValueDescription(newChildren);
  }

  @Override
  public String getSupportedDataTypeId() {
    return UnsignedIntegerAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public UnsignedIntegerAttributeValueDescriptionBuilder getCopy() {
    var builder = new UnsignedIntegerAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
