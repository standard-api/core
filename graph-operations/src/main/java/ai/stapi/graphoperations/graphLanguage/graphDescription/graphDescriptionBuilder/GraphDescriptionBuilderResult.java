package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import java.util.List;

public class GraphDescriptionBuilderResult {

  private final List<RemovalGraphDescription> removalGraphDescriptions;
  private final PositiveGraphDescription positiveGraphDescription;
  private final GraphDescription graphDescription;

  public GraphDescriptionBuilderResult(
      List<RemovalGraphDescription> removalGraphDescriptions,
      PositiveGraphDescription positiveGraphDescription,
      GraphDescription graphDescription
  ) {
    this.removalGraphDescriptions = removalGraphDescriptions;
    this.positiveGraphDescription = positiveGraphDescription;
    this.graphDescription = graphDescription;
  }

  public PositiveGraphDescription getPositiveGraphDescription() {
    return positiveGraphDescription;
  }

  public GraphDescription getGraphDescription() {
    return graphDescription;
  }

  public List<RemovalGraphDescription> getRemovalGraphDescriptions() {
    return removalGraphDescriptions;
  }
}
