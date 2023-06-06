package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class LeafAttributeDescription extends AbstractAttributeDescription {

  public static final String SERIALIZATION_TYPE = "LeafAttributeDescription";
  
  private LeafAttributeDescription() {
  }

  public LeafAttributeDescription(
      AttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, LeafAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public LeafAttributeDescription(
      AttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, LeafAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public LeafAttributeDescription(
      String attributeName,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, LeafAttribute.DATA_STRUCTURE_TYPE, attributeName, childDescriptions);
  }

  public LeafAttributeDescription(
      String attributeName,
      GraphDescription... childDescriptions
  ) {
    this(new AttributeDescriptionParameters(attributeName), childDescriptions);
  }

  public LeafAttributeDescription(
      String attributeName
  ) {
    super(SERIALIZATION_TYPE, LeafAttribute.DATA_STRUCTURE_TYPE, attributeName, new ArrayList<>());
  }
}
