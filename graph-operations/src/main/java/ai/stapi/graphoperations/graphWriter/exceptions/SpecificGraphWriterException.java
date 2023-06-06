package ai.stapi.graphoperations.graphWriter.exceptions;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphWriter.specific.AttributeGraphWriter;

public class SpecificGraphWriterException extends RuntimeException {

  private SpecificGraphWriterException(String message) {
    super(message);
  }

  public static SpecificGraphWriterException becauseGivenEdgeDirectionIsNotSupported(
      GraphDescription graphDescription) {
    return new SpecificGraphWriterException(
        "Given edge direction '"
            + graphDescription.getClass().getSimpleName()
            + "' is not supported."
    );
  }

  public static GenericGraphWriterException becauseEdgeCanNotBeFirstElementInGraph() {
    return new GenericGraphWriterException("Graph can not start with edge.");
  }

  public static SpecificGraphWriterException becauseAttributeDescriptionDoesNotContainValue(
      GraphDescription graphDescription) {
    return new SpecificGraphWriterException(
        "Provided attribute description '"
            + graphDescription.getClass().getSimpleName()
            + "' does not contain value."
    );
  }

  public static SpecificGraphWriterException becauseAttributeTypeIsNotSupported(
      GraphDescription graphDescription) {
    return new SpecificGraphWriterException(
        "Provided attribute description '"
            + graphDescription.getClass().getSimpleName()
            + "' is not recognized by '" + AttributeGraphWriter.class.getSimpleName()
            + "'. Add it to 'getAttributeValue' method."
    );
  }

  public static SpecificGraphWriterException becauseProvidedConstantIsNotUuid(Object value) {
    return new SpecificGraphWriterException(
        "Provided constant is not of type UUID. Provided constant: '" + value + "'."
    );
  }

  public static SpecificGraphWriterException becauseProvidedAttributeValueDoesNotContainAnyConstantDescription(
      GraphDescription graphDescription) {
    return new SpecificGraphWriterException(
        "'" + graphDescription.getClass().getSimpleName()
            + "' does not contain any constant description among it's children."
    );
  }

  public static SpecificGraphWriterException becauseRemovalDescriptionDoesNotHaveUuidDescriptionAsChild(
      GraphDescription graphDescription
  ) {
    return new SpecificGraphWriterException(
        "Removal description '"
            + graphDescription.getClass().getSimpleName()
            + "' does not contain any '"
            + UuidIdentityDescription.class.getSimpleName()
            + "' as child ,and that is not allowed."
    );
  }
}
