package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;

public class StartsWithFilterOption extends AbstractOneValueFilterOption<String> {

  public static final String STRATEGY = "starts_with";

  private StartsWithFilterOption() {
  }

  public StartsWithFilterOption(String attributeName, String attributeValue) {
    super(StartsWithFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public StartsWithFilterOption(PositiveGraphDescription attributePathName, String attributeValue) {
    super(StartsWithFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(String.class);
  }

}
