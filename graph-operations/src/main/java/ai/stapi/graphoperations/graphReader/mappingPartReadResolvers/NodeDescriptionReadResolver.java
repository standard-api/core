package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import java.util.List;

public class NodeDescriptionReadResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    if (!(previousResult instanceof EdgeReadResult edgeReadResult)) {
      throw GraphDescriptionReadResolverException.becauseIngoingReadResultIsNotSupported(
          previousResult,
          this
      );
    }
    var params = (NodeDescriptionParameters) graphDescription.getParameters();
    TraversableNode node;
    if (edgeReadResult.getEdge().getNodeToType().equals(params.getNodeType())) {
      node = edgeReadResult.getEdge().getNodeTo();
    } else if (edgeReadResult.getEdge().getNodeFromType().equals(params.getNodeType())) {
      node = edgeReadResult.getEdge().getNodeFrom();
    } else {
      throw GraphDescriptionReadResolverException.becauseEdgeDoesNotContainNodeOfGivenType(
          edgeReadResult.getGraphElement().getId(),
          params.getNodeType()
      );
    }
    return List.of(new NodeReadResult(node));
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof NodeDescription;
  }
}
