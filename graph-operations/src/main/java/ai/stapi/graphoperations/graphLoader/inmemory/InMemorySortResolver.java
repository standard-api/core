package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AbstractSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.SortOption;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;

public abstract class InMemorySortResolver extends AbstractInMemorySearchOptionResolver<SortOption> {
  
  private final InMemoryGraphLoader inMemoryGraphLoader;
  protected InMemorySortResolver(
      StructureSchemaFinder structureSchemaFinder,
      InMemoryGraphLoader inMemoryGraphLoader
  ) {
    super(structureSchemaFinder);
    this.inMemoryGraphLoader = inMemoryGraphLoader;
  }

  protected AttributeValue<?> getSortingValue(
      AbstractSortOption sortOption,
      TraversableGraphElement graphElement,
      AttributeValue<?> defaultValue,
      InMemorySearchResolvingContext context
  ) {
    if (sortOption.isLeaf()) {
      var attributeName = sortOption.getAttributeName();
      if (!graphElement.hasAttribute(attributeName)) {
        return defaultValue;
      }
      var attribute = graphElement.getAttribute(attributeName);
      return this.resolveAttribute(defaultValue, attribute);
    } else {
      var attributeNamePath = sortOption.getParameters();
      var copy = new GraphDescriptionBuilder().copyWithNewChildren(
          context.getLastDescription(),
          attributeNamePath
      );
      var foundSortValue = this.inMemoryGraphLoader.getSearchOptionAttributeValue(
          graphElement.getId(),
          (PositiveGraphDescription) copy
      );
      var collectionComparisonSchema = this.createRelationshipStructureSchema(
          attributeNamePath,
          graphElement.getType()
      );
      return this.resolveFoundSortValue(
          foundSortValue,
          defaultValue,
          collectionComparisonSchema
      );
    }
  }

  private AttributeValue<?> resolveAttribute(
      AttributeValue<?> defaultValue, 
      Attribute<?> attribute
  ) {
    if (attribute instanceof ListAttribute listAttribute) {
      if (listAttribute.getBoxedValues().isEmpty()) {
        return defaultValue;
      }
      return listAttribute.getBoxedValues().get(0);
    }
    if (attribute instanceof SetAttribute setAttribute) {
      if (setAttribute.getBoxedValues().isEmpty()) {
        return defaultValue;
      }
      return setAttribute.getBoxedValues().iterator().next();
    }
    return ((LeafAttribute<?, ?>) attribute).getBoxedValue();
  }

  private AttributeValue<?> resolveFoundSortValue(
      Object sortValue,
      AttributeValue<?> defaultValue,
      List<SchemaAndCollectionComparisonOperator> collectionComparisonSchema
  ) {
    if (sortValue == null) {
      return defaultValue;
    }
    if (collectionComparisonSchema.isEmpty()) {
      var attribute = (Attribute<?>) sortValue;
      return this.resolveAttribute(defaultValue, attribute);
    }
    var list = (List<Object>) sortValue;
    if (list.isEmpty()) {
      return defaultValue;
    }
    var len = collectionComparisonSchema.size();
    var restOfCollectionSchema = collectionComparisonSchema.subList(1, len);
    return this.resolveFoundSortValue(list.get(0), defaultValue, restOfCollectionSchema);
  }
}
