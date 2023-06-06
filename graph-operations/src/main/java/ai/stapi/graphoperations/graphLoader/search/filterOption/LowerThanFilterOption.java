package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class LowerThanFilterOption<ValueType> extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "lower_than";

  private LowerThanFilterOption() {
  }

  public LowerThanFilterOption(String attributeName, ValueType attributeValue) {
    super(LowerThanFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public LowerThanFilterOption(PositiveGraphDescription attributePathName,
                               ValueType attributeValue) {
    super(LowerThanFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(Integer.class, Double.class, Float.class);
  }

}
