package ai.stapi.serialization.classNameProvider.exception;

public class SerializableObjectClassNameProviderException extends RuntimeException {

  private SerializableObjectClassNameProviderException(String message) {
    super(message);
  }


  public static SerializableObjectClassNameProviderException becauseTypeIsNotDefined(
      String serializationType) {
    return new SerializableObjectClassNameProviderException(
        "There is no defined class type for serialization type '" + serializationType + "'."
    );
  }

}
