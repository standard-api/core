package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class UuidIdentityDescription extends AbstractPositiveGraphDescription {

  public static final String SERIALIZATION_TYPE = "UuidIdentityDescription";
  private UuidIdentityDescriptionParameters parameters;

  public UuidIdentityDescription() {
    super(
        SERIALIZATION_TYPE,
        GraphBaseTypes.VALUE_TYPE,
        new ArrayList<>()
    );
    this.parameters = new UuidIdentityDescriptionParameters();
  }

  public UuidIdentityDescription(List<GraphDescription> children) {
    super(
        SERIALIZATION_TYPE,
        GraphBaseTypes.VALUE_TYPE,
        children
    );
    this.parameters = new UuidIdentityDescriptionParameters();
  }

  public UuidIdentityDescription(GraphDescription... children) {
    super(
        SERIALIZATION_TYPE,
        GraphBaseTypes.VALUE_TYPE,
        children
    );
    this.parameters = new UuidIdentityDescriptionParameters();
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return this.parameters;
  }
}
