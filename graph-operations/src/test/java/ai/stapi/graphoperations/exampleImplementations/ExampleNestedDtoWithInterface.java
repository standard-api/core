package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;

public class ExampleNestedDtoWithInterface extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "3a3a042d-1f0b-47c5-acc2-a7e86e2943f9";
  private NestedDtoInterface childDto;
  private String name;

  public ExampleNestedDtoWithInterface() {
  }

  public ExampleNestedDtoWithInterface(
      String name,
      NestedDtoInterface childDto
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.childDto = childDto;
  }


  public String getName() {
    return name;
  }

  public NestedDtoInterface getChildDto() {
    return childDto;
  }
}
