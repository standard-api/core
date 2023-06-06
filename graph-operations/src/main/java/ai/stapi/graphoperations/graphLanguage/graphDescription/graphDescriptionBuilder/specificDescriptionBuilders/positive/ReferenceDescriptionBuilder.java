package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ReferenceGraphDescription;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReferenceDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  private String structureSerializationType;

  @Override
  public ReferenceDescriptionBuilder getCopy() {
    var builder = new ReferenceDescriptionBuilder();
    builder.setStructureSerializationType(this.structureSerializationType)
        .setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  @Override
  public ReferenceGraphDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    return new ReferenceGraphDescription(
        new NodeDescriptionParameters(structureSerializationType),
        children
    );
  }

  @Override
  public ReferenceGraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    return new ReferenceGraphDescription(
        (NodeDescriptionParameters) graphDescription.getParameters(),
        graphDescription.getChildGraphDescriptions()
    );
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof ReferenceGraphDescription;
  }

  @Override
  public ReferenceDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (NodeDescriptionParameters) graphDescription.getParameters();
    this.structureSerializationType = parameters.getNodeType();
    return this;
  }

  public ReferenceDescriptionBuilder setStructureSerializationType(String serializationType) {
    this.structureSerializationType = serializationType;
    return this;
  }
}
