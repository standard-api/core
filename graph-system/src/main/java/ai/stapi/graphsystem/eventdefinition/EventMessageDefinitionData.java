package ai.stapi.graphsystem.eventdefinition;

import ai.stapi.schema.structuredefinition.StructureDefinitionId;

public class EventMessageDefinitionData {

  private String id;
  private String name;
  private StructureDefinitionId type;
  private String description;

  private EventMessageDefinitionData() {
  }

  public EventMessageDefinitionData(
      String id,
      String name,
      StructureDefinitionId type,
      String description
  ) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public StructureDefinitionId getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }
}
