package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.List;

public abstract class AbstractAttributeDescription extends AbstractPositiveGraphDescription {

  private AttributeDescriptionParameters parameters;
  private String describedStructureType;

  protected AbstractAttributeDescription() {
  }

  protected AbstractAttributeDescription(
      String serializationType,
      String describedStructureType,
      AttributeDescriptionParameters parameters,
      GraphDescription... childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.ATTRIBUTE_TYPE, childDescriptions);
    this.parameters = parameters;
    this.describedStructureType = describedStructureType;
  }

  protected AbstractAttributeDescription(
      String serializationType,
      String describedStructureType,
      AttributeDescriptionParameters parameters,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.ATTRIBUTE_TYPE, childDescriptions);
    this.describedStructureType = describedStructureType;
    this.parameters = parameters;
  }

  protected AbstractAttributeDescription(
      String serializationType,
      String describedStructureType,
      String attributeName,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.ATTRIBUTE_TYPE, childDescriptions);
    this.describedStructureType = describedStructureType;
    this.parameters = new AttributeDescriptionParameters(attributeName);
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }

  public String getDescribedAttributeStructureType() {
    return describedStructureType;
  }
}
