package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.TimeAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeAttributeValueDescriptionBuilder extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected TimeAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new TimeAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof TimeAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return TimeAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public TimeAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new TimeAttributeValueDescription(newChildren);
  }

  @Override
  public TimeAttributeValueDescriptionBuilder getCopy() {
    var builder = new TimeAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::getCopy)
        .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
