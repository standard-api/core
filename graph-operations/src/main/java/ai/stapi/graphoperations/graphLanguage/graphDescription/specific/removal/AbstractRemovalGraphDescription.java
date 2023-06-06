package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal;

import ai.stapi.graphoperations.graphLanguage.graphDescription.AbstractGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public abstract class AbstractRemovalGraphDescription extends AbstractGraphDescription
    implements RemovalGraphDescription {

  protected AbstractRemovalGraphDescription() {

  }

  public AbstractRemovalGraphDescription(
      String serializationType,
      String traversingType,
      List<GraphDescription> childDescriptions
  ) {
    super(serializationType, traversingType, childDescriptions);
  }

  public AbstractRemovalGraphDescription(
      String serializationType,
      String traversingType,
      GraphDescription... childDescriptions
  ) {
    super(serializationType, traversingType, childDescriptions);
  }

  @Override
  public String getDeclarationType() {
    return GraphDescription.DECLARATION_TYPE;
  }
}
