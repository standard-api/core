package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public class ReferenceGraphDescription extends AbstractNodeDescription {

  public static final String SERIALIZATION_TYPE = "ReferenceGraphDescription";

  public ReferenceGraphDescription(
      NodeDescriptionParameters parameters,
      List<GraphDescription> graphDescriptions
  ) {
    super(SERIALIZATION_TYPE, parameters, graphDescriptions);
  }

  public ReferenceGraphDescription(NodeDescriptionParameters parameters) {
    super(SERIALIZATION_TYPE, parameters);
  }

  public ReferenceGraphDescription(String structureSerializationType) {
    super(SERIALIZATION_TYPE, new NodeDescriptionParameters(structureSerializationType));
  }
}
