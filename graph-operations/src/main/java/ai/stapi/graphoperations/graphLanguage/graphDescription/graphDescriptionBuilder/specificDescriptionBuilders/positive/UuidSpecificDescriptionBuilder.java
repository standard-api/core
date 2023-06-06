package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UuidSpecificDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  @Override
  public UuidIdentityDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return new UuidIdentityDescription(children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof UuidIdentityDescription;
  }

  @Override
  public UuidSpecificDescriptionBuilder setValues(GraphDescription graphDescription) {
    return this;
  }

  @Override
  public UuidIdentityDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new UuidIdentityDescription(newChildren);
  }

  @Override
  public UuidSpecificDescriptionBuilder getCopy() {
    var builder = new UuidSpecificDescriptionBuilder();
    builder.setChildren(
        this.getChildren().stream()
            .map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new))
    );
    return builder;
  }
}
