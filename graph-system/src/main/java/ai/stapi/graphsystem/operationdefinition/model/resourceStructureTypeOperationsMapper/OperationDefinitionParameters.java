package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import java.util.List;

public class OperationDefinitionParameters {

  private final String operationId;
  private final List<ParameterDTO> parameters;

  public OperationDefinitionParameters(String operationId, List<ParameterDTO> parameters) {
    this.operationId = operationId;
    this.parameters = parameters;
  }

  public String getOperationId() {
    return operationId;
  }

  public List<ParameterDTO> getParameters() {
    return parameters;
  }
}
