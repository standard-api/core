package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.CanonicalAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.CanonicalAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CanonicalAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected CanonicalAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new CanonicalAttributeValueDescription(children);
  }

  @Override
  public String getSupportedDataTypeId() {
    return CanonicalAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof CanonicalAttributeValueDescription;
  }

  @Override
  public CanonicalAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new CanonicalAttributeValueDescription(newChildren);
  }

  @Override
  public CanonicalAttributeValueDescriptionBuilder getCopy() {
    var builder = new CanonicalAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
