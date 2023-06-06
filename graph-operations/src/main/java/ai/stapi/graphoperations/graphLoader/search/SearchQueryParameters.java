package ai.stapi.graphoperations.graphLoader.search;


import ai.stapi.graphoperations.graphLoader.search.exceptions.SearchOptionResolverRuntimeException;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.AbstractPaginationOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.PaginationOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AbstractSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SearchQueryParameters {

  public static final OffsetPaginationOption DEFAULT_PAGINATION_OPTION = null;
  private final List<FilterOption<?>> filterOptions;
  private final List<SortOption> sortOptions;

  @Nullable
  private final PaginationOption<?> paginationOption;

  public SearchQueryParameters(
      List<FilterOption<?>> filterOptions,
      List<SortOption> sortOptions,
      @Nullable PaginationOption<?> paginationOption
  ) {
    this.sortOptions = sortOptions;
    this.filterOptions = filterOptions;
    this.paginationOption = paginationOption;
  }

  public SearchQueryParameters(List<FilterOption<?>> filterOptions, List<SortOption> sortOptions) {
    this(
        filterOptions,
        sortOptions,
        SearchQueryParameters.DEFAULT_PAGINATION_OPTION
    );
  }

  public SearchQueryParameters() {
    this(new ArrayList<>(), new ArrayList<>());
  }

  public static SearchQueryParameters from(SearchOption<?>... searchOptions) {
    Map<String, List<SearchOption<?>>> initialReduction = Map.ofEntries(
        Map.entry(AbstractPaginationOption.OPTION_TYPE, new ArrayList<>()),
        Map.entry(AbstractFilterOption.OPTION_TYPE, new ArrayList<>()),
        Map.entry(AbstractSortOption.OPTION_TYPE, new ArrayList<>())
    );
    var optionMap = Arrays.stream(searchOptions)
        .reduce(initialReduction, SearchQueryParameters::categorizeOption,
            SearchQueryParameters::mergeMaps);
    var paginationOptionCount = optionMap.get(AbstractPaginationOption.OPTION_TYPE).size();
    if (paginationOptionCount > 1) {
      throw SearchOptionResolverRuntimeException.becauseSearchQueryParamatersCanOnlyHaveOnePagingOptions();
    }
    var filters = new ArrayList<FilterOption<?>>();

    optionMap.get(AbstractFilterOption.OPTION_TYPE)
        .stream().map(searchOption -> (FilterOption<?>) searchOption)
        .forEach(filters::add);
    var sorts = optionMap.get(AbstractSortOption.OPTION_TYPE).stream()
        .map(searchOption -> (SortOption) searchOption)
        .collect(Collectors.toList());

    PaginationOption<?> paginationOption;
    if (paginationOptionCount == 0) {
      paginationOption = SearchQueryParameters.DEFAULT_PAGINATION_OPTION;
    } else {
      paginationOption = (PaginationOption<?>) optionMap.get(AbstractPaginationOption.OPTION_TYPE).get(0);
    }

    return new SearchQueryParameters(
        filters,
        sorts,
        paginationOption
    );
  }

  public static SearchQueryParametersBuilder builder() {
    return new SearchQueryParametersBuilder();
  }

  @NotNull
  private static Map<String, List<SearchOption<?>>> categorizeOption(
      Map<String, List<SearchOption<?>>> stringSearchOptionHashMap,
      SearchOption<?> searchOption
  ) {
    stringSearchOptionHashMap.get(searchOption.getOptionType()).add(searchOption);
    return stringSearchOptionHashMap;
  }

  @NotNull
  private static Map<String, List<SearchOption<?>>> mergeMaps(
      Map<String, List<SearchOption<?>>> map1,
      Map<String, List<SearchOption<?>>> map2
  ) {
    map1.putAll(map2);
    return map1;
  }

  public List<SortOption> getSortOptions() {
    return sortOptions;
  }

  public List<FilterOption<?>> getFilterOptions() {
    return filterOptions;
  }

  @Nullable
  public PaginationOption<?> getPaginationOption() {
    return paginationOption;
  }
}
