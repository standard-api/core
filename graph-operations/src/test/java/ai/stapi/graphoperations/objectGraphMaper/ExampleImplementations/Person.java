package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import java.util.UUID;

public class Person implements ExamplePeopleInterface {

  private final UUID id;
  private final String name;
  private final String surname;
  private final String nationality;

  public Person(
      UUID id,
      String name,
      String surname,
      String nationality
  ) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.nationality = nationality;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public String getNationality() {
    return nationality;
  }

  public UUID getId() {
    return id;
  }
}
