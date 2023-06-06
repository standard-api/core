package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public class SetAttributeDescription extends AbstractAttributeDescription {

  public static final String SERIALIZATION_TYPE = "SetAttributeDescription";
  
  private SetAttributeDescription() {
  }

  public SetAttributeDescription(
      AttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, SetAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public SetAttributeDescription(
      AttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, SetAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public SetAttributeDescription(
      String attributeName,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, SetAttribute.DATA_STRUCTURE_TYPE, attributeName, childDescriptions);
  }
}
