package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public class NotNullFilterOption extends AbstractLeafFilterOption<NoValueFilterOptionParameters> {

  public static final String STRATEGY = "not_null";

  private NotNullFilterOption() {
  }

  public NotNullFilterOption(String attributeName) {
    super(NotNullFilterOption.STRATEGY, new NoValueFilterOptionParameters(attributeName));
  }

  public NotNullFilterOption(
      PositiveGraphDescription attributePathName
  ) {
    super(NotNullFilterOption.STRATEGY, new NoValueFilterOptionParameters(attributePathName));
  }
  
}
