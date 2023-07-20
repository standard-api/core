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
      this.getModification().addAll(modification);
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

    public static class EventFactoryModification {

      public static final String ADD = "add";
      public static final String UPSERT = "upsert";
      public static final String REMOVE = "remove";
      public static final String INSERT = "insert";
      public static final String MOVE = "move";
      private String kind;
      private String modificationPath;
      
      @Nullable
      private String startIdParameterName;

      @Nullable
      private String inputValueParameterName;

      @Nullable
      private String destinationIndexParameterName;

      private EventFactoryModification() {
      }

      private EventFactoryModification(
          String kind,
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName,
          @Nullable String destinationIndexParameterName
      ) {
        this.kind = kind;
        this.modificationPath = modificationPath;
        this.startIdParameterName = startIdParameterName;
        this.inputValueParameterName = inputValueParameterName;
        this.destinationIndexParameterName = destinationIndexParameterName;
      }

      public static EventFactoryModification add(
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName
      ) {
        return new EventFactoryModification(
            ADD,
            modificationPath,
            startIdParameterName,
            inputValueParameterName,
            null
        );
      }

      public static EventFactoryModification upsert(
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName
      ) {
        return new EventFactoryModification(
            UPSERT,
            modificationPath,
            startIdParameterName,
            inputValueParameterName,
            null
        );
      }

      public static EventFactoryModification remove(
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName
      ) {
        return new EventFactoryModification(
            REMOVE,
            modificationPath,
            startIdParameterName,
            inputValueParameterName,
            null
        );
      }

      public static EventFactoryModification insert(
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName,
          @Nullable String destinationIndexParameterName
      ) {
        return new EventFactoryModification(
            INSERT,
            modificationPath,
            startIdParameterName,
            inputValueParameterName,
            destinationIndexParameterName
        );
      }

      public static EventFactoryModification move(
          String modificationPath,
          @Nullable String startIdParameterName,
          @Nullable String inputValueParameterName,
          @Nullable String destinationIndexParameterName
      ) {
        return new EventFactoryModification(
            MOVE,
            modificationPath,
            startIdParameterName,
            inputValueParameterName,
            destinationIndexParameterName
        );
      }

      public String getKind() {
        return kind;
      }

      public String getModificationPath() {
        return modificationPath;
      }

      public String getStartIdParameterName() {
        return startIdParameterName;
      }

      @Nullable
      public String getInputValueParameterName() {
        return inputValueParameterName;
      }

      @Nullable
      public String getDestinationIndexParameterName() {
        return destinationIndexParameterName;
      }
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
