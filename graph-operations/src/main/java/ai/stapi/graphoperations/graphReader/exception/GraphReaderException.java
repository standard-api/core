package ai.stapi.graphoperations.graphReader.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ValueReadResult;
import ai.stapi.identity.UniqueIdentifier;
import org.apache.commons.lang3.StringUtils;

public class GraphReaderException extends RuntimeException {

  private GraphReaderException(String message) {
    super(message);
  }

  public static GraphReaderException becauseStartingElementWasNotFoundInProvidedGraph(
      UniqueIdentifier startElementId,
      String startElementType,
      String startElementGraphType
  ) {
    return new GraphReaderException(
        StringUtils.capitalize(startElementGraphType) + " '"
            + startElementId.toString()
            + "' of type '"
            + startElementType
            + "' not found in provided graph structure."
    );
  }

  public static GraphReaderException becauseStartingElementWasNotFoundInProvidedGraph(
      UniqueIdentifier startElementId) {
    return new GraphReaderException(
        "Graph element with ID ["
            + startElementId.toString()
            + "] was not found in provided graph structure."
    );
  }

  public static GraphReaderException becauseThereIsNoSupportingResolverForMappingPart(
      PositiveGraphDescription graphDescription) {
    return new GraphReaderException(
        "There is no supporting resolver for Graph Description '" + graphDescription.getClass()
            .getSimpleName() + "'."
    );
  }

  public static GraphReaderException becauseThereIsMultipleSupportingResolverForMappingPart(
      PositiveGraphDescription graphDescription) {
    return new GraphReaderException(
        "There is multiple supporting resolvers for Graph Description '"
            + graphDescription.getClass().getSimpleName() + "'."
    );
  }

  public static GraphReaderException becauseGraphDescriptionContainsRemovalGraphDescription(
      GraphDescription description) {
    return new GraphReaderException(
        "Provided "
            + GraphDescription.class.getSimpleName()
            + " contains " + RemovalGraphDescription.class.getSimpleName()
            + " of type '" + description.getClass().getSimpleName()
            + "' and that is not allowed in " + GraphReaderException.class.getSimpleName()
            + "."
    );
  }

  public static GraphReaderException becauseMappingPartReturnsTypeIncompatibleWithNext(
      ObjectGraphMapping currentMappingPart,
      ObjectGraphMapping nextMappingPart
  ) {
    return new GraphReaderException(
        "Mapping part '"
            + currentMappingPart.getClass().getSimpleName()
            + "' returns '"
            + currentMappingPart.getSerializationType()
            + "' ,but following mapping part '"
            + nextMappingPart.getClass().getSimpleName()
            + "' expects '"
            + nextMappingPart.getSerializationType() + "'."
    );
  }

  public static GraphReaderException becauseProvidedGraphDescriptionCanNotBeFirst(
      PositiveGraphDescription graphDescription) {
    return new GraphReaderException(
        "Provided Graph Description of type '"
            + graphDescription.getClass().getSimpleName()
            + "' can't be as first in the composite."
    );
  }

  public static GraphReaderException becauseReadResultsFromLastMappingPartIsEmpty(
      ObjectGraphMapping mappingPart, Integer index) {
    return new GraphReaderException(
        "Read Results from mapping part '"
            + mappingPart.getClass().getSimpleName()
            + "' at index '"
            + index.toString()
            + "' was empty. That is not allowed when reading."
    );
  }

  public static GraphReaderException becauseGraphDescriptionHasMultipleChildren(
      PositiveGraphDescription graphDescription) {
    return new GraphReaderException(
        "Graph Description of type '"
            + graphDescription.getClass().getSimpleName()
            + "' contains multiple children and that is not allowed in Graph Reader."
    );
  }

  public static GraphReaderException becauseLastReadResultsAreNotValues(ReadResult readResult) {
    return new GraphReaderException(
        "When reading value, last Read Result must be of type '"
            + ValueReadResult.class.getSimpleName()
            + "' but was of type '"
            + readResult.getClass().getSimpleName()
            + "' instead."
    );
  }
}
