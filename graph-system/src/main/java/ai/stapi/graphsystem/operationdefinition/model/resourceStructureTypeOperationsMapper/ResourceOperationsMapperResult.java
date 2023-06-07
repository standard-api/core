package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import java.util.ArrayList;
import java.util.List;

public class ResourceOperationsMapperResult {

  private final List<OperationDefinitionDTO> operations;
  private final List<OperationDefinitionParameters> newParameters;

  public ResourceOperationsMapperResult(
      List<OperationDefinitionDTO> operations,
      List<OperationDefinitionParameters> newParameters
  ) {
    this.operations = operations;
    this.newParameters = newParameters;
  }

  public ResourceOperationsMapperResult(
      List<OperationDefinitionDTO> operations
  ) {
    this(operations, List.of());
  }

  public ResourceOperationsMapperResult(
      OperationDefinitionDTO operation
  ) {
    this(List.of(operation));
  }

  public ResourceOperationsMapperResult(
      OperationDefinitionParameters newParameter
  ) {
    this(List.of(), List.of(newParameter));
  }

  public ResourceOperationsMapperResult() {
    this(List.of(), List.of());
  }

  public ResourceOperationsMapperResult merge(ResourceOperationsMapperResult other) {
    var mergedOperations = new ArrayList<>(this.operations);
    mergedOperations.addAll(other.operations);
    var mergedParameters = new ArrayList<>(this.newParameters);
    mergedParameters.addAll(other.newParameters);
    return new ResourceOperationsMapperResult(
        mergedOperations,
        mergedParameters
    );
  }

  public List<OperationDefinitionDTO> getOperations() {
    return operations;
  }

  public List<OperationDefinitionParameters> getNewParameters() {
    return newParameters;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return this.operations.isEmpty() && this.newParameters.isEmpty();
  }
}
