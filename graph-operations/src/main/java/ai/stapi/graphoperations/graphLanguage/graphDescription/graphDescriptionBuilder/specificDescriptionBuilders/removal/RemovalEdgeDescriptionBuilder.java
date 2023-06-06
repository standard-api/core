package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescriptionParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemovalEdgeDescriptionBuilder extends AbstractRemovalDescriptionBuilder {

  private String edgeType;

  @Override
  public RemovalEdgeDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));

    return new RemovalEdgeDescription(new RemovalEdgeDescriptionParameters(edgeType), children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof RemovalEdgeDescription;
  }

  @Override
  public RemovalEdgeDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (RemovalEdgeDescriptionParameters) graphDescription.getParameters();
    this.edgeType = parameters.getEdgeType();
    return this;
  }

  @Override
  public RemovalEdgeDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (RemovalEdgeDescriptionParameters) graphDescription.getParameters();
    return new RemovalEdgeDescription(
        new RemovalEdgeDescriptionParameters(parameters.getEdgeType()), newChildren);
  }

  @Override
  public RemovalEdgeDescriptionBuilder getCopy() {
    var builder = new RemovalEdgeDescriptionBuilder();
    builder.setEdgeType(this.edgeType)
        .setChildren(this.getChildren().stream().map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  public RemovalEdgeDescriptionBuilder setEdgeType(String edgeType) {
    this.edgeType = edgeType;
    return this;
  }
}
