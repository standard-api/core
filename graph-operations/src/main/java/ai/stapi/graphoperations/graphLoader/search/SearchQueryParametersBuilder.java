package ai.stapi.graphoperations.graphLoader.search;

import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.PaginationOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import java.util.ArrayList;
import java.util.List;

public class SearchQueryParametersBuilder {

  private final List<FilterOption<?>> filterOptions;
  private final List<SortOption> sortOptions;
  private PaginationOption<?> paginationOption;

  public SearchQueryParametersBuilder() {
    this.filterOptions = new ArrayList<>();
    this.sortOptions = new ArrayList<>();
    this.paginationOption = SearchQueryParameters.DEFAULT_PAGINATION_OPTION;
  }

  public SearchQueryParametersBuilder addFilterOption(FilterOption<?> filterOption) {
    this.filterOptions.add(filterOption);
    return this;
  }

  public SearchQueryParametersBuilder addFilterOptions(List<FilterOption<?>> filterOptions) {
    filterOptions.forEach(this::addFilterOption);
    return this;
  }

  public SearchQueryParametersBuilder addSortOption(SortOption sortOption) {
    this.sortOptions.add(sortOption);
    return this;
  }

  public SearchQueryParametersBuilder addSortOptions(List<SortOption> sortOptions) {
    sortOptions.forEach(this::addSortOption);
    return this;
  }

  public SearchQueryParametersBuilder setPaginationOption(PaginationOption<?> paginationOption) {
    this.paginationOption = paginationOption;
    return this;
  }

  public SearchQueryParameters build() {
    return new SearchQueryParameters(filterOptions, sortOptions, paginationOption);
  }
}