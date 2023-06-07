package ai.stapi.graphsystem.operationdefinition.infrastructure;

import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.ResourceOperationsMapper;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import java.util.List;

public class OnDemandOperationDefinitionProvider implements OperationDefinitionProvider {

  private final List<ResourceOperationsMapper> resourceOperationsMappers;
  private final StructureSchemaProvider structureSchemaProvider;
  private final ScopeCacher scopeCacher;


  public OnDemandOperationDefinitionProvider(
      List<ResourceOperationsMapper> resourceOperationsMappers,
      StructureSchemaProvider structureSchemaProvider,
      ScopeCacher scopeCacher
  ) {
    this.resourceOperationsMappers = resourceOperationsMappers;
    this.structureSchemaProvider = structureSchemaProvider;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public List<OperationDefinitionDTO> provideAll() {
    return this.getOperations();
  }

  @Override
  public OperationDefinitionDTO provide(
      String operationId
  ) throws CannotProvideOperationDefinition {
    return this.getOperations().stream()
        .filter(operation -> operation.getId().equals(operationId))
        .findFirst()
        .orElseThrow(() -> new CannotProvideOperationDefinition(operationId));
  }

  @Override
  public boolean contains(String operationId) {
    return this.getOperations().stream().anyMatch(
        operation -> operation.getId().equals(operationId)
    );
  }

  private List<OperationDefinitionDTO> getOperations() {
    return this.scopeCacher.getCachedOrCompute(
        OnDemandOperationDefinitionProvider.class,
        this::createOperations
    );
  }

  private List<OperationDefinitionDTO> createOperations(ScopeOptions scopeOptions) {
    var structureTypes = this.structureSchemaProvider.provideSchema()
        .getStructureTypes()
        .values();

    return structureTypes
        .stream()
        .filter(ResourceStructureType.class::isInstance)
        .map(ResourceStructureType.class::cast)
        .flatMap(
            resourceStructureType -> this.resourceOperationsMappers.stream().flatMap(
                mapper -> mapper.map(resourceStructureType).getOperations().stream()
            )
        ).toList();
  }
}
