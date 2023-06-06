package ai.stapi.graphoperations.serializationTypeProvider.exception;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;

public class GenericSerializationTypeProviderException extends RuntimeException {

  private GenericSerializationTypeProviderException(String message) {
    super(message);
  }

  public static GenericSerializationTypeProviderException becauseThereIsNotSupportingProvider(
      TraversableGraphElement element) {
    return new GenericSerializationTypeProviderException(
        "There is not supporting provider for '"
            + element.getClass().getSimpleName()
            + "' graph element '"
            + element.getId()
            + "' of type '"
            + element.getType()
            + "'."
    );
  }
}
