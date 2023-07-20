package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.eventdefinition.EventMessageDefinitionData;
import ai.stapi.graphsystem.operationdefinition.model.FieldDefinitionWithSource;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.OperationDefinitionParameters;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UpdatedOperationEventFactoriesMapper implements OperationEventFactoriesMapper {

  private final OperationDefinitionStructureTypeMapper mapper;
  private final AggregateDefinitionProvider aggregateDefinitionProvider;

  public UpdatedOperationEventFactoriesMapper(
      OperationDefinitionStructureTypeMapper mapper,
      AggregateDefinitionProvider aggregateDefinitionProvider
  ) {
    this.mapper = mapper;
    this.aggregateDefinitionProvider = aggregateDefinitionProvider;
  }

  @Override
  public List<EventFactory> map(OperationDefinitionDTO operationDefinition) {
    var resourceName = operationDefinition.getResource().get(0);
    var createdEventId = this.createUpdatedEventId(resourceName);
    var createdEventName = this.createUpdatedEventName(resourceName);
    var fakedStructure = this.mapper.map(operationDefinition);
    var eventFactory = this.getEventFactory(operationDefinition.getId());
    return List.of(
        new EventFactory(
            eventFactory.isPresent() ? eventFactory.get().getId() : UUID.randomUUID().toString(),
            new EventMessageDefinitionData(
                createdEventId,
                createdEventName,
                new StructureDefinitionId(resourceName),
                "Generated Event for updated " + resourceName + "."
            ),
            fakedStructure.getAllFields().values().stream()
                .map(param -> this.createModification(
                    param.getName(),
                    param.getName()
                )).toList()
        )
    );
  }

  @Override
  public List<EventFactoryModificationResult> mapParameters(
      OperationDefinitionParameters operationDefinitionParameters
  ) {
    var operationId = operationDefinitionParameters.getOperationId();
    var eventFactory = this.getEventFactory(operationId);
    var fakedStructure = this.mapper.map(
        OperationDefinitionDTO.bareBone(operationId, operationDefinitionParameters.getParameters())
    );
    return List.of(
        new EventFactoryModificationResult(
            eventFactory.isPresent() ? eventFactory.get().getId() : UUID.randomUUID().toString(),
            fakedStructure.getAllFields().values().stream()
                .map(FieldDefinitionWithSource.class::cast)
                .map(param -> this.createModification(
                    param.getName(),
                    param.getName()
                )).toList()
        )
    );
  }

  private Optional<EventFactory> getEventFactory(String operationId) {
    if (!this.aggregateDefinitionProvider.containsAggregateForOperation(operationId)) {
      return Optional.empty();
    }
    var aggregate = this.aggregateDefinitionProvider.getAggregateForOperation(
        operationId
    );
    var commandHandler = aggregate.getCommand().stream()
        .filter(command -> command.getOperation().getId().equals(operationId))
        .findFirst();

    if (commandHandler.isPresent()) {
      var eventFactories = commandHandler.get().getEventFactory();
      if (!eventFactories.isEmpty()) {
        return Optional.of(eventFactories.get(0));
      }
    }
    return Optional.empty();
  }

  private EventFactoryModification createModification(
      String modificationPath,
      String parameterName
  ) {
    return EventFactoryModification.upsert(
        modificationPath,
        null,
        parameterName
    );
  }

  private String createUpdatedEventId(String resourceName) {
    return resourceName + "Updated";
  }

  private String createUpdatedEventName(String resourceName) {
    return resourceName + " Updated";
  }
}
