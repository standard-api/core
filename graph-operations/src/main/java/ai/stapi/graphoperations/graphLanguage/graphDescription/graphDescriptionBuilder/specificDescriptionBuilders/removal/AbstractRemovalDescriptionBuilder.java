package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.AbstractSpecificDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import java.util.List;

public abstract class AbstractRemovalDescriptionBuilder extends AbstractSpecificDescriptionBuilder {

  @Override
  public abstract RemovalGraphDescription build();

  @Override
  public abstract boolean represents(GraphDescription graphDescription);

  @Override
  public abstract AbstractRemovalDescriptionBuilder setValues(GraphDescription graphDescription);

  @Override
  public abstract RemovalGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  );

  @Override
  public abstract AbstractRemovalDescriptionBuilder getCopy();
}
