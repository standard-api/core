package ai.stapi.graphoperations.graphLoader.search.paginationOption;

import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = OffsetPaginationOption.class, name = OffsetPaginationOption.STRATEGY)
})
public interface PaginationOption<ParameterType> extends SearchOption<ParameterType> {

}
