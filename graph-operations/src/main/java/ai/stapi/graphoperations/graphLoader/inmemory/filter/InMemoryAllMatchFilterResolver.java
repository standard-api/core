package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AllMatchFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InMemoryAllMatchFilterResolver extends InMemoryArrayComparisonFilterResolver {


  protected InMemoryAllMatchFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, genericInMemoryFilterOptionResolver, inMemoryGraphLoader);
  }

  @Override
  protected boolean resolveSetAttribute(AbstractOneValueFilterOption<?> childFilter, SetAttribute setAttribute) {
    return setAttribute.getBoxedValues().stream().allMatch(
        attributeValue -> this.genericInMemoryFilterOptionResolver.resolveValue(childFilter, attributeValue)
    );
  }

  @Override
  protected boolean resolveListAttribute(AbstractOneValueFilterOption<?> childFilter, ListAttribute listAttribute) {
    return listAttribute.getBoxedValues().stream().allMatch(
        attributeValue -> this.genericInMemoryFilterOptionResolver.resolveValue(childFilter, attributeValue)
    );
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof AllMatchFilterOption;
  }
}
