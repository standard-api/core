package ai.stapi.graphoperations.graphLoader.exceptions;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.utils.Stringifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;

public class GraphLoaderException extends RuntimeException {

  public GraphLoaderException(String becauseMessage) {
    super("Cannot load graph, " + becauseMessage);
  }

  private GraphLoaderException(String becauseMessage, Throwable cause) {
    super("Cannot load graph, " + becauseMessage, cause);
  }

  public static GraphLoaderException becauseThereIsNoGraphElementWithProvidedUuid(
      UniqueIdentifier uuid,
      GraphDescription graphDescription
  ) {
    return new GraphLoaderException(
        "because specified graph element does not exist in graph." +
            "\nUUID: " + uuid +
            "\nGraphDescription: " + graphDescription.getClass().getSimpleName() +
            "\n" + Stringifier.convertToString(graphDescription)
    );
  }

  public static GraphLoaderException becauseThereIsMoreThanOneNodeDescriptionsOnEdgeDescription(
      UniqueIdentifier id, String type) {
    return new GraphLoaderException(
        "because there is more than one node descriptions on edge description." +
            "\nUnique Identifier: " + id +
            "\nEdge type: " + type
    );
  }

  public static GraphLoaderException becauseEdgeDescriptionHadMoreChildNodeDescriptions(
      AbstractEdgeDescription edgeDescription,
      List<String> nodeTypes
  ) {
    var param = (EdgeDescriptionParameters) edgeDescription.getParameters();
    return new GraphLoaderException(
        "because edge description had more child node descriptions." +
            "\nEdge Description ClassName: " + edgeDescription.getClass().getSimpleName() +
            "\nEdge type: " + param.getEdgeType() +
            "\nNode Types: " + String.join(", ", nodeTypes)
    );
  }

  public static GraphLoaderException becauseProvidedOGMDidNotStartWithObjectOGM(
      ObjectGraphMapping objectGraphMapping) {
    return new GraphLoaderException(
        "because provided OGM did not start with object OGM." +
            "\nOGM Class: " + objectGraphMapping.getClass().getSimpleName()
    );
  }
}
