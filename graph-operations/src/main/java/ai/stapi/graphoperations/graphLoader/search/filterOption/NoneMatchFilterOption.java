package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.ArrayList;
import java.util.List;

public class NoneMatchFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "none_match";

  private NoneMatchFilterOption() {
  }

  public NoneMatchFilterOption(AbstractOneValueFilterOption<?> childFilterOption) {
    super(NoneMatchFilterOption.STRATEGY, new ArrayList<>(List.of(childFilterOption)));
  }

  public AbstractOneValueFilterOption<?> getChildFilterOption() {
    return (AbstractOneValueFilterOption<?>) this.getParameters().getChildFilterOptions().get(0);
  }
}
