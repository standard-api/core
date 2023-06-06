package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SpecificGraphOgmDeserializerException extends RuntimeException {

  private SpecificGraphOgmDeserializerException(String message) {
    super(message);
  }

  public static SpecificGraphOgmDeserializerException becauseThereIsMultipleGraphBranches(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return new SpecificGraphOgmDeserializerException(
        "Traversing graph led to multiple branches. That is not allowed for "
            + ObjectGraphMapping.class.getSimpleName()
            + " of type '" + objectGraphMapping.getClass().getSimpleName() + "'."
            + System.lineSeparator()
            + " Given elements: "
            + StringUtils.join(elements.stream().map(TraversableGraphElement::getType), ", ")
    );
  }

  public static SpecificGraphOgmDeserializerException becauseGraphTraversingLedToMultipleBranches(
      String elementType) {
    return new SpecificGraphOgmDeserializerException(
        GraphDescription.class.getSimpleName()
            + " inside " + ObjectObjectGraphMapping.class.getSimpleName()
            + " led from element of type '" + elementType
            + "' to multiple graph branches and that is not allowed."
    );
  }

  public static SpecificGraphOgmDeserializerException becauseGraphReadingAccordingToOgmFailed(
      String currentElementType,
      PositiveGraphDescription declaration,
      GraphReaderException exception
  ) {
    return new SpecificGraphOgmDeserializerException(
        "Traversing graph from element of type '" + currentElementType
            + "' using " + declaration.getClass().getSimpleName()
            + " failed. Ogm doesn't correspond to the actual graph structure saved. "
            + System.lineSeparator()
            + "Cause: " + exception.getMessage()
    );
  }
}
