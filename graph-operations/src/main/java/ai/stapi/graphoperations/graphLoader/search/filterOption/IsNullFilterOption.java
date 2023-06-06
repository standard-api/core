package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public class IsNullFilterOption extends AbstractLeafFilterOption<NoValueFilterOptionParameters> {

  public static final String STRATEGY = "is_null";

  private IsNullFilterOption() {
  }

  public IsNullFilterOption(String attributeName) {
    super(IsNullFilterOption.STRATEGY, new NoValueFilterOptionParameters(attributeName));
  }

  public IsNullFilterOption(PositiveGraphDescription attributePathName) {
    super(IsNullFilterOption.STRATEGY, new NoValueFilterOptionParameters(attributePathName));
  }
  
}
