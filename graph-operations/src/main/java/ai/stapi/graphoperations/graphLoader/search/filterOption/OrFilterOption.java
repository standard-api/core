package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "or";

  private OrFilterOption() {
  }

  public OrFilterOption(FilterOption<?>... filterOptions) {
    this(Arrays.stream(filterOptions).collect(Collectors.toList()));
  }

  public OrFilterOption(List<FilterOption<?>> childFilterOptions) {
    super(OrFilterOption.STRATEGY, childFilterOptions);
  }
}
