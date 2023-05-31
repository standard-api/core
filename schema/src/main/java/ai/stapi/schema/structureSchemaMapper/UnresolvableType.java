package ai.stapi.schema.structureSchemaMapper;

import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.List;

public record UnresolvableType(StructureDefinitionData structureDefinitionData,
                               List<String> missingDependencies) {

}
