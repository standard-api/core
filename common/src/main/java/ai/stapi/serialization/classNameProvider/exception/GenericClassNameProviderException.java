package ai.stapi.serialization.classNameProvider.exception;

import ai.stapi.serialization.classNameProvider.specific.SpecificClassNameProvider;
import java.util.List;
import java.util.stream.Collectors;

public class GenericClassNameProviderException extends RuntimeException {

  private GenericClassNameProviderException(String message) {
    super(message);
  }

  public static GenericClassNameProviderException becauseNoSupportingSpecificProvider(
      String serializationType) {
    return new GenericClassNameProviderException(
        "\nThere is no "
            + SpecificClassNameProvider.class.getSimpleName()
            + " for serialization type '"
            + serializationType
            + "'.\n"
            + "Either there is some error in configuration or you should create a service which implements:\n" + 
            SpecificClassNameProvider.class + " which supports this serialization type."
    );
  }

  public static GenericClassNameProviderException becauseMoreThanOneSpecificProvider(
      String serializationType,
      List<SpecificClassNameProvider> listOfSupportingResolvers
  ) {
    return new GenericClassNameProviderException(
        "There is multiple supporting "
            + SpecificClassNameProvider.class.getSimpleName()
            + "for serialization type " + serializationType + "'."
            + " Supporting providers: ["
            + listOfSupportingResolvers.stream()
            .map(provider -> provider.getClass().getSimpleName())
            .collect(Collectors.joining(", ")) + "]."
    );
  }
}
