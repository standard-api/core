package ai.stapi.graphoperations.graphLoader.search.sortOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public class DescendingSortOption extends AbstractSortOption {

  public static final String STRATEGY = "descending";

  private DescendingSortOption() {
  }

  public DescendingSortOption(String attributeName) {
    super(DescendingSortOption.STRATEGY, attributeName);
  }

  public DescendingSortOption(PositiveGraphDescription attributeNamePath) {
    super(DescendingSortOption.STRATEGY, attributeNamePath);
  }
}
