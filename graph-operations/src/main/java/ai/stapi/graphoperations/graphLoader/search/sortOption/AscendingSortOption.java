package ai.stapi.graphoperations.graphLoader.search.sortOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public class AscendingSortOption extends AbstractSortOption {

  public static final String STRATEGY = "ascending";

  private AscendingSortOption() {
  }

  public AscendingSortOption(String attributeName) {
    super(AscendingSortOption.STRATEGY, attributeName);
  }

  public AscendingSortOption(PositiveGraphDescription attributeNamePath) {
    super(AscendingSortOption.STRATEGY, attributeNamePath);
  }
}
