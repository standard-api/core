package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.StringAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected StringAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new StringAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof StringAttributeValueDescription;
  }

  @Override
  public StringAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new StringAttributeValueDescription(newChildren);
  }

  @Override
  public String getSupportedDataTypeId() {
    return StringAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public StringAttributeValueDescriptionBuilder getCopy() {
    var builder = new StringAttributeValueDescriptionBuilder();
    builder.setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new))
    );
    return builder;
  }
}
