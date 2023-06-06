package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ListAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListAttributeDescriptionBuilder extends AbstractAttributeDescriptionBuilder {

  @Override
  protected PositiveGraphDescription buildAttributeDescription(
      AttributeDescriptionParameters parameters, 
      List<GraphDescription> children
  ) {
    return new ListAttributeDescription(parameters, children);
  }

  @Override
  public String getSupportedStructureTypeId() {
    return ListAttribute.DATA_STRUCTURE_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof ListAttributeDescription;
  }

  @Override
  public PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var param = (AttributeDescriptionParameters) graphDescription.getParameters();
    return new ListAttributeDescription(param, newChildren);
  }

  @Override
  public AbstractAttributeDescriptionBuilder getCopy() {
    var builder = new ListAttributeDescriptionBuilder();
    builder.setAttributeName(this.getAttributeName());
    builder.setChildren(this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::getCopy)
        .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
