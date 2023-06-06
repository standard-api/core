package ai.stapi.graphoperations.graphLoader.graphLoaderOGMFactory;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.GraphElementTypeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.EdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.springframework.stereotype.Service;

@Service
public class GraphLoaderOgmFactory {

  public ObjectGraphMapping create(GraphDescription graphDescription) {
    var ogmBuilder = new ObjectGraphMappingBuilder();
    ogmBuilder.setGraphDescription(
        new GraphDescriptionBuilder().copyWithNewChildren(graphDescription));
    graphDescription.getChildGraphDescriptions().forEach(childDescription ->
        this.resolveChildGraphDescription(childDescription, ogmBuilder)
    );
    return ogmBuilder.build();
  }

  private void resolveChildGraphDescription(
      GraphDescription graphDescription,
      ObjectGraphMappingBuilder ogmBuilder
  ) {
    if (graphDescription instanceof AbstractNodeDescription) {
      var nodeParam = (NodeDescriptionParameters) graphDescription.getParameters();
      var childOgmBuilder = ogmBuilder.addField(nodeParam.getNodeType())
          .addObjectAsObjectFieldMapping();
      if (graphDescription instanceof NodeQueryGraphDescription nodeQueryGraphDescription) {
        childOgmBuilder.setGraphDescription(
            new NodeQueryGraphDescription(
                nodeParam,
                nodeQueryGraphDescription.getSearchQueryParameters(),
                nodeQueryGraphDescription.getChildGraphDescriptions()
            )
        );
      } else {
        childOgmBuilder.setGraphDescription(
            new GraphDescriptionBuilder().addNodeDescription(nodeParam.getNodeType())
        );
      }

      graphDescription.getChildGraphDescriptions().forEach(childDescription ->
          this.resolveChildGraphDescription(childDescription, childOgmBuilder)
      );

    }
    if (graphDescription instanceof AbstractEdgeDescription edgeDescription) {
      var edgeParam = (EdgeDescriptionParameters) edgeDescription.getParameters();
      var childOgmBuilder = ogmBuilder.addField(edgeParam.getEdgeType())
          .addListAsObjectFieldMapping()
          .addObjectChildDefinition();

      if (edgeDescription instanceof EdgeQueryDescription edgeQueryDescription) {
        if (edgeDescription.isOutgoing()) {
          if (edgeQueryDescription.isCompact()) {
            childOgmBuilder.setGraphDescription(
                new OutgoingEdgeQueryDescription(
                    (EdgeDescriptionParameters) edgeQueryDescription.getParameters(),
                    edgeQueryDescription.getSearchQueryParameters()
                )
            );
          } else {
            childOgmBuilder.setGraphDescription(
                OutgoingEdgeQueryDescription.asConnections(
                    (EdgeDescriptionParameters) edgeQueryDescription.getParameters(),
                    edgeQueryDescription.getSearchQueryParameters()
                )
            );
          }
        } else {
          if (edgeQueryDescription.isCompact()) {
            childOgmBuilder.setGraphDescription(
                new IngoingEdgeQueryDescription(
                    (EdgeDescriptionParameters) edgeQueryDescription.getParameters(),
                    edgeQueryDescription.getSearchQueryParameters()
                )
            );
          } else {
            childOgmBuilder.setGraphDescription(
                IngoingEdgeQueryDescription.asConnections(
                    (EdgeDescriptionParameters) edgeQueryDescription.getParameters(),
                    edgeQueryDescription.getSearchQueryParameters()
                )
            );
          }
        }
      } else {
        if (edgeDescription.isOutgoing()) {
          childOgmBuilder
              .setGraphDescription(
                  new GraphDescriptionBuilder().addOutgoingEdge(edgeParam.getEdgeType()));
        } else {
          childOgmBuilder
              .setGraphDescription(
                  new GraphDescriptionBuilder().addIngoingEdge(edgeParam.getEdgeType()));
        }
      }

      graphDescription.getChildGraphDescriptions().forEach(childDescription ->
          this.resolveChildGraphDescription(childDescription, childOgmBuilder)
      );
    }
    if (graphDescription instanceof AbstractAttributeDescription) {
      var attributeParams = (AttributeDescriptionParameters) graphDescription.getParameters();
      ogmBuilder.addField(attributeParams.getAttributeName())
          .addLeafAsObjectFieldMapping()
          .setGraphDescription(graphDescription);
    }
    if (graphDescription instanceof UuidIdentityDescription) {
      ogmBuilder.addField("id")
          .addLeafAsObjectFieldMapping()
          .setGraphDescription(graphDescription);
    }
    if (graphDescription instanceof GraphElementTypeDescription) {
      ogmBuilder.addField("graphElementType")
          .addLeafAsObjectFieldMapping()
          .setGraphDescription(graphDescription);
    }
  }
}
