package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchemaMapper.MappingOutcome;
import ai.stapi.schema.structureSchemaMapper.StructureDefinitionDTOMerger;
import ai.stapi.schema.structureSchemaMapper.StructureDefinitionToSSMapper;
import ai.stapi.schema.structureSchemaMapper.UnresolvableType;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultStructureSchemaProvider implements StructureSchemaProvider {

  private final StructureDefinitionToSSMapper structureDefinitionToSSMapper;
  private final StructureDefinitionLoader structureDefinitionLoader;
  private final ScopeCacher scopeCacher;

  public DefaultStructureSchemaProvider(
      StructureDefinitionToSSMapper structureDefinitionToSSMapper,
      StructureDefinitionLoader structureDefinitionLoader,
      ScopeCacher scopeCacher
  ) {
    this.structureDefinitionToSSMapper = structureDefinitionToSSMapper;
    this.structureDefinitionLoader = structureDefinitionLoader;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public AbstractStructureType provideSpecific(String serializationType)
      throws CannotProvideStructureSchema {
    if (!this.getCurrentSchema().containsDefinition(serializationType)) {
      throw new CannotProvideStructureSchema(serializationType, this.getCurrentUnresolvableTypes());
    }
    return this.getCurrentSchema().getDefinition(serializationType);
  }

  @Override
  public StructureSchema provideSchema() {
    return this.getCurrentSchema();
  }

  @Override
  public boolean containsSchema(String serializationType) {
    return this.getCurrentSchema().containsDefinition(serializationType);
  }

  @Override
  public List<UnresolvableType> provideUnresolvableTypes() {
    return this.getCurrentUnresolvableTypes();
  }

  @Override
  public List<UnresolvableType> add(StructureDefinitionData structureDefinitionData) {
    return this.add(List.of(structureDefinitionData));
  }

  @Override
  public List<UnresolvableType> add(StructureDefinitionData... structureDefinitionData) {
    return this.add(Arrays.stream(structureDefinitionData).toList());
  }

  @Override
  public List<UnresolvableType> add(List<StructureDefinitionData> structureDefinitionData) {
    var mixedDefinitions = this.mixItWithPreviouslyFailed(structureDefinitionData);
    var outcome = this.structureDefinitionToSSMapper.map(
        mixedDefinitions,
        getCurrentSchema()
    );
    this.scopeCacher.recompute(
        DefaultStructureSchemaProvider.class,
        new MappingOutcome(
            new StructureSchema(),
            List.of(),
            List.of()
        ),
        (scope, previous) -> new MappingOutcome(
            previous.structureSchema().merge(outcome.structureSchema()),
            outcome.unresolvableTypes(),
            outcome.successfullyMappedTypes()
        )
    );
    this.tryAddFailedTypes(outcome.successfullyMappedTypes());
    return this.getCurrentUnresolvableTypes();
  }

  public List<UnresolvableType> getCurrentFailedTypes() {
    return this.getCurrentUnresolvableTypes();
  }

  private void tryAddFailedTypes(List<String> successfullyAddedTypes) {
    if (this.getCurrentUnresolvableTypes().isEmpty()) {
      return;
    }
    if (successfullyAddedTypes.isEmpty()) {
      return;
    }
    var failed = this.getCurrentUnresolvableTypes().stream()
        .map(UnresolvableType::structureDefinitionData)
        .toList();

    this.scopeCacher.recompute(
        DefaultStructureSchemaProvider.class,
        new MappingOutcome(
            new StructureSchema(),
            List.of(),
            List.of()
        ),
        (scope, previous) -> new MappingOutcome(
            previous.structureSchema(),
            new ArrayList<>(),
            previous.successfullyMappedTypes()
        )
    );
    this.add(failed);
  }

  private List<StructureDefinitionData> mixItWithPreviouslyFailed(
      List<StructureDefinitionData> structureDefinitionData
  ) {
    var joinedStructures = new ConcurrentHashMap<String, StructureDefinitionData>();
    this.getCurrentUnresolvableTypes().stream()
        .map(UnresolvableType::structureDefinitionData)
        .forEach(
            structureDefinitionDTO -> joinedStructures.put(structureDefinitionDTO.getId(),
                structureDefinitionDTO)
        );
    structureDefinitionData.forEach(
        structureDefinitionDTO -> {
          if (joinedStructures.containsKey(structureDefinitionDTO.getId())) {
            joinedStructures.put(structureDefinitionDTO.getId(),
                StructureDefinitionDTOMerger.merge(
                    joinedStructures.get(structureDefinitionDTO.getId()),
                    structureDefinitionDTO
                )
            );
          } else {
            joinedStructures.put(structureDefinitionDTO.getId(), structureDefinitionDTO);
          }
        }
    );
    return joinedStructures.values().stream().toList();
  }

  private MappingOutcome getCurrentOutcome() {
    return this.scopeCacher.getCachedOrCompute(
        DefaultStructureSchemaProvider.class,
        scope -> this.structureDefinitionToSSMapper.map(
            this.structureDefinitionLoader.load()
        )
    );
  }

  private StructureSchema getCurrentSchema() {
    return this.getCurrentOutcome().structureSchema();
  }

  private List<UnresolvableType> getCurrentUnresolvableTypes() {
    return this.getCurrentOutcome().unresolvableTypes();
  }
}
