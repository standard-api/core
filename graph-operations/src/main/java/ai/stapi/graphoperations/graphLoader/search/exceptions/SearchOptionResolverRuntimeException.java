package ai.stapi.graphoperations.graphLoader.search.exceptions;

public class SearchOptionResolverRuntimeException extends RuntimeException {

  private SearchOptionResolverRuntimeException(String becauseMessage) {
    super("Search option resolver threw exception, " + becauseMessage);
  }

  public static SearchOptionResolverRuntimeException becauseSearchQueryParamatersCanOnlyHaveOnePagingOptions() {
    return new SearchOptionResolverRuntimeException(
        "because Search Query Paramaters Can Only Have One Paging Options"
    );
  }

  public static SearchOptionResolverRuntimeException becauseGraphDescriptionInsideSortOptionMustBeSinglePath() {
    return new SearchOptionResolverRuntimeException(
        "because GraphDescription inside Sort Option must be single path."
    );
  }

  public static SearchOptionResolverRuntimeException becauseGraphDescriptionInsideSortOptionMustEndWithValueDescription() {
    return new SearchOptionResolverRuntimeException(
        "because GraphDescription inside Sort Option must end with value description."
    );
  }

  public static SearchOptionResolverRuntimeException becauseGraphDescriptionInsideOneValueFilterOptionMustBeSinglePath() {
    return new SearchOptionResolverRuntimeException(
        "because GraphDescription inside OneValueFilterOption must be single path."
    );
  }

  public static SearchOptionResolverRuntimeException becauseGraphDescriptionInsideOneValueFilterOptionMustEndWithValueDescription() {
    return new SearchOptionResolverRuntimeException(
        "because GraphDescription inside OneValueFilterOption must end with value description."
    );
  }

}
