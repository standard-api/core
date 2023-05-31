package ai.stapi.schema.structureSchemaMapper;

import ai.stapi.schema.structureSchema.StructureSchema;
import java.util.List;

public record MappingOutcome(
    StructureSchema structureSchema,
    List<UnresolvableType> unresolvableTypes,
    List<String> successfullyMappedTypes
) {

}
