package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.DateTimeAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DateTimeAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected DateTimeAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new DateTimeAttributeValueDescription(children);
  }

  @Override
  public String getSupportedDataTypeId() {
    return DateTimeAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof DateTimeAttributeValueDescription;
  }

  @Override
  public DateTimeAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new DateTimeAttributeValueDescription(newChildren);
  }

  @Override
  public DateTimeAttributeValueDescriptionBuilder getCopy() {
    var builder = new DateTimeAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
