package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class EndsWithFilterOption extends AbstractOneValueFilterOption<String> {

  public static final String STRATEGY = "ends_with";

  private EndsWithFilterOption() {
  }

  public EndsWithFilterOption(String attributeName, String attributeValue) {
    super(EndsWithFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public EndsWithFilterOption(PositiveGraphDescription attributePathName, String attributeValue) {
    super(EndsWithFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(String.class);
  }

}
