package ai.stapi.graphoperations.graphLanguage.graphDescription;

import ai.stapi.graphoperations.graphLanguage.AbstractGraphDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGraphDescription extends AbstractGraphDeclaration
    implements GraphDescription {

  protected List<GraphDescription> childGraphDescriptions;
  protected String traversingType;

  protected AbstractGraphDescription() {
  }

  protected AbstractGraphDescription(
      String deserializationType,
      String traversingType,
      List<GraphDescription> childGraphDescriptions
  ) {
    super(deserializationType);
    this.traversingType = traversingType;
    this.childGraphDescriptions = childGraphDescriptions;
  }

  protected AbstractGraphDescription(
      String deserializationType,
      String traversingType,
      GraphDescription... childGraphDescriptions
  ) {
    super(deserializationType);
    this.traversingType = traversingType;
    this.childGraphDescriptions =
        Arrays.stream(childGraphDescriptions).collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public String getDeclarationType() {
    return GraphDescription.DECLARATION_TYPE;
  }

  @Override
  public String getGraphTraversingType() {
    return traversingType;
  }

  @Override
  public List<GraphDescription> getChildGraphDescriptions() {
    return this.childGraphDescriptions;
  }
}
