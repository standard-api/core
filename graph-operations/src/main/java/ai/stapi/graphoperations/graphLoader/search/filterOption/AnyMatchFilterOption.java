package ai.stapi.graphoperations.graphLoader.search.filterOption;

import java.util.ArrayList;
import java.util.List;

public class AnyMatchFilterOption extends AbstractCompositeFilterOption {

  public static final String STRATEGY = "any_match";

  private AnyMatchFilterOption() {
  }

  public AnyMatchFilterOption(AbstractOneValueFilterOption<?> childFilterOption) {
    super(AnyMatchFilterOption.STRATEGY, new ArrayList<>(List.of(childFilterOption)));
  }

  public AbstractOneValueFilterOption<?> getChildFilterOption() {
    return (AbstractOneValueFilterOption<?>) this.getParameters().getChildFilterOptions().get(0);
  }
}
