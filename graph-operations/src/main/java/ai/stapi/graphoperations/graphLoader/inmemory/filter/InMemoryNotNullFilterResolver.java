package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LeafFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotNullFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InMemoryNotNullFilterResolver extends InMemoryLeafFilterResolver {

  protected InMemoryNotNullFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolve(LeafFilterOption<?> option, AttributeValue<?> actualValue) {
    return actualValue != null;
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    return actualValue != null;
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    return actualValue != null;
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof NotNullFilterOption;
  }
}
