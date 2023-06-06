package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LeafFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public abstract class InMemoryOneValueFilterResolver extends InMemoryLeafFilterResolver {

  protected InMemoryOneValueFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }
  
  public abstract boolean resolveValue(AbstractOneValueFilterOption<?> option, AttributeValue<?> actualValue);

  protected abstract boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue);

  protected abstract boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue);
  

  @Override
  public boolean resolve(LeafFilterOption<?> option, AttributeValue<?> actualValue) {
    if (actualValue == null) {
      return false;
    }
    return this.resolveValue((AbstractOneValueFilterOption<?>) option, actualValue);
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    if (actualValue == null) {
      return false;
    }
    return this.resolveValue((AbstractOneValueFilterOption<?>) option, actualValue);
  }

  @Override
  protected boolean resolve(LeafFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    if (actualValue == null) {
      return false;
    }
    return this.resolveValue((AbstractOneValueFilterOption<?>) option, actualValue);
  }
}
