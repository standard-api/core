package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLoader.search.AbstractSearchOption;

public abstract class AbstractFilterOption<ParametersType>
    extends AbstractSearchOption<ParametersType> {

  public static final String OPTION_TYPE = "filter";

  protected AbstractFilterOption() {
  }

  public AbstractFilterOption(String strategy, ParametersType parameters) {
    super(AbstractFilterOption.OPTION_TYPE, strategy, parameters);
  }
}
