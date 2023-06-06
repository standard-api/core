package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.AbstractGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public abstract class AbstractPositiveGraphDescription extends AbstractGraphDescription
    implements PositiveGraphDescription {

  protected AbstractPositiveGraphDescription() {
  }

  public AbstractPositiveGraphDescription(
      String serializationType,
      String traversingType,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, traversingType, childDescriptions);
  }

  public AbstractPositiveGraphDescription(
      String serializationType,
      String traversingType,
      GraphDescription... childDescriptions
  ) {
    super(serializationType, traversingType, childDescriptions);
  }

  @Override
  public String getDeclarationType() {
    return PositiveGraphDescription.DECLARATION_TYPE;
  }
}
