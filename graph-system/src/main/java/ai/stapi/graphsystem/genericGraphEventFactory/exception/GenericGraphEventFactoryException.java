package ai.stapi.graphsystem.genericGraphEventFactory.exception;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;

public class GenericGraphEventFactoryException extends RuntimeException {

  private GenericGraphEventFactoryException(String message) {
    super(message);
  }

  public static GenericGraphEventFactoryException becauseNoSupportingSpecificFactoriesForGivenEvent(
      Class<? extends AggregateGraphUpdatedEvent> event
  ) {
    return new GenericGraphEventFactoryException(
        "There are no supporting factories for event '" + event.getSimpleName() + "'."
    );
  }

  public static GenericGraphEventFactoryException becauseMoreThanOneSpecificFactoriesForGivenCommand(
      Class<? extends AggregateGraphUpdatedEvent> event) {
    return new GenericGraphEventFactoryException(
        "There are multiple supporting factories for event '"
            + event.getSimpleName()
            + "' and that is not allowed."
    );
  }
}
