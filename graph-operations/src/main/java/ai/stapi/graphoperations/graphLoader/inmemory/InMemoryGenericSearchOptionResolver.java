package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graphoperations.graphLoader.search.AbstractGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.search.SearchOptionResolver;
import java.util.List;

public class InMemoryGenericSearchOptionResolver
    extends AbstractGenericSearchOptionResolver<InMemorySearchResolvingContext> {

  public InMemoryGenericSearchOptionResolver(
      List<SearchOptionResolver<InMemorySearchResolvingContext>> searchOptionResolvers
  ) {
    super(searchOptionResolvers);
  }
}
