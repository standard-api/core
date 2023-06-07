package ai.stapi.graphsystem.messaging.event;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public class DynamicGraphUpdatedEvent extends AggregateGraphUpdatedEvent<UniqueIdentifier> {

  private String eventName;

  private DynamicGraphUpdatedEvent() {
  }

  public DynamicGraphUpdatedEvent(
      String eventName,
      UniqueIdentifier identity,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(identity, graphElementsForRemoval);
    this.eventName = eventName;
  }

  public DynamicGraphUpdatedEvent(
      String eventName,
      UniqueIdentifier identity,
      Graph synchronizedGraph
  ) {
    super(identity, synchronizedGraph);
    this.eventName = eventName;
  }

  public DynamicGraphUpdatedEvent(
      String eventName,
      UniqueIdentifier identity,
      Graph synchronizedGraph,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(identity, synchronizedGraph, graphElementsForRemoval);
    this.eventName = eventName;
  }

  public String getEventName() {
    return eventName;
  }
}
