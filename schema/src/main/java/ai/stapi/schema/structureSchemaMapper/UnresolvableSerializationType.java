package ai.stapi.schema.structureSchemaMapper;

import java.util.List;

public record UnresolvableSerializationType(
    String serializationType,
    String originalSerializationType,
    List<String> missingDependencies
) {

}
