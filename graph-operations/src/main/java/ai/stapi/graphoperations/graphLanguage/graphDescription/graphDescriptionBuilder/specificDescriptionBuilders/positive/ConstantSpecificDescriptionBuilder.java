package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConstantSpecificDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  private Object value;

  @Override
  public ConstantDescription build() {
    return new ConstantDescription(new ConstantDescriptionParameters(value));
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof ConstantDescription;
  }

  @Override
  public ConstantSpecificDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (ConstantDescriptionParameters) graphDescription.getParameters();
    this.value = parameters.getValue();
    return this;
  }

  @Override
  public ConstantDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    var parameters = (ConstantDescriptionParameters) graphDescription;
    return new ConstantDescription(new ConstantDescriptionParameters(parameters.getValue()));
  }

  @Override
  public ConstantSpecificDescriptionBuilder getCopy() {
    var builder = new ConstantSpecificDescriptionBuilder();
    builder.setValue(this.value)
        .setChildren(
            this.getChildren().stream()
                .map(SpecificGraphDescriptionBuilder::getCopy)
                .collect(Collectors.toCollection(ArrayList::new)));
    return builder;
  }

  public ConstantSpecificDescriptionBuilder setValue(Object value) {
    this.value = value;
    return this;
  }
}
