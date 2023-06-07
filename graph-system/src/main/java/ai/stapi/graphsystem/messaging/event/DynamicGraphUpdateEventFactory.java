package ai.stapi.graphsystem.messaging.event;

import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DynamicGraphUpdateEventFactory {

  public AggregateGraphUpdatedEvent<? extends UniqueIdentifier> createEvent(
      String eventName,
      UniqueIdentifier identity,
      Graph graph,
      List<GraphElementForRemoval> elementsForRemoval
  ) {
    return new DynamicGraphUpdatedEvent(
        eventName,
        identity,
        graph,
        elementsForRemoval
    );
  }

  public boolean supports(Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> event) {
    return event.equals(DynamicGraphUpdatedEvent.class);
  }
}
