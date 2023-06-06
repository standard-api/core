package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IntegerAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected IntegerAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new IntegerAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof IntegerAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return IntegerAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public IntegerAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new IntegerAttributeValueDescription(newChildren);
  }

  @Override
  public IntegerAttributeValueDescriptionBuilder getCopy() {
    var builder = new IntegerAttributeValueDescriptionBuilder();
    builder.setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
