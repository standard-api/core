package ai.stapi.graphoperations.graphLoader.search;

public interface SearchOptionResolver<R extends ResolvedQueryPart> {

  boolean supports(SearchOption<?> option);

  R resolve(SearchOption<?> option, SearchResolvingContext context);
}
