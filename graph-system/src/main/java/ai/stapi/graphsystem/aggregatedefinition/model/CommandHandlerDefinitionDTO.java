package ai.stapi.graphsystem.aggregatedefinition.model;

import ai.stapi.graphsystem.eventdefinition.EventMessageDefinitionData;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class CommandHandlerDefinitionDTO {

  private OperationDefinitionDTO operation;
  private String creationalPolicy;
  private List<EventFactory> eventFactory;

  private CommandHandlerDefinitionDTO() {
  }

  public CommandHandlerDefinitionDTO(
      OperationDefinitionDTO operation,
      String creationalPolicy,
      List<EventFactory> eventFactory
  ) {
    this.operation = operation;
    this.creationalPolicy = creationalPolicy;
    this.eventFactory = eventFactory;
  }

  public OperationDefinitionDTO getOperation() {
    return operation;
  }

  public String getCreationalPolicy() {
    return creationalPolicy;
  }

  public List<EventFactory> getEventFactory() {
    return eventFactory;
  }

  public static class EventFactory {

    private String id;

    private EventMessageDefinitionData event;

    private List<EventFactoryModification> modification;

    protected EventFactory() {
    }

    public EventFactory(
        String id,
        EventMessageDefinitionData event,
        List<EventFactoryModification> modification
    ) {
      this.id = id;
      this.event = event;
      this.modification = modification;
    }

    public EventFactory(
        EventMessageDefinitionData event,
        List<EventFactoryModification> modification
    ) {
      this(
          UUID.randomUUID().toString(),
          event,
          modification
      );
    }

    public void addModification(EventFactoryModification modification) {
      this.getModification().add(modification);
    }

    public void addModifications(List<EventFactoryModification> modifications) {
      this.getModification().addAll(modifications);
    }

    public EventMessageDefinitionData getEvent() {
      return event;
    }

    public List<EventFactoryModification> getModification() {
      return modification;
    }

    public String getId() {
      return id;
    }
  }

  public static class CreationPolicy {

    public static final String ALWAYS = "always";
    public static final String NEVER = "never";
    public static final String IF_MISSING = "if_missing";

    private CreationPolicy() {
    }
  }
}
