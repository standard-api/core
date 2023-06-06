package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.ArrayList;
import java.util.List;

public class ListAttributeDescription extends AbstractAttributeDescription {

  public static final String SERIALIZATION_TYPE = "ListAttributeDescription";
  
  private ListAttributeDescription() {
  }

  public ListAttributeDescription(
      AttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(SERIALIZATION_TYPE, ListAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public ListAttributeDescription(
      AttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, ListAttribute.DATA_STRUCTURE_TYPE, parameters, childDescriptions);
  }

  public ListAttributeDescription(
      String attributeName,
      List<GraphDescription> childDescriptions
  ) {
    super(SERIALIZATION_TYPE, ListAttribute.DATA_STRUCTURE_TYPE, attributeName, childDescriptions);
  }

  public ListAttributeDescription(
      String attributeName
  ) {
    super(SERIALIZATION_TYPE, ListAttribute.DATA_STRUCTURE_TYPE, attributeName, new ArrayList<>());
  }
}
