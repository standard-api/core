package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EdgeDescriptionReadResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository context
  ) {
    if (!(previousResult instanceof NodeReadResult nodeReadResult)) {
      throw GraphDescriptionReadResolverException.becauseIngoingReadResultIsNotSupported(
          previousResult, this);
    }
    var parameters = (EdgeDescriptionParameters) graphDescription.getParameters();
    var edgeStream = nodeReadResult.getNode()
        .getEdges(parameters.getEdgeType())
        .stream();

    if (graphDescription instanceof OutgoingEdgeDescription) {
      return edgeStream
          .filter(traversableEdge -> traversableEdge.getNodeFromId()
              .equals(nodeReadResult.getGraphElement().getId()))
          .map(edge -> (ReadResult) new EdgeReadResult(edge))
          .toList();
    }

    if (graphDescription instanceof IngoingEdgeDescription) {
      return edgeStream
          .filter(traversableEdge -> traversableEdge.getNodeToId()
              .equals(nodeReadResult.getGraphElement().getId()))
          .map(edge -> (ReadResult) new EdgeReadResult(edge))
          .toList();
    }
    throw GraphDescriptionReadResolverException.becauseGraphDescriptionTypeIsNotSupported(
        graphDescription, this);
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof AbstractEdgeDescription;
  }
}
