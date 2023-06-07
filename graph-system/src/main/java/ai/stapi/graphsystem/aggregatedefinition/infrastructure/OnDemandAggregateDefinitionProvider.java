package ai.stapi.graphsystem.aggregatedefinition.infrastructure;

import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.ResourceAggregateDefinitionMapper;
import ai.stapi.graphsystem.aggregatedefinition.model.eventFactory.CreatedOperationEventFactoriesMapper;
import ai.stapi.graphsystem.aggregatedefinition.model.eventFactory.ItemAddedOperationEventFactoriesMapper;
import ai.stapi.graphsystem.aggregatedefinition.model.exceptions.CannotProvideAggregateDefinition;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.AddItemOnResourceOperationsMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.CreationalResourceOperationMapper;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import java.util.List;

public class OnDemandAggregateDefinitionProvider implements AggregateDefinitionProvider {

  private final StructureSchemaProvider structureSchemaProvider;
  private final ResourceAggregateDefinitionMapper aggregateMapper;
  private final CreationalResourceOperationMapper createOperationMapper;
  private final AddItemOnResourceOperationsMapper addItemOperationsMapper;
  private final CreatedOperationEventFactoriesMapper createdEventFactoryMapper;
  private final ItemAddedOperationEventFactoriesMapper itemAddedOperationEventFactoryMapper;
  private final ScopeCacher scopeCacher;

  public OnDemandAggregateDefinitionProvider(
      StructureSchemaProvider structureSchemaProvider,
      ResourceAggregateDefinitionMapper aggregateMapper,
      CreationalResourceOperationMapper createOperationMapper,
      AddItemOnResourceOperationsMapper addItemOperationsMapper,
      CreatedOperationEventFactoriesMapper createdEventFactoryMapper,
      ItemAddedOperationEventFactoriesMapper itemAddedOperationEventFactoryMapper,
      ScopeCacher scopeCacher
  ) {
    this.structureSchemaProvider = structureSchemaProvider;
    this.aggregateMapper = aggregateMapper;
    this.createOperationMapper = createOperationMapper;
    this.addItemOperationsMapper = addItemOperationsMapper;
    this.createdEventFactoryMapper = createdEventFactoryMapper;
    this.itemAddedOperationEventFactoryMapper = itemAddedOperationEventFactoryMapper;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public List<AggregateDefinitionDTO> provideAll() {
    return this.getAggregates().stream().toList();
  }

  @Override
  public AggregateDefinitionDTO provide(
      String aggregateType
  ) throws CannotProvideAggregateDefinition {
    return this.getAggregates().stream()
        .filter(resource -> resource.getName().equals(aggregateType))
        .findFirst()
        .orElseThrow(() -> CannotProvideAggregateDefinition.becauseItDoesNotExist(aggregateType));


  }

  @Override
  public boolean containsAggregateForOperation(String operationDefinitionId) {
    var cached = this.scopeCacher.<List<AggregateDefinitionDTO>>getCached(
        OnDemandAggregateDefinitionProvider.class
    );
    if (cached == null) {
      return false;
    }
    return cached.stream().anyMatch(
        aggregate -> aggregate.containsOperation(operationDefinitionId)
    );
  }

  private List<AggregateDefinitionDTO> getAggregates() {
    return this.scopeCacher.getCachedOrCompute(
        OnDemandAggregateDefinitionProvider.class,
        this::createAggregates
    );
  }

  private List<AggregateDefinitionDTO> createAggregates(ScopeOptions scopeOptions) {
    return this.structureSchemaProvider.provideSchema()
        .getStructureTypes()
        .values()
        .stream()
        .filter(ResourceStructureType.class::isInstance)
        .map(ResourceStructureType.class::cast)
        .map(this::createAggregateDefinition)
        .toList();
  }


  private AggregateDefinitionDTO createAggregateDefinition(
      ResourceStructureType resourceStructureType
  ) {
    var aggregateDefinition = this.aggregateMapper.map(resourceStructureType);
    var addItemCommandHandlers = this.createAddItemCommandHandlers(
        resourceStructureType
    );
    addItemCommandHandlers.forEach(aggregateDefinition::addCommandHandlerDefinition);
    if (!resourceStructureType.isAbstract()) {
      aggregateDefinition.addCommandHandlerDefinition(
          this.createCreateCommandHandler(resourceStructureType)
      );
    }
    return aggregateDefinition;
  }

  private List<CommandHandlerDefinitionDTO> createAddItemCommandHandlers(
      ResourceStructureType resourceStructureType
  ) {
    return this.addItemOperationsMapper.map(resourceStructureType).getOperations()
        .stream()
        .map(this::createAddItemCommandHandler)
        .toList();
  }

  private CommandHandlerDefinitionDTO createAddItemCommandHandler(
      OperationDefinitionDTO operationDefinition
  ) {
    var itemAddedFactories = this.itemAddedOperationEventFactoryMapper.map(operationDefinition);
    return new CommandHandlerDefinitionDTO(
        operationDefinition,
        CommandHandlerDefinitionDTO.CreationPolicy.NEVER,
        itemAddedFactories
    );
  }

  private CommandHandlerDefinitionDTO createCreateCommandHandler(
      ResourceStructureType resourceStructureType
  ) {
    var operationDefinition = this.createOperationMapper.map(resourceStructureType)
        .getOperations()
        .get(0);
    var eventFactories = this.createdEventFactoryMapper.map(operationDefinition);
    return new CommandHandlerDefinitionDTO(
        operationDefinition,
        CommandHandlerDefinitionDTO.CreationPolicy.IF_MISSING,
        eventFactories
    );
  }
}
