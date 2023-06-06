package ai.stapi.graphoperations.synchronization;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.Graph;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.exceptions.GraphException;

public class ReplacingGraphSynchronizer implements GraphSynchronizer {

  private final NodeRepository nodeRepository;
  private final EdgeRepository edgeRepository;

  public ReplacingGraphSynchronizer(
      NodeRepository nodeRepository, 
      EdgeRepository edgeRepository
  ) {
    this.nodeRepository = nodeRepository;
    this.edgeRepository = edgeRepository;
  }

  @Override
  public void synchronize(Graph graph) throws GraphException {
    graph.getAllNodes().forEach(node -> {
      if (this.nodeRepository.nodeExists(node.getId(), node.getType())) {
        this.nodeRepository.replace(node);
      } else {
        this.nodeRepository.save(node);
      }
    });
    graph.getAllEdges().forEach(edge -> {
      if (this.edgeRepository.edgeExists(edge.getId(), edge.getType())) {
        this.edgeRepository.replace(edge);
      } else {
        this.edgeRepository.save(edge);
      }
    });
  }
}
