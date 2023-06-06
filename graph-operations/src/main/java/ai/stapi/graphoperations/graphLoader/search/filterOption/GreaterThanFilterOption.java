package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class GreaterThanFilterOption<ValueType> extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "greater_than";

  private GreaterThanFilterOption() {
  }

  public GreaterThanFilterOption(String attributeName, ValueType attributeValue) {
    super(GreaterThanFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public GreaterThanFilterOption(PositiveGraphDescription attributePathName,
                                 ValueType attributeValue) {
    super(GreaterThanFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(Integer.class, Double.class, Float.class);
  }

}
