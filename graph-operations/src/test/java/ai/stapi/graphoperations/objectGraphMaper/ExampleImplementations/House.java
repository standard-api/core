package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import java.util.List;
import java.util.UUID;

public class House {

  private final UUID id;
  private final String address;
  private final List<Person> residents;
  private final List<String> listOfRooms;

  public House(
      UUID id,
      List<Person> residents, String address, List<String> listOfRooms) {
    this.id = id;
    this.residents = residents;
    this.address = address;
    this.listOfRooms = listOfRooms;
  }

  public String getAddress() {
    return address;
  }

  public List<String> getListOfRooms() {
    return listOfRooms;
  }

  public List<Person> getResidents() {
    return residents;
  }

  public UUID getId() {
    return id;
  }
}
