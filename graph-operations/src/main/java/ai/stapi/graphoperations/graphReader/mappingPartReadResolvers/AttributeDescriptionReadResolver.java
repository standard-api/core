package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphReader.readResults.AttributeReadResult;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AttributeDescriptionReadResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository graph
  ) {
    var parameters = (AttributeDescriptionParameters) graphDescription.getParameters();
    if (previousResult instanceof NodeReadResult nodeReadResult) {
      var node = graph.loadNode(
          nodeReadResult.getGraphElement().getId(),
          nodeReadResult.getGraphElement().getType()
      );
      if (!node.hasAttribute(parameters.getAttributeName())) {
        return List.of();
//        throw GraphDescriptionReadResolverException.becauseAttributeDoesNotExists(
//            node.getId(),
//            parameters.getAttributeName()
//        );
      }
      return List.of(new AttributeReadResult(node.getAttribute(parameters.getAttributeName())));
    }
    if (previousResult instanceof EdgeReadResult edgeReadResult) {
      var edge = graph.loadEdge(
          edgeReadResult.getGraphElement().getId(),
          edgeReadResult.getGraphElement().getType()
      );
      if (!edge.hasAttribute(parameters.getAttributeName())) {
        throw GraphDescriptionReadResolverException.becauseAttributeDoesNotExists(edge.getId(),
            parameters.getAttributeName());
      }
      return List.of(new AttributeReadResult(edge.getAttribute(parameters.getAttributeName())));
    }
    throw GraphDescriptionReadResolverException.becauseIngoingReadResultIsNotSupported(
        previousResult,
        this
    );
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof AbstractAttributeDescription;
  }
}
