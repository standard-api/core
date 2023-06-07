package ai.stapi.graphsystem.operationdefinition.infrastructure;

import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinitions;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

public class AdHocOperationDefinitionProvider implements OperationDefinitionProvider {

  private final GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader;
  private final ScopeCacher scopeCacher;

  public AdHocOperationDefinitionProvider(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher
  ) {
    this.genericAdHocModelDefinitionsLoader = genericAdHocModelDefinitionsLoader;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public List<OperationDefinitionDTO> provideAll() {
    return this.getOperationDefinitions();
  }

  @Override
  public OperationDefinitionDTO provide(
      String operationId
  ) throws CannotProvideOperationDefinition {
    return this.getOperationDefinitions().stream()
        .filter(operation -> operation.getId().equals(operationId))
        .findFirst()
        .orElseThrow(() -> new CannotProvideOperationDefinition(operationId));
  }

  @Override
  public boolean contains(String operationId) {
    return this.getOperationDefinitions().stream().anyMatch(
        operation -> operation.getId().equals(operationId)
    );
  }

  private List<OperationDefinitionDTO> getOperationDefinitions() {
    return this.scopeCacher.getCachedOrCompute(
        AdHocOperationDefinitionProvider.class,
        this::load
    );
  }

  private List<OperationDefinitionDTO> load(ScopeOptions scopeOptions) {
    var createOperation = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "CreateOperationDefinition",
            OperationDefinitionDTO.class
        ).stream()
        .toList();

    var addParameterOnOperation = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "AddParameterOnOperationDefinition",
            AddParameterOnOperationDefinitionDTO.class
        ).stream()
        .toList();

    var addParameterWithoutTarget = addParameterOnOperation.stream()
        .filter(map -> this.isContainedInFoundOperations(createOperation, map))
        .toList();

    if (!addParameterWithoutTarget.isEmpty()) {
      throw new CannotProvideOperationDefinitions(
          String.format(
              "Cannot provide operation definitions for scope: %s with tags: %s.%n%s%n%s",
              scopeOptions.getScopes(),
              scopeOptions.getTags(),
              "There were some AddParameterOnOperationDefinition JSONs which eiter did "
                  + "not have id specified or it was missing in found OperationDefinitions.",
              "Missing ids: " + addParameterWithoutTarget.stream()
                  .map(AddParameterOnOperationDefinitionDTO::getId)
                  .toList()
          )
      );
    }

    var groupedParams = addParameterOnOperation
        .stream()
        .collect(Collectors.groupingBy(AddParameterOnOperationDefinitionDTO::getId));

    return createOperation.stream().map(operation -> {
      var addedParameters = groupedParams.get(operation.getId());
      if (addedParameters == null) {
        return operation;
      }
      var newParameters = addedParameters.stream()
          .map(AddParameterOnOperationDefinitionDTO::getParameter)
          .flatMap(List::stream)
          .toList();

      operation.addParameters(newParameters);
      return operation;
    }).toList();
  }

  private boolean isContainedInFoundOperations(
      List<OperationDefinitionDTO> createOperation,
      AddParameterOnOperationDefinitionDTO addParameterDTO
  ) {
    return addParameterDTO.getId() == null || createOperation.stream().noneMatch(
        operation -> operation.getId().equals(addParameterDTO.getId())
    );
  }

  private static class AddParameterOnOperationDefinitionDTO {

    private String id;
    private List<ParameterDTO> parameter;

    private AddParameterOnOperationDefinitionDTO() {
    }

    public AddParameterOnOperationDefinitionDTO(String id, List<ParameterDTO> parameter) {
      this.id = id;
      this.parameter = parameter;
    }

    public String getId() {
      return id;
    }

    public List<ParameterDTO> getParameter() {
      return parameter;
    }
  }
}
