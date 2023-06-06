package ai.stapi.graphoperations.graphLoader.search;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.CollectionComparisonOperator;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchOptionResolver<S extends SearchOption<?>, C extends SearchResolvingContext, R extends ResolvedQueryPart>
    implements SearchOptionResolver<R> {

  protected final StructureSchemaFinder structureSchemaFinder;

  protected AbstractSearchOptionResolver(StructureSchemaFinder structureSchemaFinder) {
    this.structureSchemaFinder = structureSchemaFinder;
  }

  protected abstract R resolveTyped(S option, C context);

  @Override
  public R resolve(SearchOption<?> option, SearchResolvingContext context) {
    return this.resolveTyped((S) option, (C) context);
  }

  protected List<SchemaAndCollectionComparisonOperator> createRelationshipStructureSchema(
      GraphDescription attributeNamePath,
      String graphElementType
  ) {
    var result = new ArrayList<SchemaAndCollectionComparisonOperator>();
    var graphDescription = attributeNamePath;
    var currentGraphElementType = graphElementType;
    while (!graphDescription.getChildGraphDescriptions().isEmpty()) {
      if (graphDescription instanceof AbstractNodeDescription nodeDescription) {
        graphDescription = nodeDescription.getChildGraphDescriptions().get(0);
        var param = (NodeDescriptionParameters) nodeDescription.getParameters();
        currentGraphElementType = param.getNodeType();
      } else if (graphDescription instanceof AbstractEdgeDescription edgeDescription) {
        graphDescription = edgeDescription.getChildGraphDescriptions().get(0);
        var param = (EdgeDescriptionParameters) edgeDescription.getParameters();
        var schema = this.structureSchemaFinder.getFieldDefinitionOrFallback(
            currentGraphElementType,
            param.getEdgeType()
        );
        currentGraphElementType = param.getEdgeType();
        if (edgeDescription instanceof OutgoingEdgeQueryDescription queryDescription) {
          result.add(
              new SchemaAndCollectionComparisonOperator(
                  schema,
                  queryDescription.getCollectionComparisonOperator()
              )
          );
        } else if (edgeDescription instanceof IngoingEdgeQueryDescription queryDescription) {
          result.add(
              new SchemaAndCollectionComparisonOperator(
                  schema,
                  queryDescription.getCollectionComparisonOperator()
              )
          );
        } else {
          result.add(
              new SchemaAndCollectionComparisonOperator(schema, CollectionComparisonOperator.ANY)
          );
        }
      } else {
        graphDescription = graphDescription.getChildGraphDescriptions().get(0);
      }
    }

    return result;
  }

  public record SchemaAndCollectionComparisonOperator(
      FieldDefinition fieldDefinition,
      CollectionComparisonOperator operator
  ) {

  }
}
