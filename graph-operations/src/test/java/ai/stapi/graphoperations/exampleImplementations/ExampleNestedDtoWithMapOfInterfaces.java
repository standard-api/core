package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;
import java.util.Map;

public class ExampleNestedDtoWithMapOfInterfaces extends AbstractSerializableObject {

  public static final String SERIALIZATION_TYPE = "85b11aa7-b4d2-4006-af8c-704cc6f2db52";
  private String name;
  private Map<String, NestedDtoInterface> childDtos;

  public ExampleNestedDtoWithMapOfInterfaces() {
  }

  public ExampleNestedDtoWithMapOfInterfaces(
      String name,
      Map<String, NestedDtoInterface> childDtos
  ) {
    super(SERIALIZATION_TYPE);
    this.name = name;
    this.childDtos = childDtos;
  }

  public String getName() {
    return name;
  }

  public Map<String, NestedDtoInterface> getChildDtos() {
    return childDtos;
  }
}
