package ai.stapi.formapi.formmapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import java.util.Map;

public interface FormDataLoader {

  Map<String, Object> load(
      OperationDefinitionDTO operationDefinitionDTO,
      String resourceId,
      String startModificationNodeIdAndType

  );

}
