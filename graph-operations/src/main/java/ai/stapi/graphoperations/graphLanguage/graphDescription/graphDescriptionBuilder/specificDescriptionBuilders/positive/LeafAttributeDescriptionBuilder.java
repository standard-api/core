package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeafAttributeDescriptionBuilder extends AbstractAttributeDescriptionBuilder {

  @Override
  protected PositiveGraphDescription buildAttributeDescription(
      AttributeDescriptionParameters parameters, 
      List<GraphDescription> children
  ) {
    return new LeafAttributeDescription(parameters, children);
  }

  @Override
  public String getSupportedStructureTypeId() {
    return LeafAttribute.DATA_STRUCTURE_TYPE;
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof LeafAttributeDescription;
  }

  @Override
  public PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var param = (AttributeDescriptionParameters) graphDescription.getParameters();
    return new LeafAttributeDescription(param, newChildren);
  }

  @Override
  public AbstractAttributeDescriptionBuilder getCopy() {
    var builder = new LeafAttributeDescriptionBuilder();
    builder.setAttributeName(this.getAttributeName());
    builder.setChildren(this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::getCopy)
        .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }
}
