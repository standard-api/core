package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryOffsetPaginationResolver extends AbstractInMemorySearchOptionResolver<OffsetPaginationOption> {

  protected InMemoryOffsetPaginationResolver(
      StructureSchemaFinder structureSchemaFinder
  ) {
    super(structureSchemaFinder);
  }

  @Override
  protected InMemorySearchResolvingContext resolveTyped(
      OffsetPaginationOption option,
      InMemorySearchResolvingContext context
  ) {
    return context.setSearchOption(
        elements -> elements
            .skip(option.getParameters().getOffset())
            .limit(option.getParameters().getLimit())
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof OffsetPaginationOption;
  }
}
