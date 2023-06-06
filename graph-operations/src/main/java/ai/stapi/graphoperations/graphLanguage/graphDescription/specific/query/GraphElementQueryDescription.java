package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query;

import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;

public interface GraphElementQueryDescription extends QueryGraphDescription {

  SearchQueryParameters getSearchQueryParameters();
}
