package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class LowerThanOrEqualsFilterOption<ValueType>
    extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "lower_than_or_equals";

  private LowerThanOrEqualsFilterOption() {
  }

  public LowerThanOrEqualsFilterOption(String attributeName, ValueType attributeValue) {
    super(LowerThanOrEqualsFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public LowerThanOrEqualsFilterOption(PositiveGraphDescription attributePathName,
                                       ValueType attributeValue) {
    super(LowerThanOrEqualsFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(Integer.class, Double.class, Float.class);
  }

}
