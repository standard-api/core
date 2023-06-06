package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.ArrayList;
import java.util.List;

public class NotFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "not";

  private NotFilterOption() {
  }

  public NotFilterOption(FilterOption<?> childFilterOption) {
    super(NotFilterOption.STRATEGY, new ArrayList<>(List.of(childFilterOption)));
  }
  
  public FilterOption<?> getChildFilterOption() {
    return this.getParameters().getChildFilterOptions().get(0);
  }
}
