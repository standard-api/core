package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;

public class ExampleNestedDto extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "b78cee2a-1063-49b7-a020-4790931dc85a";
  private ExampleDto childDto;
  private String name;

  public ExampleNestedDto() {
  }

  public ExampleNestedDto(
      String name,
      ExampleDto childDto
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.childDto = childDto;
  }


  public String getName() {
    return name;
  }

  public ExampleDto getChildDto() {
    return childDto;
  }
}
