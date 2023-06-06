package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.PositiveIntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveIntegerAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PositiveIntegerAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected PositiveIntegerAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new PositiveIntegerAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof PositiveIntegerAttributeValueDescription;
  }

  @Override
  public PositiveIntegerAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new PositiveIntegerAttributeValueDescription(newChildren);
  }

  @Override
  public String getSupportedDataTypeId() {
    return PositiveIntegerAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public PositiveIntegerAttributeValueDescriptionBuilder getCopy() {
    var builder = new PositiveIntegerAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
