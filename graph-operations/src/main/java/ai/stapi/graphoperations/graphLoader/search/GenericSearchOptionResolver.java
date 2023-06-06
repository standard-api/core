package ai.stapi.graphoperations.graphLoader.search;

public interface GenericSearchOptionResolver<R extends ResolvedQueryPart> {

  R resolve(SearchOption<?> option, SearchResolvingContext context);
}
