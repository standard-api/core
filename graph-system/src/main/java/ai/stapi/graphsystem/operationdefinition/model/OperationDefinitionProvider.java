package ai.stapi.graphsystem.operationdefinition.model;

import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import java.util.List;

public interface OperationDefinitionProvider {

  List<OperationDefinitionDTO> provideAll();

  OperationDefinitionDTO provide(String operationId) throws CannotProvideOperationDefinition;

  boolean contains(String operationId);
}
