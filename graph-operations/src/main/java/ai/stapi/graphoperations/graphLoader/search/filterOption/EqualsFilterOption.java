package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import java.util.List;
import java.util.Set;

public class EqualsFilterOption<ValueType> extends AbstractOneValueFilterOption<ValueType> {

  public static final String STRATEGY = "equals";

  private EqualsFilterOption() {
  }

  public EqualsFilterOption(String attributeName, ValueType attributeValue) {
    super(EqualsFilterOption.STRATEGY, attributeName, attributeValue);
  }

  public EqualsFilterOption(PositiveGraphDescription attributePathName, ValueType attributeValue) {
    super(EqualsFilterOption.STRATEGY, attributePathName, attributeValue);
  }

  @Override
  protected List<Class<?>> setAllowedValueTypes() {
    return List.of(
        String.class, 
        Boolean.class, 
        Integer.class, 
        Double.class, 
        Float.class,
        List.class, 
        Set.class
    );
  }

}
