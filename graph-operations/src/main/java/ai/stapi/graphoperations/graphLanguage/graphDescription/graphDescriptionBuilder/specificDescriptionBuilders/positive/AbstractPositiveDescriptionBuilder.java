package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.AbstractSpecificDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public abstract class AbstractPositiveDescriptionBuilder
    extends AbstractSpecificDescriptionBuilder {

  @Override
  public abstract PositiveGraphDescription build();

  @Override
  public abstract AbstractPositiveDescriptionBuilder setValues(GraphDescription graphDescription);

  @Override
  public abstract PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  );

  @Override
  public abstract AbstractPositiveDescriptionBuilder getCopy();


}
