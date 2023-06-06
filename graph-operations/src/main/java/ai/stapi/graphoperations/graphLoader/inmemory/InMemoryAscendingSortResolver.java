package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AscendingSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InMemoryAscendingSortResolver extends InMemorySortResolver {

  public static final String GREATEST_VALUE = "\uffff";

  protected InMemoryAscendingSortResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  protected InMemorySearchResolvingContext resolveTyped(
      SortOption option,
      InMemorySearchResolvingContext context
  ) {
    var ascending = (AscendingSortOption) option;
    return context.setAscSort(
        graphElement -> this.getSortingValue(
            ascending,
            graphElement,
            new StringAttributeValue(GREATEST_VALUE),
            context
        )
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof AscendingSortOption;
  }
}
