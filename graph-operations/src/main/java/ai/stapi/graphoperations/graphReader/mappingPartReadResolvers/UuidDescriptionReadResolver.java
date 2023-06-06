package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graphoperations.graphReader.readResults.UuidIdentityReadResult;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import java.util.List;

public class UuidDescriptionReadResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository graph
  ) {
    if (previousResult instanceof NodeReadResult nodeReadResult) {
      return List.of(
          new UuidIdentityReadResult(nodeReadResult.getNode().getId())
      );
    }
    if (previousResult instanceof EdgeReadResult edgeReadResult) {
      return List.of(
          new UuidIdentityReadResult(edgeReadResult.getEdge().getId())
      );
    }
    throw GraphDescriptionReadResolverException.becauseIngoingReadResultIsNotSupported(
        previousResult,
        this
    );
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof UuidIdentityDescription;
  }
}
