package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.GraphDescriptionReadResolver;
import ai.stapi.identity.UniqueIdentifier;

public class GraphDescriptionReadResolverException extends RuntimeException {

  private GraphDescriptionReadResolverException(String message) {
    super(message);
  }

  public static GraphDescriptionReadResolverException becauseIngoingReadResultIsNotSupported(
      ReadResult readResult,
      GraphDescriptionReadResolver resolver
  ) {
    return new GraphDescriptionReadResolverException(
        "Provided previous Read Result of type '"
            + readResult.getClass().getSimpleName()
            + "' is not supported by resolver '"
            + resolver.getClass().getSimpleName()
            + "'."
    );
  }

  public static GraphDescriptionReadResolverException becauseEdgeDoesNotContainNodeOfGivenType(
      UniqueIdentifier edgeId, String nodeType) {
    return new GraphDescriptionReadResolverException(
        "Edge '" + edgeId.toString() + "' does not contain node of type '" + nodeType + "'."
    );
  }

  public static GraphDescriptionReadResolverException becauseGraphDescriptionTypeIsNotSupported(
      PositiveGraphDescription graphDescription,
      GraphDescriptionReadResolver resolver
  ) {
    return new GraphDescriptionReadResolverException(
        "Graph Description of type '"
            + graphDescription.getClass().getSimpleName()
            + "' is not supported by '"
            + resolver.getClass().getSimpleName()
            + "' resolver."
    );
  }

  public static GraphDescriptionReadResolverException becauseAttributeDoesNotExists(
      UniqueIdentifier graphElementId,
      String attributeName
  ) {
    return new GraphDescriptionReadResolverException(
        "Graph Element with id '" + graphElementId + "' does not contain attribute '"
            + attributeName + "'."
    );
  }

}
