package ai.stapi.schema.structuredefinition.loader;

import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.List;

public interface StructureDefinitionLoader {

  List<StructureDefinitionData> load();
}
