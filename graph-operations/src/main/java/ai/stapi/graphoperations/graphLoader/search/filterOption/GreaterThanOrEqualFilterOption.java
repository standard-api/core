package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class GreaterThanOrEqualFilterOption<ValueType>
    extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "greater_than_or_equals";

  private GreaterThanOrEqualFilterOption() {
  }

  public GreaterThanOrEqualFilterOption(String attributeName, ValueType attributeValue) {
    super(GreaterThanOrEqualFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public GreaterThanOrEqualFilterOption(PositiveGraphDescription attributePathName,
                                        ValueType attributeValue) {
    super(GreaterThanOrEqualFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(Integer.class, Double.class, Float.class);
  }

}
