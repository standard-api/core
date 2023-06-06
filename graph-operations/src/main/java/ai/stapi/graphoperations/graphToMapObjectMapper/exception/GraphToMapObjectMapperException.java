package ai.stapi.graphoperations.graphToMapObjectMapper.exception;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.SpecificGraphToMapMapper;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GraphToMapObjectMapperException extends RuntimeException {

  private GraphToMapObjectMapperException(String message) {
    super(message);
  }

  public static GraphToMapObjectMapperException becauseNodeTypeIsNotValidSerializationType(
      String nodeType
  ) {
    return new GraphToMapObjectMapperException(
        "Node of type '%s' is not valid serialization type.".formatted(nodeType)
    );
  }

  public static GraphToMapObjectMapperException becauseOgmIsNotSupported(
      ObjectGraphMapping mapping
  ) {
    return new GraphToMapObjectMapperException(
        "Ogm of type '%s' is not supported by any %s."
            .formatted(
                mapping.getClass().getSimpleName(),
                SpecificGraphToMapMapper.class.getSimpleName()
            )
    );
  }

  public static GraphToMapObjectMapperException becauseOgmIsSupportedByMultipleMappers(
      ObjectGraphMapping mapping
  ) {
    return new GraphToMapObjectMapperException(
        "Ogm of type '%s' is supported by multiple %s."
            .formatted(
                mapping.getClass().getSimpleName(),
                SpecificGraphToMapMapper.class.getSimpleName()
            )
    );
  }

  public static GraphToMapObjectMapperException
  becauseObjectCannotBeResolvedFromMultipleStartingElements(
      List<TraversableGraphElement> elements
  ) {
    return new GraphToMapObjectMapperException(
        """
            Object cannot be resolved from multiple starting elements.
            Element types: %s
            """.formatted(
            StringUtils.join(
                elements.stream()
                    .map(TraversableGraphElement::getType)
                    .toArray(),
                ", "
            )
        ));
  }

  public static GraphToMapObjectMapperException
  becauseObjectsCannotSplitIntoMultipleBranchedInsideObjectOgm() {
    return new GraphToMapObjectMapperException(
        "Objects cannot split into multiple branched inside ObjectOgm.");
  }

  public static GraphToMapObjectMapperException becauseLeafCannotLeadToMultipleValues() {
    return new GraphToMapObjectMapperException("Leaf cannot lead to multiple values.");
  }

  public static GraphToMapObjectMapperException becauseProvidedGraphIsEmpty() {
    return new GraphToMapObjectMapperException(
        "Provided graph was empty."
    );
  }

}
