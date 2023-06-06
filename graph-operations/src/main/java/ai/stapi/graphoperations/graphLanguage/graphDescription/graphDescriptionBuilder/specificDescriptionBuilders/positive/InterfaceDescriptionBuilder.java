package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.InterfaceGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.InterfaceGraphDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  private String interfaceId;

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof InterfaceGraphDescription;
  }

  @Override
  public PositiveGraphDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return new InterfaceGraphDescription(new InterfaceGraphDescriptionParameters(this.interfaceId),
        children);
  }

  @Override
  public AbstractPositiveDescriptionBuilder setValues(GraphDescription graphDescription) {
    var params = (InterfaceGraphDescriptionParameters) graphDescription.getParameters();
    this.interfaceId = params.getInterfaceId();
    return this;
  }

  @Override
  public PositiveGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (InterfaceGraphDescriptionParameters) graphDescription.getParameters();
    return new InterfaceGraphDescription(
        new InterfaceGraphDescriptionParameters(parameters.getInterfaceId()),
        newChildren
    );
  }

  @Override
  public AbstractPositiveDescriptionBuilder getCopy() {
    var builder = new InterfaceDescriptionBuilder();
    builder.setInterfaceId(this.interfaceId)
        .setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  public AbstractPositiveDescriptionBuilder setInterfaceId(String interfaceId) {
    this.interfaceId = interfaceId;
    return this;
  }
}
