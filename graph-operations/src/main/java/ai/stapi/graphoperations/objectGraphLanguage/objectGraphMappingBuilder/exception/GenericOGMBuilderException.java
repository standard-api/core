package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.exception;

import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;

public class GenericOGMBuilderException extends RuntimeException {

  private GenericOGMBuilderException(String message) {
    super(message);
  }

  public static GenericOGMBuilderException becauseThereIsNoSupportingBuilder(
      ObjectGraphMapping objectGraphMapping) {
    return new GenericOGMBuilderException(
        "There is no supporting builder for "
            + ObjectGraphMapping.class.getSimpleName()
            + " of type '" + objectGraphMapping.getClass().getSimpleName() + "'."
    );
  }
}
