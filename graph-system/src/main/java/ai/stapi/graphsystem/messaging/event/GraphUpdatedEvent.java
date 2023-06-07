package ai.stapi.graphsystem.messaging.event;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.graphsystem.messaging.event.Event;
import java.util.ArrayList;
import java.util.List;

public abstract class GraphUpdatedEvent implements Event {

  private Graph synchronizedGraph;
  private List<GraphElementForRemoval> graphElementsForRemoval;

  protected GraphUpdatedEvent() {
  }

  protected GraphUpdatedEvent(List<GraphElementForRemoval> graphElementsForRemoval) {
    this.graphElementsForRemoval = graphElementsForRemoval;
    this.synchronizedGraph = new Graph();
  }

  protected GraphUpdatedEvent(Graph synchronizedGraph) {
    this.synchronizedGraph = synchronizedGraph;
    this.graphElementsForRemoval = new ArrayList<>();
  }

  protected GraphUpdatedEvent(
      Graph synchronizedGraph,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    this.synchronizedGraph = synchronizedGraph;
    this.graphElementsForRemoval = graphElementsForRemoval;
  }

  public Graph getSynchronizedGraph() {
    return synchronizedGraph;
  }

  public List<GraphElementForRemoval> getGraphElementsForRemoval() {
    return graphElementsForRemoval;
  }
}
