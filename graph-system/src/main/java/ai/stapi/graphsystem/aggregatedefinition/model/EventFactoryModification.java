package ai.stapi.graphsystem.aggregatedefinition.model;

import org.jetbrains.annotations.Nullable;

public class EventFactoryModification {

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
