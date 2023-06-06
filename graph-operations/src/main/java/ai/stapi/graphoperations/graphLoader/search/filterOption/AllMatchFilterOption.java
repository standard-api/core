package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.ArrayList;
import java.util.List;

public class AllMatchFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "all_match";

  private AllMatchFilterOption() {
  }

  public AllMatchFilterOption(AbstractOneValueFilterOption<?> childFilterOption) {
    super(AllMatchFilterOption.STRATEGY, new ArrayList<>(List.of(childFilterOption)));
  }

  public AbstractOneValueFilterOption<?> getChildFilterOption() {
    return (AbstractOneValueFilterOption<?>) this.getParameters().getChildFilterOptions().get(0);
  }
}
