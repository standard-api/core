package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EqualsFilterOption.class, name = EqualsFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = ContainsFilterOption.class, name = ContainsFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = GreaterThanFilterOption.class, name = GreaterThanFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = AnyMatchFilterOption.class, name = AnyMatchFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = AndFilterOption.class, name = AndFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = OrFilterOption.class, name = OrFilterOption.STRATEGY),
    @JsonSubTypes.Type(value = NotFilterOption.class, name = NotFilterOption.STRATEGY),
})
public interface FilterOption<ParametersType> extends SearchOption<ParametersType> {

}
