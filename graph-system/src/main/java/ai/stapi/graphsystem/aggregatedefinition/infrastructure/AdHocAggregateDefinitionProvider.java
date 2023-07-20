package ai.stapi.graphsystem.aggregatedefinition.infrastructure;

import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.exceptions.CannotProvideAggregateDefinition;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

public class AdHocAggregateDefinitionProvider implements AggregateDefinitionProvider {

  private final GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader;
  private final ScopeCacher scopeCacher;
  private final OperationDefinitionProvider operationDefinitionProvider;

  public AdHocAggregateDefinitionProvider(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher,
      OperationDefinitionProvider operationDefinitionProvider
  ) {
    this.genericAdHocModelDefinitionsLoader = genericAdHocModelDefinitionsLoader;
    this.scopeCacher = scopeCacher;
    this.operationDefinitionProvider = operationDefinitionProvider;
  }

  @Override
  public List<AggregateDefinitionDTO> provideAll() {
    return this.getAggregateDefinitions();
  }

  @Override
  public AggregateDefinitionDTO provide(
      String aggregateType
  ) throws CannotProvideOperationDefinition {
    return this.getAggregateDefinitions().stream()
        .filter(aggregate -> aggregate.getName().equals(aggregateType))
        .findFirst()
        .orElseThrow(() -> CannotProvideAggregateDefinition.becauseItDoesNotExist(aggregateType));
  }

  private List<AggregateDefinitionDTO> getAggregateDefinitions() {
    return this.scopeCacher.getCachedOrCompute(
        AdHocAggregateDefinitionProvider.class,
        this::load
    );
  }

  private List<AggregateDefinitionDTO> load(ScopeOptions scopeOptions) {
    var createAggregates = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "CreateAggregateDefinition",
            AggregateDefinitionDTO.class
        ).stream()
        .toList();

