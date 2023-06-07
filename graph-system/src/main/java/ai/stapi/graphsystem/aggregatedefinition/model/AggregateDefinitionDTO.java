package ai.stapi.graphsystem.aggregatedefinition.model;

import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class AggregateDefinitionDTO {

  private String id;
  private String name;
  private String description;
  private List<CommandHandlerDefinitionDTO> command;
  private StructureDefinitionId structure;

  protected AggregateDefinitionDTO() {
  }

  public AggregateDefinitionDTO(
      String id,
      String name,
      String description,
      List<CommandHandlerDefinitionDTO> command,
      StructureDefinitionId structure
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.command = command;
    this.structure = structure;
  }

  public AggregateDefinitionDTO(
      String id,
      String name,
      String description,
      StructureDefinitionId structure
  ) {
    this(id, name, description, new ArrayList<>(), structure);
  }

  public AggregateDefinitionDTO addCommandHandlerDefinition(
      CommandHandlerDefinitionDTO commandHandlerDefinition
  ) {
    this.command.add(commandHandlerDefinition);
    return this;
  }

  @JsonIgnore
  public boolean containsOperation(String operationId) {
    return this.command.stream().anyMatch(
        someCommand -> someCommand.getOperation().getId().equals(operationId)
    );
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<CommandHandlerDefinitionDTO> getCommand() {
    return command;
  }

  public StructureDefinitionId getStructure() {
    return structure;
  }
}