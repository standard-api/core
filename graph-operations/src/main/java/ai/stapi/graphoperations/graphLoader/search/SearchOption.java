package ai.stapi.graphoperations.graphLoader.search;

import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.AbstractPaginationOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.PaginationOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AbstractSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "optionType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FilterOption.class, name = AbstractFilterOption.OPTION_TYPE),
    @JsonSubTypes.Type(value = SortOption.class, name = AbstractSortOption.OPTION_TYPE),
    @JsonSubTypes.Type(value = PaginationOption.class, name = AbstractPaginationOption.OPTION_TYPE)
})
public interface SearchOption<ParametersType> {

  String getOptionType();

  String getStrategy();

  ParametersType getParameters();
}
