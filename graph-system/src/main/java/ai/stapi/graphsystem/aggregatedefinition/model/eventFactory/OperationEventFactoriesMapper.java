package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.OperationDefinitionParameters;
import java.util.List;

public interface OperationEventFactoriesMapper {

  List<EventFactory> map(OperationDefinitionDTO operationDefinition);

  List<EventFactoryModificationResult> mapParameters(
      OperationDefinitionParameters operationDefinitionParameters
  );
}
