package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.OgmGraphElementTypes;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphoperations.serializationTypeProvider.specific.SpecificSerializationTypeProvider;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GenericGraphOgmDeserializerException extends RuntimeException {

  private GenericGraphOgmDeserializerException(String message) {
    super(message);
  }

  public static GenericGraphOgmDeserializerException becauseNodeTypeIsNotSupported(
      String nodeType) {
    return new GenericGraphOgmDeserializerException(
        "There are no supporting "
            + SpecificSerializationTypeProvider.class.getSimpleName()
            + " provider for node of type '"
            + nodeType + "'."
    );
  }

  public static GenericGraphOgmDeserializerException becauseNodeTypeIsSupportedByMultipleDeserializers(
      String nodeType,
      List<String> supportingDeserializerClassNames
  ) {
    return new GenericGraphOgmDeserializerException(
        "Node of type '" + nodeType
            + "' is supported by multiple deserializers: ["
            + StringUtils.join(supportingDeserializerClassNames, ", ")
            + "]."
    );
  }

  public static GenericGraphOgmDeserializerException becauseDeserializationHasToStartFromObjectGraphMapping(
      String providedNodeType) {
    return new GenericGraphOgmDeserializerException(
        "Deserialization has to start from "
            + ObjectObjectGraphMapping.class.getSimpleName()
            + " serialized under node '"
            + OgmGraphElementTypes.OGM_OBJECT_NODE
            + "'. Provided node type was: '"
            + providedNodeType + "'."
    );
  }

  public static GenericGraphOgmDeserializerException becauseThereIsMultipleGraphBranchesButMappingIsNotForList() {
    return new GenericGraphOgmDeserializerException(
        "Graph description led to multiple graph branches but provided "
            + ObjectGraphMapping.class.getSimpleName()
            + " did not point to list."
    );
  }

  public static GenericGraphOgmDeserializerException becauseGraphTraversingLedToMultipleBranches(
      String elementType) {
    return new GenericGraphOgmDeserializerException(
        GraphDescription.class.getSimpleName()
            + " inside " + ObjectObjectGraphMapping.class.getSimpleName()
            + " led from element of type '" + elementType
            + "' to multiple graph branches and that is not allowed."
    );
  }

  public static GenericGraphOgmDeserializerException becauseGraphReadingAccordingToOgmFailed(
      String currentElementType,
      PositiveGraphDescription declaration,
      GraphReaderException exception
  ) {
    return new GenericGraphOgmDeserializerException(
        "Traversing graph from element of type '" + currentElementType
            + "' using " + declaration.getClass().getSimpleName()
            + " failed. Ogm doesn't correspond to the actual graph structure saved. "
            + System.lineSeparator()
            + "Cause: " + exception.getMessage()
    );
  }

  public static GenericGraphOgmDeserializerException becauseElementWasNotFound(
      UniqueIdentifier graphElementId) {
    return new GenericGraphOgmDeserializerException(
        "Graph element with id [" + graphElementId
            + "] was not found in graph. This should have never happen!"
    );
  }

  public static GenericGraphOgmDeserializerException givenReadResultIsNotGraphElement() {
    return new GenericGraphOgmDeserializerException(
        ReadResult.class.getSimpleName() + " does not contain traversable graph element in it."
    );
  }

  public static GenericGraphOgmDeserializerException becauseGraphTraversingLedToNoBranches(
      String type) {
    return new GenericGraphOgmDeserializerException(
        GraphDescription.class.getSimpleName()
            + " inside " + ObjectObjectGraphMapping.class.getSimpleName()
            + " led from element of type '" + type
            + "' to no graph branches and that is not allowed."
    );
  }
}
