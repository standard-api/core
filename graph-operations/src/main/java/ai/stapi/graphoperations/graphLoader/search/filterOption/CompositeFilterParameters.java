package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilterParameters implements FilterOptionParameters {

  private final List<FilterOption<?>> childFilterOptions;

  private CompositeFilterParameters() {
    this.childFilterOptions = new ArrayList<>();
  }

  public CompositeFilterParameters(List<FilterOption<?>> childFilterOptions) {
    this.childFilterOptions = childFilterOptions;
  }

  public List<FilterOption<?>> getChildFilterOptions() {
    return childFilterOptions;
  }
}
