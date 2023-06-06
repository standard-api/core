package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.BooleanAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BooleanAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected BooleanAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new BooleanAttributeValueDescription(children);
  }

  @Override
  public String getSupportedDataTypeId() {
    return BooleanAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof BooleanAttributeValueDescription;
  }

  @Override
  public BooleanAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new BooleanAttributeValueDescription(newChildren);
  }

  @Override
  public BooleanAttributeValueDescriptionBuilder getCopy() {
    var builder = new BooleanAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
