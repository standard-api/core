package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;

public class ExampleDto extends AbstractSerializableObject implements NestedDtoInterface {

  public static final String SERIALIZATION_TYPE = "fb6b6720-6ff7-4d62-bb47-84f888171fe9";
  private String name;

  public ExampleDto() {
  }

  public ExampleDto(String name) {
    super(SERIALIZATION_TYPE);
    this.name = name;
  }


  public String getName() {
    return name;
  }
}
