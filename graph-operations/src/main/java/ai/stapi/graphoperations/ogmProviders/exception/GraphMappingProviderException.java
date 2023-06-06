package ai.stapi.graphoperations.ogmProviders.exception;

import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import ai.stapi.serialization.AbstractSerializableObject;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GraphMappingProviderException extends RuntimeException {

  private GraphMappingProviderException(String message) {
    super(message);
  }

  public static GraphMappingProviderException becauseThereIsNoSupportingSpecificGraphMappingProvider(
      String deserializationType) {
    return new GraphMappingProviderException(
        "There is no supporting "
            + SpecificGraphMappingProvider.class.getSimpleName()
            + " for object with serialization type '"
            + deserializationType + "'."
    );
  }

  public static GraphMappingProviderException becauseThereAreMoreThanOneSpecificGraphMappingProviders(
      String deserializationType,
      List<SpecificGraphMappingProvider> listOfSupportingProviders
  ) {
    return new GraphMappingProviderException(
        "Object of type '"
            + deserializationType
            + "' is supported by multiple providers ("
            + StringUtils.join(listOfSupportingProviders, ", ")
            + ") and that is not allowed."
    );
  }

  public static GraphMappingProviderException becauseProvidedObjectMapDoesNotContainTypeField() {
    return new GraphMappingProviderException(
        "Provided object map does not contain field '"
            + AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE
            + "' with type information."
    );
  }
}