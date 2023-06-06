package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;
import java.util.Arrays;
import java.util.List;

public class ExampleNestedDtoWithListOfInterfaces extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "16880151-4c68-488c-8fdc-26ca1eeccf91";
  private List<NestedDtoInterface> childDtos;
  private String name;

  public ExampleNestedDtoWithListOfInterfaces() {
  }

  public ExampleNestedDtoWithListOfInterfaces(
      String name,
      List<NestedDtoInterface> childDtos
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.childDtos = childDtos;
  }

  public ExampleNestedDtoWithListOfInterfaces(
      String name,
      NestedDtoInterface... childDtos
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.childDtos = Arrays.stream(childDtos).toList();
  }

  public String getName() {
    return name;
  }

  public List<NestedDtoInterface> getChildDtos() {
    return childDtos;
  }
}
