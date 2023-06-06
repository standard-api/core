package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAttributeValueDescriptionBuilder
    extends AbstractPositiveDescriptionBuilder {

  protected abstract PositiveGraphDescription buildAttributeDescription(
      List<GraphDescription> children
  );

  public abstract String getSupportedDataTypeId();

  @Override
  public abstract boolean represents(GraphDescription graphDescription);

  @Override
  public abstract PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  );

  @Override
  public abstract AbstractAttributeValueDescriptionBuilder getCopy();

  @Override
  public PositiveGraphDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return this.buildAttributeDescription(children);
  }

  @Override
  public AbstractPositiveDescriptionBuilder setValues(GraphDescription graphDescription) {
    return this;
  }
}
