package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.CollectionComparisonOperator;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LeafFilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import java.util.Set;

public abstract class InMemoryLeafFilterResolver extends InMemoryFilterResolver {

  private final InMemoryGraphLoader inMemoryGraphLoader;

  protected InMemoryLeafFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder);
    this.inMemoryGraphLoader = inMemoryGraphLoader;
  }

  public abstract boolean resolve(LeafFilterOption<?> option, AttributeValue<?> actualValue);

  protected abstract boolean resolve(LeafFilterOption<?> option, List<AttributeValue<?>> actualValue);

  protected abstract boolean resolve(LeafFilterOption<?> option, Set<AttributeValue<?>> actualValue);

  @Override
  public boolean resolveElement(
      FilterOption<?> option,
      TraversableGraphElement element,
      InMemorySearchResolvingContext context
  ) {
    var leafFilterOption = (LeafFilterOption<?>) option;
    if (leafFilterOption.isLeaf()) {
      if (leafFilterOption.isDescribingAttribute()) {
        var attributeName = leafFilterOption.getParameters().getAttributeName();
        if (!element.hasAttribute(attributeName)) {
          return this.resolve(leafFilterOption, (AttributeValue<?>) null);
        }
        var attribute = element.getAttribute(attributeName);
        return this.resolveAttribute(leafFilterOption, attribute);
      } else {
        return this.resolve(leafFilterOption, new IdAttributeValue(element.getId().getId()));
      }
    } else {
      var attributeNamePath = leafFilterOption.getParameters().getAttributeNamePath();
      var copy = new GraphDescriptionBuilder().copyWithNewChildren(
          context.getLastDescription(),
          attributeNamePath
      );
      var filterValue = this.inMemoryGraphLoader.getSearchOptionAttributeValue(
          element.getId(),
          (PositiveGraphDescription) copy
      );
      var collectionComparisonSchema = this.createRelationshipStructureSchema(
          attributeNamePath,
          element.getType()
      );
      return this.resolveFilterValue(leafFilterOption, filterValue, collectionComparisonSchema);
    }
  }

  private boolean resolveAttribute(LeafFilterOption<?> leafFilterOption, Attribute<?> attribute) {
    if (attribute instanceof ListAttribute listAttribute) {
      return this.resolve(leafFilterOption, listAttribute.getBoxedValues());
    }
    if (attribute instanceof SetAttribute setAttribute) {
      return this.resolve(leafFilterOption, setAttribute.getBoxedValues());
    }
    return this.resolve(leafFilterOption, ((LeafAttribute<?, ?>) attribute).getBoxedValue());
  }

  private boolean resolveFilterValue(
      LeafFilterOption<?> filterOption,
      Object filterValue,
      List<SchemaAndCollectionComparisonOperator> collectionComparisonSchema
  ) {
    if (filterValue == null) {
      return this.resolve(filterOption, (AttributeValue<?>) null);
    }
    if (collectionComparisonSchema.isEmpty()) {
      var attribute = (Attribute<?>) filterValue;
      return this.resolveAttribute(filterOption, attribute);
    }
    var currentComparison = collectionComparisonSchema.get(0);
    var list = (List<Object>) filterValue;
    var len = collectionComparisonSchema.size();
    var restOfCollectionSchema = collectionComparisonSchema.subList(1, len);
    if (currentComparison.operator().equals(CollectionComparisonOperator.ALL)) {
      return list.stream().allMatch(value -> this.resolveFilterValue(filterOption, value, restOfCollectionSchema));
    } else if (currentComparison.operator().equals(CollectionComparisonOperator.NONE)) {
      return list.stream().noneMatch(value -> this.resolveFilterValue(filterOption, value, restOfCollectionSchema));
    } else {
      return list.stream().anyMatch(value -> this.resolveFilterValue(filterOption, value, restOfCollectionSchema));
    }
  }
}