    var additionalCommandHandlers = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "AddCommandOnAggregateDefinition",
            AddCommandOnAggregateDefinitionDTO.class
        ).stream()
        .toList();

    this.ensureAdditionalCommandAreValid(scopeOptions, createAggregates, additionalCommandHandlers);

    var groupedCommands = additionalCommandHandlers
        .stream()
        .collect(Collectors.groupingBy(AddCommandOnAggregateDefinitionDTO::getId));

    var aggregatesWithAddedCommands = createAggregates.stream().map(
        aggregate -> {
          var addedCommands = groupedCommands.get(aggregate.getId());
          if (addedCommands == null) {
            return aggregate;
          }
          addedCommands.stream()
              .map(AddCommandOnAggregateDefinitionDTO::getCommand)
              .flatMap(List::stream)
              .forEach(aggregate::addCommandHandlerDefinition);

          return aggregate;
        }
    ).toList();

    var additionalModifications = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "AddModificationOnAggregateDefinitionCommandEventFactory",
            AddModificationOnAggregateDefinitionDTO.class
        ).stream()
        .toList();

    this.ensureAdditionalModificationsAreValid(
        scopeOptions,
        aggregatesWithAddedCommands,
        additionalModifications
    );

    var groupedModifications = additionalModifications
        .stream()
        .collect(Collectors.groupingBy(AddModificationOnAggregateDefinitionDTO::getId));

    var aggregatesWithAddedModifications = createAggregates.stream().map(
        aggregate -> {
          var addedModifications = groupedModifications.get(aggregate.getId());
          if (addedModifications == null) {
            return aggregate;
          }
          addedModifications.forEach(addModification -> {
            var foundEventFactory = aggregate.getCommand()
                .stream()
                .flatMap(command -> command.getEventFactory().stream())
                .filter(eventFactory -> eventFactory.getId().equals(addModification.getEventFactoryId()))
                .findFirst()
                .get();

            foundEventFactory.addModifications(addModification.getModification());
          });

          return aggregate;
        }
    ).toList();

    return aggregatesWithAddedModifications.stream().map(aggregate -> new AggregateDefinitionDTO(
        aggregate.getId(),
        aggregate.getName(),
        aggregate.getDescription(),
        aggregate.getCommand().stream().map(command -> new CommandHandlerDefinitionDTO(
            this.operationDefinitionProvider.provide(command.getOperation().getId()),
            command.getCreationalPolicy(),
            command.getEventFactory()
        )).toList(),
        aggregate.getStructure()
    )).toList();
  }

  private void ensureAdditionalCommandAreValid(
      ScopeOptions scopeOptions,
      List<AggregateDefinitionDTO> createAggregates,
      List<AddCommandOnAggregateDefinitionDTO> additionalCommandHandlers
  ) {
    var addCommandWithoutTarget = additionalCommandHandlers.stream()
        .filter(map -> this.isNotContainedInFoundAggregates(createAggregates, map))
        .toList();

    if (!addCommandWithoutTarget.isEmpty()) {
      throw new CannotProvideAggregateDefinition(
          String.format(
              "Cannot provide aggregate definitions for scope: %s with tags: %s.%n%s%n%s",
              scopeOptions.getScopes(),
              scopeOptions.getTags(),
              "There were some AddCommandOnAggregateDefinition JSONs which eiter did "
                  + "not have id specified or it was missing in found AggregateDefinitions.",
              "Missing ids: " + addCommandWithoutTarget.stream()
                  .map(AddCommandOnAggregateDefinitionDTO::getId)
                  .toList()
          )
      );
    }
  }

  private boolean isNotContainedInFoundAggregates(
      List<AggregateDefinitionDTO> createAggregates,
      AddCommandOnAggregateDefinitionDTO addCommand
  ) {
    return addCommand.getId() == null || createAggregates.stream().noneMatch(
        aggregate -> aggregate.getId().equals(addCommand.getId())
    );
  }

  private void ensureAdditionalModificationsAreValid(
      ScopeOptions scopeOptions,
      List<AggregateDefinitionDTO> aggregates,
      List<AddModificationOnAggregateDefinitionDTO> additionalModifications
  ) {
    var addModificationWithoutTarget = additionalModifications.stream()
        .filter(map -> !this.isModificationContainedInFoundAggregates(aggregates, map))
        .toList();

    if (!addModificationWithoutTarget.isEmpty()) {
      throw new CannotProvideAggregateDefinition(
          String.format(
              "Cannot provide aggregate definitions for scope: %s with tags: %s.%n%s%n%s",
              scopeOptions.getScopes(),
              scopeOptions.getTags(),
              "There were some AddModificationOnAggregateDefinition JSONs which eiter did "
                  + "not have id specified or it was missing in found AggregateDefinitions.",
              "Missing ids: " + addModificationWithoutTarget.stream()
                  .map(AddModificationOnAggregateDefinitionDTO::getId)
                  .toList()
          )
      );
    }
  }

  private boolean isModificationContainedInFoundAggregates(
      List<AggregateDefinitionDTO> aggregates,
      AddModificationOnAggregateDefinitionDTO addModification
  ) {
    if (addModification.getId() == null) {
      return false;
    }
    if (addModification.getEventFactoryId() == null) {
      return false;
    }
    var foundAggregate = aggregates.stream()
        .filter(aggregate -> aggregate.getId().equals(addModification.getId()))
        .findFirst();

    if (foundAggregate.isEmpty()) {
      return false;
    }
    return foundAggregate.get().getCommand().stream().anyMatch(
        command -> command.getEventFactory().stream().anyMatch(
            eventFactory -> eventFactory.getId().equals(addModification.getEventFactoryId())
        )
    );
  }

  private static class AddCommandOnAggregateDefinitionDTO {

    private String id;
    private List<CommandHandlerDefinitionDTO> command;

    private AddCommandOnAggregateDefinitionDTO() {
    }

    public AddCommandOnAggregateDefinitionDTO(
        String id,
        List<CommandHandlerDefinitionDTO> command
    ) {
      this.id = id;
      this.command = command;
    }

    public String getId() {
      return id;
    }

    public List<CommandHandlerDefinitionDTO> getCommand() {
      return command;
    }
  }

  private static class AddModificationOnAggregateDefinitionDTO {

    private String id;
    private String eventFactoryId;
    private List<CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification> modification;

    private AddModificationOnAggregateDefinitionDTO() {
    }

    public AddModificationOnAggregateDefinitionDTO(
        String id,
        String eventFactoryId,
        List<CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification> modification
    ) {
      this.id = id;
      this.eventFactoryId = eventFactoryId;
      this.modification = modification;
    }

    public String getId() {
      return id;
    }

    public String getEventFactoryId() {
      return eventFactoryId;
    }

    public List<CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification> getModification() {
      return modification;
    }
  }
}
