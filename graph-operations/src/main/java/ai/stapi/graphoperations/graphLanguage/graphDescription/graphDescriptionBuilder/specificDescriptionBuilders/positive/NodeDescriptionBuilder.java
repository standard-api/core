package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NodeDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  private String nodeType;

  @Override
  public NodeDescriptionBuilder getCopy() {
    var builder = new NodeDescriptionBuilder();
    builder.setNodeType(this.nodeType)
        .setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  @Override
  public NodeDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return new NodeDescription(new NodeDescriptionParameters(nodeType), children);
  }

  @Override
  public AbstractNodeDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    if (graphDescription instanceof NodeQueryGraphDescription nodeQueryGraphDescription) {
      return new NodeQueryGraphDescription(
          (NodeDescriptionParameters) graphDescription.getParameters(),
          nodeQueryGraphDescription.getSearchQueryParameters(), newChildren
      );
    } else {
      return new NodeDescription((NodeDescriptionParameters) graphDescription.getParameters(),
          newChildren);
    }

  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof AbstractNodeDescription;
  }

  @Override
  public NodeDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (NodeDescriptionParameters) graphDescription.getParameters();
    this.nodeType = parameters.getNodeType();
    return this;
  }

  public String getNodeType() {
    return nodeType;
  }

  public NodeDescriptionBuilder setNodeType(String nodeType) {
    this.nodeType = nodeType;
    return this;
  }
}
