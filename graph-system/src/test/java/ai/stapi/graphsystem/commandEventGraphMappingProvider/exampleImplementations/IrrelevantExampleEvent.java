package ai.stapi.graphsystem.commandEventGraphMappingProvider.exampleImplementations;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public class IrrelevantExampleEvent extends AggregateGraphUpdatedEvent<UniqueIdentifier> {

  public IrrelevantExampleEvent(
      UniqueIdentifier identity,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(identity, graphElementsForRemoval);
  }

  protected IrrelevantExampleEvent(
      UniqueIdentifier identity,
      Graph synchronizedGraph
  ) {
    super(identity, synchronizedGraph);
  }

  public IrrelevantExampleEvent(
      UniqueIdentifier identity,
      Graph synchronizedGraph,
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    super(identity, synchronizedGraph, graphElementsForRemoval);
  }
}
