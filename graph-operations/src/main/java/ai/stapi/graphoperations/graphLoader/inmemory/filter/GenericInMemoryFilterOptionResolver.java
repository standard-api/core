package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import java.util.List;

public class GenericInMemoryFilterOptionResolver {
  
  private final List<InMemoryFilterResolver> inMemoryFilterResolvers;

  public GenericInMemoryFilterOptionResolver(List<InMemoryFilterResolver> inMemoryFilterResolvers) {
    this.inMemoryFilterResolvers = inMemoryFilterResolvers;
  }

  public boolean resolveElement(
      FilterOption<?> option,
      TraversableGraphElement element,
      InMemorySearchResolvingContext context
  ) {
    var supported = this.inMemoryFilterResolvers.stream()
        .filter(resolver -> resolver.supports(option))
        .findFirst()
        .get();
    
    return supported.resolveElement(option, element, context);
  }
  
  public boolean resolveValue(
      AbstractOneValueFilterOption<?> option,
      AttributeValue<?> attributeValue
  ) {
    var supported = this.inMemoryFilterResolvers.stream()
        .filter(InMemoryOneValueFilterResolver.class::isInstance)
        .map(InMemoryOneValueFilterResolver.class::cast)
        .filter(resolver -> resolver.supports(option))
        .findFirst()
        .get();

    return supported.resolveValue(option, attributeValue);
  }
}
