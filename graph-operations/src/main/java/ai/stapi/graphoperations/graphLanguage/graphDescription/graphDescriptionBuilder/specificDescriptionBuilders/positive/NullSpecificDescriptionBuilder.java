package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import java.util.List;

public class NullSpecificDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  @Override
  public NullGraphDescription build() {
    return new NullGraphDescription();
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof NullGraphDescription;
  }

  @Override
  public NullSpecificDescriptionBuilder setValues(GraphDescription graphDescription) {
    return new NullSpecificDescriptionBuilder();
  }

  @Override
  public NullGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new NullGraphDescription(newChildren);
  }

  @Override
  public NullSpecificDescriptionBuilder getCopy() {
    return new NullSpecificDescriptionBuilder();
  }
}
