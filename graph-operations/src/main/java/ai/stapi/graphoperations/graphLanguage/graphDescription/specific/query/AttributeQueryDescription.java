package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import java.util.ArrayList;
import java.util.List;

public class AttributeQueryDescription extends AbstractAttributeDescription implements QueryGraphDescription {

  public static final String SERIALIZATION_TYPE = "AttributeQueryDescription";
  public static final String DESCRIBED_STRUCTURE_TYPE = "Irrelevant";

  private AttributeQueryDescription() {
  }

  public AttributeQueryDescription(
      AttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, DESCRIBED_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public AttributeQueryDescription(
      AttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, DESCRIBED_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public AttributeQueryDescription(
      String attributeName,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, DESCRIBED_STRUCTURE_TYPE, attributeName, childDescriptions);
  }

  public AttributeQueryDescription(
      String attributeName
  ) {
    super(SERIALIZATION_TYPE, DESCRIBED_STRUCTURE_TYPE, attributeName, new ArrayList<>());
  }
}
