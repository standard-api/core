package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.Base64BinaryAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.Base64BinaryAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Base64BinaryAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected Base64BinaryAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new Base64BinaryAttributeValueDescription(children);
  }

  @Override
  public String getSupportedDataTypeId() {
    return Base64BinaryAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof Base64BinaryAttributeValueDescription;
  }

  @Override
  public Base64BinaryAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new Base64BinaryAttributeValueDescription(newChildren);
  }

  @Override
  public Base64BinaryAttributeValueDescriptionBuilder getCopy() {
    var builder = new Base64BinaryAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    
    return builder;
  }
}
