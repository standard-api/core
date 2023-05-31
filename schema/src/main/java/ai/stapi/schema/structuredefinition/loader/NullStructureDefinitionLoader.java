package ai.stapi.schema.structuredefinition.loader;

import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.ArrayList;
import java.util.List;

public class NullStructureDefinitionLoader implements StructureDefinitionLoader {

  @Override
  public List<StructureDefinitionData> load() {
    return new ArrayList<>();
  }
}
