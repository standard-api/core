package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeValueDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.DecimalAttributeValueDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecimalAttributeValueDescriptionBuilder
    extends AbstractAttributeValueDescriptionBuilder {

  @Override
  protected DecimalAttributeValueDescription buildAttributeDescription(
      List<GraphDescription> children
  ) {
    return new DecimalAttributeValueDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof DecimalAttributeValueDescription;
  }


  @Override
  public String getSupportedDataTypeId() {
    return DecimalAttributeValue.SERIALIZATION_TYPE;
  }

  @Override
  public DecimalAttributeValueDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (AttributeValueDescriptionParameters) graphDescription.getParameters();
    return new DecimalAttributeValueDescription(newChildren);
  }

  @Override
  public DecimalAttributeValueDescriptionBuilder getCopy() {
    var builder = new DecimalAttributeValueDescriptionBuilder();
    builder.setChildren(this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
