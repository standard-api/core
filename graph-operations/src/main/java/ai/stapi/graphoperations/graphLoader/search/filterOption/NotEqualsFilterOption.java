package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;
import java.util.Set;

public class NotEqualsFilterOption<ValueType> extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "not_equals";

  private NotEqualsFilterOption() {
  }

  public NotEqualsFilterOption(String attributeName, ValueType attributeValue) {
    super(NotEqualsFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public NotEqualsFilterOption(
      PositiveGraphDescription attributePathName,
      ValueType attributeValue
  ) {
    super(NotEqualsFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(
        String.class, Boolean.class, Integer.class, Double.class, Float.class, List.class, Set.class);
  }
}
