package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalAttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalAttributeGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemovalAttributeDescriptionBuilder extends AbstractRemovalDescriptionBuilder {

  private String attributeName;

  @Override
  public RemovalAttributeGraphDescription build() {
    return new RemovalAttributeGraphDescription(
        new RemovalAttributeDescriptionParameters(attributeName));
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof RemovalAttributeGraphDescription;
  }

  @Override
  public RemovalAttributeDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (RemovalAttributeDescriptionParameters) graphDescription.getParameters();
    this.attributeName = parameters.getAttributeName();
    return this;
  }

  @Override
  public RemovalAttributeGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (RemovalAttributeDescriptionParameters) graphDescription.getParameters();
    return new RemovalAttributeGraphDescription(
        new RemovalAttributeDescriptionParameters(
            parameters.getAttributeName()
        ),
        newChildren
    );
  }

  @Override
  public RemovalAttributeDescriptionBuilder getCopy() {
    var builder = new RemovalAttributeDescriptionBuilder();
    builder.setAttributeName(this.attributeName)
        .setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new))
        );
    return builder;
  }

  public RemovalAttributeDescriptionBuilder setAttributeName(String attributeName) {
    this.attributeName = attributeName;
    return this;
  }
}
