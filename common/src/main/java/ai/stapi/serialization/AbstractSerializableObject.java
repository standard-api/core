package ai.stapi.serialization;

public abstract class AbstractSerializableObject implements SerializableObject {

  public static final String NAME_OF_FIELD_WITH_SERIALIZATION_TYPE = "serializationType";
  private String serializationType;

  protected AbstractSerializableObject() {
  }

  protected AbstractSerializableObject(String serializationType) {
    this.serializationType = serializationType;
  }

  public String getSerializationType() {
    return serializationType;
  }
}

