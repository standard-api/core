package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchemaMapper.StructureDefinitionToSSMapper;
import ai.stapi.schema.structureSchemaMapper.UnresolvableType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Primary;

@Primary
public class StubStructureSchemaProvider implements StructureSchemaProvider {

  private final StructureDefinitionToSSMapper structureDefinitionToSSMapper;
  private StructureSchema structureSchema;

  public StubStructureSchemaProvider(StructureDefinitionToSSMapper structureDefinitionToSSMapper) {
    this.structureDefinitionToSSMapper = structureDefinitionToSSMapper;
    this.structureSchema = new StructureSchema();
  }

  @Override
  public AbstractStructureType provideSpecific(String serializationType) {
    return this.structureSchema.getDefinition(serializationType);
  }

  @Override
  public boolean containsSchema(String serializationType) {
    return this.structureSchema.has(serializationType);
  }

  @Override
  public StructureSchema provideSchema() {
    return this.structureSchema;
  }

  @Override
  public List<UnresolvableType> provideUnresolvableTypes() {
    return List.of();
  }

  @Override
  public List<UnresolvableType> add(StructureDefinitionData structureDefinitionData) {
    var mapped = this.structureDefinitionToSSMapper.map(
        structureDefinitionData,
        this.structureSchema
    );
    this.structureSchema = mapped.structureSchema();
    return mapped.unresolvableTypes();
  }

  @Override
  public List<UnresolvableType> add(List<StructureDefinitionData> structureDefinitionData) {
    var mapped = this.structureDefinitionToSSMapper.map(
        structureDefinitionData,
        this.structureSchema
    );
    this.structureSchema = mapped.structureSchema();
    return mapped.unresolvableTypes();
  }

  @Override
  public List<UnresolvableType> add(StructureDefinitionData... structureDefinitionData) {
    var mapped = this.structureDefinitionToSSMapper.map(
        Arrays.stream(structureDefinitionData).toList(),
        this.structureSchema
    );
    this.structureSchema = mapped.structureSchema();
    return mapped.unresolvableTypes();
  }

  public void setSchema(StructureSchema structureSchema) {
    this.structureSchema = structureSchema;
  }
}
