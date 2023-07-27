package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal.AbstractRemovalDescriptionBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import ai.stapi.utils.Stringifier;

public class GraphDescriptionBuilderException extends RuntimeException {

  private GraphDescriptionBuilderException(String message) {
    super(message);
  }

  public static GraphDescriptionBuilderException becauseEdgeDirectionIsNotSupported(
      EdgeDirection direction) {
    return new GraphDescriptionBuilderException(
        "Edge direction '" + direction.name() + "' is not supported by the builder.");
  }

  public static GraphDescriptionBuilderException becauseDescriptionTypeIsNotSupported(
      GraphDescription graphDescription) {
    return new GraphDescriptionBuilderException(
        "Graph Description of type '"
            + graphDescription.getClass().getSimpleName()
            + "' is not supported by the builder here."
    );
  }

  public static GraphDescriptionBuilderException becauseProvidedValueIsNotPrimitiveType(
      Object value) {
    throw new GraphDescriptionBuilderException(
        "Provided value is not of primitive type." +
            System.lineSeparator() +
            "Value: " +
            System.lineSeparator() +
            Stringifier.convertToString(value)

    );
  }

  public static GraphDescriptionBuilderException becauseValueTypeIsNotSupported(Object value) {
    return new GraphDescriptionBuilderException(
        "Attribute value of type '" + value.getClass().getSimpleName() + "' is not supported.");
  }

  public static GraphDescriptionBuilderException becauseDescriptionTypeIsSupportedByMultipleBuilders(
      GraphDescription graphDescription) {
    return new GraphDescriptionBuilderException(
        "Graph Description '"
            + graphDescription.getClass().getSimpleName()
            + "' is supported by multiple builders."
    );
  }

  public static GraphDescriptionBuilderException becauseThereAreNoBuilders() {
    return new GraphDescriptionBuilderException(
        "There are no specific builders inside the builder.");
  }

  public static GraphDescriptionBuilderException becauseThereAreNoGraphElementsBuilders() {
    return new GraphDescriptionBuilderException(
        "There are no node or edge builders in this or parent builder branches."
    );
  }

  public static GraphDescriptionBuilderException becauseThereAreNoGraphBuildersWithGivenType(
      Class<? extends SpecificGraphDescriptionBuilder> builderType) {
    return new GraphDescriptionBuilderException(
        "There are no builders with "
            + SpecificGraphDescriptionBuilder.class.getSimpleName()
            + " of type '"
            + builderType.getSimpleName()
            + "'."
    );
  }

  public static GraphDescriptionBuilderException becauseFirstGraphDescriptionIsForRemoval(
      SpecificGraphDescriptionBuilder localSpecificBuilder) {
    return new GraphDescriptionBuilderException(
        "First builder '"
            + localSpecificBuilder.getClass().getSimpleName()
            + "' inherits '"
            + AbstractRemovalDescriptionBuilder.class.getSimpleName()
            + "' and that is not allowed when calling 'getOnlyPositiveGraphDescriptions()'."
    );
  }
  
  public static GraphDescriptionBuilderException becauseToAddToDeepestGraphDescriptionItMustBeSinglePath(
      GraphDescription mainGraphDescription
  ) {
    return new GraphDescriptionBuilderException(
        String.format(
            "Cannot add child descriptions to composite graph description to the end." +
                "Main graph description has to be single path.%nInvalid main description: %s",
            Stringifier.convertToString(mainGraphDescription)
        )
    );
  }
}
