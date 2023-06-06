package ai.stapi.graphoperations.graphLoader.search;

import ai.stapi.graphoperations.graphLoader.search.exceptions.SearchOptionNotSupportedByExactlyOneResolver;
import java.util.List;

public abstract class AbstractGenericSearchOptionResolver<R extends ResolvedQueryPart>
    implements GenericSearchOptionResolver<R> {

  protected final List<SearchOptionResolver<R>> searchOptionResolvers;

  protected AbstractGenericSearchOptionResolver(List<SearchOptionResolver<R>> searchOptionResolvers) {
    this.searchOptionResolvers = searchOptionResolvers;
  }

  @Override
  public R resolve(SearchOption<?> option, SearchResolvingContext context) {
    var supporting = this.searchOptionResolvers.stream()
        .filter(resolver -> resolver.supports(option))
        .toList();
    if (supporting.size() != 1) {
      throw new SearchOptionNotSupportedByExactlyOneResolver(supporting.size(), option);
    }
    return supporting.get(0).resolve(option, context);
  }
}
