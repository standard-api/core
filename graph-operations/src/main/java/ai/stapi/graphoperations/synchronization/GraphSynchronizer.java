package ai.stapi.graphoperations.synchronization;

import ai.stapi.graph.Graph;
import ai.stapi.graph.exceptions.GraphException;
import org.springframework.stereotype.Service;

@Service
public interface GraphSynchronizer {

  void synchronize(Graph graph) throws GraphException;
}
