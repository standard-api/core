package ai.stapi.graphoperations.exampleImplementations;

import ai.stapi.serialization.AbstractSerializableObject;
import java.util.List;

public class ExampleDtoWithListOfStrings extends AbstractSerializableObject
    implements NestedDtoInterface {

  public static final String SERIALIZATION_TYPE = "dbadaf01-f13b-4e1b-b8f5-d027e2874cff";
  private List<String> names;

  public ExampleDtoWithListOfStrings() {
  }

  public ExampleDtoWithListOfStrings(List<String> names) {
    super(SERIALIZATION_TYPE);
    this.names = names;
  }

  public List<String> getNames() {
    return names;
  }
}
