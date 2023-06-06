package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphLanguage.graphDescription.AbstractGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.graphReader.readResults.AbstractGraphElementReadResult;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

public abstract class AbstractSpecificGraphToMapMapper implements SpecificGraphToMapMapper {

  protected final GraphReader graphReader;
  protected final GraphToMapObjectMapper graphToMapObjectMapper;
  protected final GenericGraphMappingProvider genericGraphMappingProvider;

  protected AbstractSpecificGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    this.graphReader = graphReader;
    this.graphToMapObjectMapper = graphToMapObjectMapper;
    this.genericGraphMappingProvider = genericGraphMappingProvider;
  }

  protected List<TraversableGraphElement> traverseGraph(
      Declaration declaration,
      List<TraversableGraphElement> elements,
      Graph graph
  ) throws GraphReaderException {
    if (!(declaration instanceof AbstractGraphDescription)) {
      return elements;
    }
    if (declaration instanceof NullGraphDescription) {
      return elements;
    }
    return elements.stream()
        .map(graphElement -> {
          if (this.isDescriptionDescribingElement(graphElement, declaration)) {
            return this.graphReader.read(
                graphElement.getId(),
                (PositiveGraphDescription) declaration,
                graph.traversable()
            );
          } else {
            return this.graphReader.readFromUncertainFirstElement(
                graphElement.getId(),
                (PositiveGraphDescription) declaration,
                graph.traversable()
            );
          }
        }).flatMap(List::stream)
        .map(AbstractGraphElementReadResult.class::cast)
        .map(AbstractGraphElementReadResult::getGraphElement)
        .toList();
  }

  private boolean isDescriptionDescribingElement(
      TraversableGraphElement element,
      Declaration declaration
  ) {
    if (!(declaration instanceof GraphDescription description)) {
      return false;
    }
    if (element instanceof TraversableNode
        && description instanceof AbstractNodeDescription) {
      var nodeParam = (NodeDescriptionParameters) description.getParameters();
      return element.getType().equals(nodeParam.getNodeType());

    }
    if (element instanceof TraversableEdge
        && description instanceof AbstractEdgeDescription) {
      var edgeParam = (EdgeDescriptionParameters) description.getParameters();
      return element.getType().equals(edgeParam.getEdgeType());

    }
    return false;
  }

  protected Object traverseGraphToValue(
      Declaration declaration,
      TraversableGraphElement element,
      Graph graph
  ) {
    if (!(declaration instanceof AbstractGraphDescription)) {
      return null;
    }
    if (declaration instanceof NullGraphDescription) {
      return null;
    }
    List<Object> values;
    try {
      values = this.graphReader.readValuesFromUncertainFirstElement(
          element.getId(),
          (PositiveGraphDescription) declaration,
          graph.traversable()
      );
    } catch (GraphDescriptionReadResolverException exception) {
      return null;
    }
    if (values.size() > 1) {
      throw GraphToMapObjectMapperException.becauseLeafCannotLeadToMultipleValues();
    }
    if (values.isEmpty()) {
      return null;
    }
    return values.get(0);
  }

  protected String resolveSerializationType(TraversableGraphElement element) {
    if (element instanceof TraversableNode node) {
      return node.getType();
    }
    if (element instanceof TraversableEdge edge) {
      return edge.getNodeToType();
    }
    throw new NotImplementedException(
        "Unknown element type: '%s'"
            .formatted(element.getClass().getSimpleName())
    );
  }
}
