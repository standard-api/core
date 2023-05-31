package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchemaMapper.UnresolvableType;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.List;

public interface StructureSchemaProvider {

  AbstractStructureType provideSpecific(String serializationType)
      throws CannotProvideStructureSchema;

  boolean containsSchema(String serializationType);

  StructureSchema provideSchema();

  List<UnresolvableType> provideUnresolvableTypes();

  List<UnresolvableType> add(StructureDefinitionData structureDefinitionData);

  List<UnresolvableType> add(List<StructureDefinitionData> structureDefinitionData);

  List<UnresolvableType> add(StructureDefinitionData... structureDefinitionData);
}
