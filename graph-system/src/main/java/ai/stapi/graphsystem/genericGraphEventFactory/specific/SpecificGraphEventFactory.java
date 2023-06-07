package ai.stapi.graphsystem.genericGraphEventFactory.specific;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public interface SpecificGraphEventFactory {

  AggregateGraphUpdatedEvent<? extends UniqueIdentifier> createEvent(
      UniqueIdentifier identity,
      Graph graph,
      List<GraphElementForRemoval> elementsForRemoval
  );

  boolean supports(
      Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> event);
}
