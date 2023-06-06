package ai.stapi.graphoperations.graphLoader.search.exceptions;

import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOptionParameters;

public class CannotCreateFilterOption extends RuntimeException {

  private CannotCreateFilterOption(String becauseMessage) {
    super("Cannot create FilterOption, " + becauseMessage);
  }

  public static CannotCreateFilterOption becauseInvalidLeafFilterStrategyProvided(
      String filterStrategy) {
    return new CannotCreateFilterOption(
        "because invalid leaf filter strategy provided. " +
            "Strategy: " + filterStrategy
    );
  }

  public static CannotCreateFilterOption becauseInvalidListFilterStrategyProvided(
      String filterStrategy) {
    return new CannotCreateFilterOption(
        "because invalid list filter strategy provided. " +
            "Strategy: " + filterStrategy
    );
  }

  public static CannotCreateFilterOption becauseInvalidLogicalFilterStrategyProvided(
      String filterStrategy) {
    return new CannotCreateFilterOption(
        "because invalid logical filter strategy provided. " +
            "Strategy: " + filterStrategy
    );
  }

  public static CannotCreateFilterOption becauseInvalidFilterOptionParameters(
      String filterStrategy,
      FilterOptionParameters filterOptionParameters
  ) {
    return new CannotCreateFilterOption(
        "because invalid filter option parameters provided. " +
            "\nParameters: " + filterOptionParameters.getClass().getSimpleName() +
            "\nStrategy: " + filterStrategy
    );
  }

  public static CannotCreateFilterOption becauseInvalidFilterOptionFoundWhenMakingCopy(
      FilterOption<?> originalFilter) {
    return new CannotCreateFilterOption(
        "because invalid filter option found when making copy. " +
            "\nFilter option: " + originalFilter.getClass().getSimpleName()
    );
  }
}
