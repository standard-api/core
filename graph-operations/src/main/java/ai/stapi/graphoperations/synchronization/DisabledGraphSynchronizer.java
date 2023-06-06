package ai.stapi.graphoperations.synchronization;

import ai.stapi.graph.Graph;
import ai.stapi.graph.exceptions.GraphException;

public class DisabledGraphSynchronizer implements GraphSynchronizer {

  @Override
  public void synchronize(Graph graph) throws GraphException {
  }
  
}
