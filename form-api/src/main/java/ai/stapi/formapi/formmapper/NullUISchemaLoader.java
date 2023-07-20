package ai.stapi.formapi.formmapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import java.util.Map;

public class NullUISchemaLoader implements UISchemaLoader {
  @Override
  public Map<String, Object> load(OperationDefinitionDTO operationDefinitionDTO) {
    return Map.of();
  }
}
