package ai.stapi.graphoperations.graphLoader.search;




public abstract class AbstractSearchOption<ParametersType> implements SearchOption<ParametersType> {

  private String optionType;

  private String strategy;

  private ParametersType parameters;

  protected AbstractSearchOption() {
  }

  protected AbstractSearchOption(
      String optionType,
      String strategy,
      ParametersType parameters
  ) {
    this.optionType = optionType;
    this.strategy = strategy;
    this.parameters = parameters;
  }

  @Override
  public String getOptionType() {
    return this.optionType;
  }

  @Override
  public String getStrategy() {
    return this.strategy;
  }

  @Override
  public ParametersType getParameters() {
    return this.parameters;
  }
}
