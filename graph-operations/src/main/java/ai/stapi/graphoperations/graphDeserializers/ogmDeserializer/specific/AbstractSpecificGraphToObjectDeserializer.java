package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception.GenericGraphOgmDeserializerException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.graphReader.readResults.AbstractGraphElementReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSpecificGraphToObjectDeserializer implements SpecificGraphToObjectDeserializer {

  protected final GraphReader graphReader;
  protected final GenericGraphToObjectDeserializer genericDeserializer;
  protected final GenericSerializationTypeByNodeProvider serializationTypeProvider;
  protected final GenericGraphMappingProvider mappingProvider;

  protected AbstractSpecificGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    this.graphReader = graphReader;
    this.genericDeserializer = genericDeserializer;
    this.serializationTypeProvider = serializationTypeProvider;
    this.mappingProvider = mappingProvider;
  }

  protected TraversableGraphElement traverseSingleGraphBranch(
      UniqueIdentifier firstElementId,
      String firstElementType,
      Declaration declaration,
      InMemoryGraphRepository contextualGraph
  ) {
    var traversable = this.loadElement(firstElementId, firstElementType, contextualGraph);
    if (!(declaration instanceof PositiveGraphDescription graphDescription)) {
      return traversable;
    }
    if (declaration instanceof NullGraphDescription) {
      return traversable;
    }
    List<ReadResult> results;
    try {
      if (this.doesGraphDescriptionDescribesGivenGraphElement(traversable, graphDescription)) {
        results = this.graphReader.read(
            firstElementId,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      } else {
        results = this.graphReader.readFromUncertainFirstElement(
            firstElementId,
            firstElementType,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      }
      if (results.size() > 1) {
        throw GenericGraphOgmDeserializerException.becauseGraphTraversingLedToMultipleBranches(
            traversable.getType());
      }
      if (results.isEmpty()) {
        throw GenericGraphOgmDeserializerException.becauseGraphTraversingLedToNoBranches(
            traversable.getType()
        );
      }
      return this.convertReadResultToTraversableGraphElement(results.get(0));
    } catch (GraphReaderException exception) {
      throw GenericGraphOgmDeserializerException.becauseGraphReadingAccordingToOgmFailed(
          traversable.getType(),
          (PositiveGraphDescription) declaration,
          exception
      );
    }
  }

  protected List<TraversableGraphElement> traverseMultipleGraphBranch(
      UniqueIdentifier firstElementId,
      String firstElementType,
      Declaration declaration,
      InMemoryGraphRepository contextualGraph
  ) {
    var traversable = this.loadElement(firstElementId, firstElementType, contextualGraph);
    if (!(declaration instanceof PositiveGraphDescription graphDescription)) {
      return List.of(traversable);
    }
    if (declaration instanceof NullGraphDescription) {
      return List.of(traversable);
    }
    List<ReadResult> results;
    try {
      if (this.doesGraphDescriptionDescribesGivenGraphElement(traversable, graphDescription)) {
        results = this.graphReader.read(
            firstElementId,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      } else {
        results = this.graphReader.readFromUncertainFirstElement(
            firstElementId,
            firstElementType,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      }
      return results.stream()
          .map(this::convertReadResultToTraversableGraphElement)
          .toList();
    } catch (GraphReaderException exception) {
      throw GenericGraphOgmDeserializerException.becauseGraphReadingAccordingToOgmFailed(
          traversable.getType(),
          (PositiveGraphDescription) declaration,
          exception
      );
    }
  }

  protected Object traverseSingleGraphBranchToValue(
      UniqueIdentifier firstElementId,
      String firstElementType,
      Declaration declaration,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingFieldStrategy
  ) {
    var traversable = this.loadElement(firstElementId, firstElementType, contextualGraph);
    if (!(declaration instanceof PositiveGraphDescription graphDescription)) {
      return "";
    }
    if (declaration instanceof NullGraphDescription) {
      return "";
    }
    List<Object> results;
    try {
      if (this.doesGraphDescriptionDescribesGivenGraphElement(traversable, graphDescription)) {
        results = this.graphReader.readValues(
            firstElementId,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      } else {
        results = this.graphReader.readValuesFromUncertainFirstElement(
            firstElementId,
            firstElementType,
            (PositiveGraphDescription) declaration,
            contextualGraph
        );
      }
      if (results.size() > 1) {
        throw GenericGraphOgmDeserializerException.becauseGraphTraversingLedToMultipleBranches(
            traversable.getType());
      }
      if (missingFieldStrategy.equals(MissingTraversalTargetResolvingStrategy.LENIENT)) {
        if (results.isEmpty()) {
          return null;
        }
      }
      return results.get(0);
    } catch (GraphReaderException exception) {
      throw GenericGraphOgmDeserializerException.becauseGraphReadingAccordingToOgmFailed(
          traversable.getType(),
          (PositiveGraphDescription) declaration,
          exception
      );
    }
  }

  protected boolean doesGraphDescriptionDescribesGivenGraphElement(
      TraversableGraphElement element,
      GraphDescription graphDescription
  ) {
    if (element instanceof TraversableNode node
        && graphDescription instanceof NodeDescription nodeDescription) {
      var parameters = (NodeDescriptionParameters) nodeDescription.getParameters();
      return node.getType().equals(parameters.getNodeType());
    }
    if (element instanceof TraversableEdge edge
        && graphDescription instanceof AbstractEdgeDescription edgeDescription) {
      var parameters = (EdgeDescriptionParameters) edgeDescription.getParameters();
      return edge.getType().equals(parameters.getEdgeType());
    }
    return false;
  }

  protected TraversableGraphElement loadElement(
      UniqueIdentifier graphElementId,
      String graphElementType,
      InMemoryGraphRepository graph
  ) {
    try {
      return graph.loadNode(graphElementId, graphElementType);

    } catch (NodeNotFound ignored) {
    }
    try {
      return graph.loadEdge(graphElementId, graphElementType);
    } catch (NodeNotFound ignored) {
    }
    throw GenericGraphOgmDeserializerException.becauseElementWasNotFound(graphElementId);

  }

  protected TraversableGraphElement convertReadResultToTraversableGraphElement(
      ReadResult readResult
  ) {
    if (readResult instanceof AbstractGraphElementReadResult elementReadResult) {
      return elementReadResult.getGraphElement();
    }
    throw GenericGraphOgmDeserializerException.givenReadResultIsNotGraphElement();
  }

  protected GraphDescription updateLastGraphDescription(GraphDescription lastGraphDescription,
                                                        GraphDescription lastlyTraversedDescription) {
    if (lastlyTraversedDescription instanceof NullGraphDescription) {
      return lastGraphDescription;
    }
    return GraphDescriptionBuilder.getGraphDescriptionAsStream(lastlyTraversedDescription)
        .filter(description -> description.getChildGraphDescriptions().isEmpty())
        .toList()
        .get(0);
  }

  protected List<TraversableGraphElement> ensureElementsAreNodes(
      List<TraversableGraphElement> traversedField, GraphDescription lastGraphDescription) {
    if (traversedField.isEmpty()) {
      return new ArrayList<>();
    }
    if (!(traversedField.get(0) instanceof TraversableEdge)) {
      return traversedField;
    }
    if (lastGraphDescription instanceof OutgoingEdgeDescription) {
      return traversedField.stream()
          .map(TraversableEdge.class::cast)
          .map(traversableEdge -> (TraversableGraphElement) traversableEdge.getNodeTo())
          .toList();
    }
    if (lastGraphDescription instanceof IngoingEdgeDescription) {
      return traversedField.stream()
          .map(TraversableEdge.class::cast)
          .map(traversableEdge -> (TraversableGraphElement) traversableEdge.getNodeFrom())
          .toList();
    }
    return traversedField;
  }
}
