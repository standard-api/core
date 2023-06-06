package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescriptionParameters;
import java.util.List;

public abstract class AbstractAttributeValueDescription extends AbstractPositiveGraphDescription {

  private AttributeValueDescriptionParameters parameters;
  private String describedAttributeDataTypeId;

  protected AbstractAttributeValueDescription() {
  }

  protected AbstractAttributeValueDescription(
      String serializationType,
      String describedAttributeDataTypeId,
      GraphDescription... childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.VALUE_TYPE, childDescriptions);
    this.describedAttributeDataTypeId = describedAttributeDataTypeId;
  }

  protected AbstractAttributeValueDescription(
      String serializationType,
      String describedAttributeDataTypeId,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, GraphBaseTypes.VALUE_TYPE, childDescriptions);
    this.describedAttributeDataTypeId = describedAttributeDataTypeId;
    this.parameters = new AttributeValueDescriptionParameters();
  }

  @Override
  public GraphDescriptionParameters getParameters() {
    return parameters;
  }

  public String getDescribedAttributeDataTypeId() {
    return describedAttributeDataTypeId;
  }
}
