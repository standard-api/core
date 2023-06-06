package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;
import java.util.List;

public class ExampleDtoWithListAttribute extends AbstractSerializableObject
    implements NestedDtoInterface {

  public static final String SERIALIZATION_TYPE = "ExampleDtoWithListAttribute";
  private List<String> tags;

  public ExampleDtoWithListAttribute() {
  }

  public ExampleDtoWithListAttribute(List<String> tags) {
    super(SERIALIZATION_TYPE);
    this.tags = tags;
  }


  public List<String> getTags() {
    return tags;
  }
}
