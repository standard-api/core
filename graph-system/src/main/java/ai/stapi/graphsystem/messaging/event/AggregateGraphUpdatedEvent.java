package ai.stapi.graphsystem.messaging.event;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public abstract class AggregateGraphUpdatedEvent<T extends UniqueIdentifier> extends GraphUpdatedEvent {

  private T identity;

  protected AggregateGraphUpdatedEvent() {
  }

  protected AggregateGraphUpdatedEvent(
      T identity,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(graphElementsForRemoval);
    this.identity = identity;
  }

  protected AggregateGraphUpdatedEvent(
      T identity,
      Graph synchronizedGraph
  ) {
    super(synchronizedGraph);
    this.identity = identity;
  }

  protected AggregateGraphUpdatedEvent(
      T identity,
      Graph synchronizedGraph,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(synchronizedGraph, graphElementsForRemoval);
    this.identity = identity;
  }

  public T getIdentity() {
    return identity;
  }
}
