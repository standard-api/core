package ai.stapi.graphoperations.graphLoader.inmemory.filter;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.CollectionComparisonOperator;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractCompositeFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;

public abstract class InMemoryArrayComparisonFilterResolver extends InMemoryFilterResolver {
  protected final GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver;
  private final InMemoryGraphLoader inMemoryGraphLoader;

  protected InMemoryArrayComparisonFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder);
    this.genericInMemoryFilterOptionResolver = genericInMemoryFilterOptionResolver;
    this.inMemoryGraphLoader = inMemoryGraphLoader;
  }


  protected abstract boolean resolveListAttribute(
      AbstractOneValueFilterOption<?> childFilter,
      ListAttribute listAttribute
  );

  protected abstract boolean resolveSetAttribute(
      AbstractOneValueFilterOption<?> childFilter,
      SetAttribute setAttribute
  );

  @Override
  public boolean resolveElement(
      FilterOption<?> option, 
      TraversableGraphElement element,
      InMemorySearchResolvingContext context
  ) {
    var compositeFilterOption = (AbstractCompositeFilterOption) option;
    var childFilter = (AbstractOneValueFilterOption<?>) compositeFilterOption.getParameters()
        .getChildFilterOptions()
        .get(0);
    
    if (childFilter.isLeaf() && childFilter.isDescribingAttribute()) {
        var attributeName = childFilter.getParameters().getAttributeName();
        var attribute = element.getAttribute(attributeName);
        if (attribute instanceof ListAttribute listAttribute) {
          return this.resolveListAttribute(childFilter, listAttribute);
        }
        if (attribute instanceof SetAttribute setAttribute) {
          return this.resolveSetAttribute(childFilter, setAttribute);
        }
    }
    var attributeNamePath = childFilter.getParameters().getAttributeNamePath();
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
    return this.resolveFilterValue(childFilter, filterValue, collectionComparisonSchema);
  }

  private boolean resolveFilterValue(
      AbstractOneValueFilterOption<?> filterOption,
      Object filterValue,
      List<SchemaAndCollectionComparisonOperator> collectionComparisonSchema
  ) {
    if (filterValue == null) {
      return false;
    }
    if (collectionComparisonSchema.isEmpty()) {
      var attribute = (Attribute<?>) filterValue;
      if (attribute instanceof ListAttribute listAttribute) {
        return this.resolveListAttribute(filterOption, listAttribute);
      }
      if (attribute instanceof SetAttribute setAttribute) {
        return this.resolveSetAttribute(filterOption, setAttribute);
      }
      return false;
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
