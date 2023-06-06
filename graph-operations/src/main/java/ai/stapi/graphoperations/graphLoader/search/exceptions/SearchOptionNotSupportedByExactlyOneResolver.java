package ai.stapi.graphoperations.graphLoader.search.exceptions;

import ai.stapi.graphoperations.graphLoader.search.SearchOption;

public class SearchOptionNotSupportedByExactlyOneResolver extends RuntimeException {

  public SearchOptionNotSupportedByExactlyOneResolver(int size, SearchOption<?> searchOption) {
    super(
        String.format(
            "Search Option Of type: \"%s\" is supported by %d resolvers. But it should be exactly one.",
            searchOption.getClass().getCanonicalName(),
            size
        )
    );
  }
}
