package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import java.util.List;

public class EventFactoryModificationResult {

  private final String eventFactoryId;
  private final List<EventFactoryModification> eventFactoryModifications;

  public EventFactoryModificationResult(
      String eventFactoryId,
      List<EventFactoryModification> eventFactoryModifications
  ) {
    this.eventFactoryId = eventFactoryId;
    this.eventFactoryModifications = eventFactoryModifications;
  }

  public String getEventFactoryId() {
    return eventFactoryId;
  }

  public List<EventFactoryModification> getEventFactoryModifications() {
    return eventFactoryModifications;
  }
}
