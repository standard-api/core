package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class ContainsFilterOption extends AbstractOneValueFilterOption<String> {

  public static final String STRATEGY = "contains";

  private ContainsFilterOption() {
  }

  public ContainsFilterOption(String attributeName, String attributeValue) {
    super(ContainsFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public ContainsFilterOption(PositiveGraphDescription attributePathName, String attributeValue) {
    super(ContainsFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(String.class);
  }

}
