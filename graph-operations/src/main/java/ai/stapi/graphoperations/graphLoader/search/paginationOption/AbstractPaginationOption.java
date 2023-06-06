package ai.stapi.graphoperations.graphLoader.search.paginationOption;

import ai.stapi.graphoperations.graphLoader.search.AbstractSearchOption;

public abstract class AbstractPaginationOption<ParametersType>
    extends AbstractSearchOption<ParametersType> implements PaginationOption<ParametersType> {

  public static final String OPTION_TYPE = "paging";

  protected AbstractPaginationOption() {
  }

  public AbstractPaginationOption(String strategy, ParametersType parameters) {
    super(AbstractPaginationOption.OPTION_TYPE, strategy, parameters);
  }
}
