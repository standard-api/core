package ai.stapi.graphoperations.synchronization;

import ai.stapi.graph.Graph;
import ai.stapi.graph.exceptions.GraphException;

public interface GraphSynchronizer {

  void synchronize(Graph graph) throws GraphException;
}
