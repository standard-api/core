package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AndFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "and";

  private AndFilterOption() {
  }

  public AndFilterOption(FilterOption<?>... childFilterOptions) {
    this(Arrays.stream(childFilterOptions).collect(Collectors.toList()));
  }

  public AndFilterOption(List<FilterOption<?>> childFilterOptions) {
    super(AndFilterOption.STRATEGY, childFilterOptions);
  }
}
