package ai.stapi.graphsystem.genericGraphEventFactory;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graphsystem.genericGraphEventFactory.exception.GenericGraphEventFactoryException;
import ai.stapi.graphsystem.genericGraphEventFactory.specific.SpecificGraphEventFactory;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.graphsystem.messaging.event.DynamicGraphUpdatedEvent;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GenericGraphEventFactory {

  private final List<SpecificGraphEventFactory> eventFactories;

  public GenericGraphEventFactory(
      List<SpecificGraphEventFactory> eventFactories
  ) {
    this.eventFactories = eventFactories;
  }

  public AggregateGraphUpdatedEvent<? extends UniqueIdentifier> createEvent(
      Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> eventType,
      String eventName,
      UniqueIdentifier identity,
      Graph graph,
      List<GraphElementForRemoval> elementsForRemoval
  ) {
    if (DynamicGraphUpdatedEvent.class.equals(eventType)) {
      return new DynamicGraphUpdatedEvent(
          eventName,
          identity,
          graph,
          elementsForRemoval
      );
    }
    var supportingFactory = this.getSupportingSpecificFactory(eventType);
    return supportingFactory.createEvent(identity, graph, elementsForRemoval);
  }

  public AggregateGraphUpdatedEvent<? extends UniqueIdentifier> createEvent(
      Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> eventType,
      UniqueIdentifier identity,
      Graph graph,
      List<GraphElementForRemoval> elementsForRemoval
  ) {
    return this.createEvent(
        eventType,
        eventType.getSimpleName(),
        identity,
        graph,
        elementsForRemoval
    );
  }

  @NotNull
  private SpecificGraphEventFactory getSupportingSpecificFactory(
      Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> event
  ) {
    var supportingFactories = this.eventFactories.stream()
        .filter(specificObjectGraphMapper -> specificObjectGraphMapper.supports(event))
        .toList();
    if (supportingFactories.isEmpty()) {
      throw GenericGraphEventFactoryException.becauseNoSupportingSpecificFactoriesForGivenEvent(event);
    }
    if (supportingFactories.size() > 1) {
      throw GenericGraphEventFactoryException.becauseMoreThanOneSpecificFactoriesForGivenCommand(event);
    }
    return supportingFactories.get(0);
  }
}
