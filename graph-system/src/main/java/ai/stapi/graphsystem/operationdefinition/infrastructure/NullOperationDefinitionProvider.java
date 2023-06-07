package ai.stapi.graphsystem.operationdefinition.infrastructure;

import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.List;

public class NullOperationDefinitionProvider implements OperationDefinitionProvider {

  @Override
  public List<OperationDefinitionDTO> provideAll() {
    return List.of();
  }

  @Override
  public OperationDefinitionDTO provide(String operationId)
      throws CannotProvideOperationDefinition {
    throw new CannotProvideOperationDefinition(operationId);
  }

  @Override
  public boolean contains(String operationId) {
    return false;
  }
}
