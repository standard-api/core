package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAttributeDescriptionBuilder
    extends AbstractPositiveDescriptionBuilder {
  
  private String attributeName;

  protected abstract PositiveGraphDescription buildAttributeDescription(
      AttributeDescriptionParameters parameters,
      List<GraphDescription> children
  );

  public abstract String getSupportedStructureTypeId();

  @Override
  public abstract boolean represents(GraphDescription graphDescription);

  @Override
  public abstract PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  );

  @Override
  public abstract AbstractAttributeDescriptionBuilder getCopy();

  @Override
  public PositiveGraphDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return this.buildAttributeDescription(
        new AttributeDescriptionParameters(attributeName),
        children
    );
  }

  @Override
  public AbstractPositiveDescriptionBuilder setValues(GraphDescription graphDescription) {
    var params = (AttributeDescriptionParameters) graphDescription.getParameters();
    this.setAttributeName(params.getAttributeName());
    return this;
  }

  public AbstractAttributeDescriptionBuilder setAttributeName(String attributeName) {
    this.attributeName = attributeName;
    return this;
  }

  public String getAttributeName() {
    return attributeName;
  }
}
