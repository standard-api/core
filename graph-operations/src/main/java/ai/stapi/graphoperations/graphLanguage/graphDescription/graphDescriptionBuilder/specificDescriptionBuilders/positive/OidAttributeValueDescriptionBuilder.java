package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.OidAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OidAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OidAttributeValueDescriptionBuilder extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected OidAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new OidAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof OidAttributeValueDescription;
  }

  @Override
  public String getSupportedDataTypeId() {
    return OidAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public OidAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new OidAttributeValueDescription(newChildren);
  }

  @Override
  public OidAttributeValueDescriptionBuilder getCopy() {
    var builder = new OidAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
