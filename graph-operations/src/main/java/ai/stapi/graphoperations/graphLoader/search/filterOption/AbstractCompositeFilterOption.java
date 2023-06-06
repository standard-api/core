package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.List;

public abstract class AbstractCompositeFilterOption
    extends AbstractFilterOption<CompositeFilterParameters>
    implements CompositeFilterOption {

  protected AbstractCompositeFilterOption() {
  }

  public AbstractCompositeFilterOption(String operation, List<FilterOption<?>> childFilterOptions) {
    super(operation, new CompositeFilterParameters(childFilterOptions));
  }
}
