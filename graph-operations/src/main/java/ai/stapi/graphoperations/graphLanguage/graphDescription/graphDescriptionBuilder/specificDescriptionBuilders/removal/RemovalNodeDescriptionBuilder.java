package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalNodeDescriptionParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemovalNodeDescriptionBuilder extends AbstractRemovalDescriptionBuilder {

  private String nodeType;

  @Override
  public RemovalNodeDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return new RemovalNodeDescription(new RemovalNodeDescriptionParameters(nodeType), children);
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof RemovalNodeDescription;
  }

  @Override
  public RemovalNodeDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (RemovalNodeDescriptionParameters) graphDescription.getParameters();
    this.nodeType = parameters.getNodeType();
    return this;
  }

  @Override
  public RemovalNodeDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (RemovalNodeDescriptionParameters) graphDescription.getParameters();
    return new RemovalNodeDescription(
        new RemovalNodeDescriptionParameters(parameters.getNodeType()), newChildren);
  }

  @Override
  public RemovalNodeDescriptionBuilder getCopy() {
    var builder = new RemovalNodeDescriptionBuilder();
    builder.setNodeType(this.nodeType)
        .setChildren(this.getChildren().stream().map(SpecificGraphDescriptionBuilder::getCopy)
            .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  public RemovalNodeDescriptionBuilder setNodeType(String nodeType) {
    this.nodeType = nodeType;
    return this;
  }
}
