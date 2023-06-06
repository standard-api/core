package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import java.util.UUID;

public class UpdateHouseOccupantCommand {

  private UUID id;
  private Person person;

  public UpdateHouseOccupantCommand(UUID id,
      Person person
  ) {
    this.id = id;
    this.person = person;
  }

  public UUID getId() {
    return id;
  }

  public Person getPerson() {
    return person;
  }
}
