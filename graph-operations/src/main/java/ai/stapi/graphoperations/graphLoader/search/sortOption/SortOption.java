package ai.stapi.graphoperations.graphLoader.search.sortOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AscendingSortOption.class, name = AscendingSortOption.STRATEGY),
    @JsonSubTypes.Type(value = DescendingSortOption.class, name = DescendingSortOption.STRATEGY)
})
public interface SortOption extends SearchOption<PositiveGraphDescription> {

}
