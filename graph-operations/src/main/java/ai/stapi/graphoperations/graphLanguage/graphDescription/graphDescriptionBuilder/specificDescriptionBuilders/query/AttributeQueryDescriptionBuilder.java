package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.query;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.AbstractAttributeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeQueryDescriptionBuilder extends AbstractAttributeDescriptionBuilder {

  @Override
  protected PositiveGraphDescription buildAttributeDescription(
      AttributeDescriptionParameters parameters, 
      List<GraphDescription> children
  ) {
    return new AttributeQueryDescription(parameters, children);
  }

  @Override
  public String getSupportedStructureTypeId() {
    return AttributeQueryDescription.DESCRIBED_STRUCTURE_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof AttributeQueryDescription;
  }

  @Override
  public PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var param = (AttributeDescriptionParameters) graphDescription.getParameters();
    return new AttributeQueryDescription(param, newChildren);
  }

  @Override
  public AbstractAttributeDescriptionBuilder getCopy() {
    var builder = new AttributeQueryDescriptionBuilder();
    builder.setAttributeName(this.getAttributeName());
    builder.setChildren(this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::getCopy)
        .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
