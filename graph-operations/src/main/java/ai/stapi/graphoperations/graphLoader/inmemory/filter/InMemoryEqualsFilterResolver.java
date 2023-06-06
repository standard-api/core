package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class InMemoryEqualsFilterResolver extends InMemoryOneValueFilterResolver {

  protected InMemoryEqualsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder, inMemoryGraphLoader);
  }

  @Override
  public boolean resolveValue(AbstractOneValueFilterOption<?> option, AttributeValue<?> actualValue) {
    if (option.hasListValue()) {
      return false;
    }
    if (option.hasSetValue()) {
      return false;
    }
    return option.getValueAsAttributeValue().equals(actualValue);
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, List<AttributeValue<?>> actualValue) {
    if (option.hasListValue()) {
      return option.getValueAsListAttributeValue().equals(actualValue);
    }
    return false;
  }

  @Override
  protected boolean resolveValue(AbstractOneValueFilterOption<?> option, Set<AttributeValue<?>> actualValue) {
    if (option.hasSetValue()) {
      return option.getValueAsSetAttributeValue().equals(actualValue);
    }
    return false;
  }

  @Override
  public boolean supports(SearchOption<?> option) {
    return option instanceof EqualsFilterOption<?>;
  }
}
