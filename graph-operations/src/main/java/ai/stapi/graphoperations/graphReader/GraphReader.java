package ai.stapi.graphoperations.graphReader;

import ai.stapi.graphoperations.graphLanguage.GraphBaseTypes;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.graphReader.readResults.ValueReadResult;
import ai.stapi.graph.Graph;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.GraphDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GraphReader {

  private final List<GraphDescriptionReadResolver> graphDescriptionResolvers;

  public GraphReader(List<GraphDescriptionReadResolver> graphDescriptionResolvers) {
    this.graphDescriptionResolvers = graphDescriptionResolvers;
  }

  public List<ReadResult> read(
      UniqueIdentifier startElementId,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    if (graphDescription.getChildGraphDescriptions().size() > 1) {
      throw GraphReaderException.becauseGraphDescriptionHasMultipleChildren(graphDescription);
    }
    this.ensureGraphDescriptionContainsOnlyUpsertDescriptions(graphDescription);
    var firstReadResult = this.createFirstReadResult(
        startElementId,
        graphDescription,
        contextualGraph
    );
    if (graphDescription.getChildGraphDescriptions().size() == 0) {
      return firstReadResult;
    }
    return resolveRestOfTheGraphDescriptions(
        firstReadResult,
        graphDescription,
        contextualGraph
    );
  }

  public List<ReadResult> readFromUncertainFirstElement(
      UniqueIdentifier startElementId,
      PositiveGraphDescription graphDescriptionForNextElement,
      InMemoryGraphRepository contextualGraph
  ) {
    if (graphDescriptionForNextElement.getChildGraphDescriptions().size() > 1) {
      throw GraphReaderException.becauseGraphDescriptionHasMultipleChildren(
          graphDescriptionForNextElement);
    }
    this.ensureGraphDescriptionContainsOnlyUpsertDescriptions(graphDescriptionForNextElement);
    var firstReadResult = this.guessFirstReadResult(
        startElementId,
        contextualGraph
    );
    return this.resolveAllOfTheGraphDescriptions(
        firstReadResult,
        graphDescriptionForNextElement,
        contextualGraph
    );
  }

  public List<ReadResult> read(
      UniqueIdentifier startElementId,
      PositiveGraphDescription firstElementDescription,
      PositiveGraphDescription graphDescription,
      Graph contextualGraph
  ) {
    if (graphDescription.getChildGraphDescriptions().size() > 1) {
      throw GraphReaderException.becauseGraphDescriptionHasMultipleChildren(graphDescription);
    }
    this.ensureGraphDescriptionContainsOnlyUpsertDescriptions(graphDescription);
    InMemoryGraphRepository traversable = contextualGraph.traversable();
    var firstReadResult = this.createFirstReadResult(
        startElementId,
        firstElementDescription,
        traversable
    );
    if (graphDescription.getChildGraphDescriptions().size() == 0) {
      return firstReadResult;
    }
    return resolveRestOfTheGraphDescriptions(
        firstReadResult,
        graphDescription,
        traversable
    );
  }

  public <T> List<T> readValues(
      UniqueIdentifier startElementId,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    var results = this.read(startElementId, graphDescription, contextualGraph);
    if (results.size() == 0) {
      return (List<T>) results;
    }
    if (!(results.get(0) instanceof ValueReadResult)) {
      throw GraphReaderException.becauseLastReadResultsAreNotValues(results.get(0));
    }
    return results.stream()
        .map(ValueReadResult.class::cast)
        .map(result -> (T) result.getValue())
        .toList();
  }

  public <T> List<T> readValuesFromUncertainFirstElement(
      UniqueIdentifier startElementId,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    var results =
        this.readFromUncertainFirstElement(startElementId, graphDescription, contextualGraph);
    if (results.size() == 0) {
      return (List<T>) results;
    }
    if (!(results.get(0) instanceof ValueReadResult)) {
      throw GraphReaderException.becauseLastReadResultsAreNotValues(results.get(0));
    }
    return results.stream()
        .map(result -> (ValueReadResult) result)
        .map(result -> (T) result.getValue())
        .toList();
  }

  @NotNull
  private List<ReadResult> resolveRestOfTheGraphDescriptions(
      List<ReadResult> firstReadResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    var currentDescription = graphDescription;
    do {
      currentDescription =
          (PositiveGraphDescription) currentDescription.getChildGraphDescriptions().get(0);
      if (currentDescription.getChildGraphDescriptions().size() > 1) {
        throw GraphReaderException.becauseGraphDescriptionHasMultipleChildren(graphDescription);
      }
      firstReadResult =
          this.resolveGraphDescription(firstReadResult, currentDescription, contextualGraph);
    } while (currentDescription.getChildGraphDescriptions().size() > 0);
    return firstReadResult;
  }

  @NotNull
  private List<ReadResult> resolveAllOfTheGraphDescriptions(
      List<ReadResult> firstReadResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    var currentDescription = graphDescription;
    while (currentDescription != null) {
      if (currentDescription.getChildGraphDescriptions().size() > 1) {
        throw GraphReaderException.becauseGraphDescriptionHasMultipleChildren(graphDescription);
      }
      firstReadResult =
          this.resolveGraphDescription(firstReadResult, currentDescription, contextualGraph);
      if (currentDescription.getChildGraphDescriptions().size() == 0) {
        break;
      }
      currentDescription =
          (PositiveGraphDescription) currentDescription.getChildGraphDescriptions().get(0);
    }
    return firstReadResult;
  }

  @NotNull
  private List<ReadResult> createFirstReadResult(
      UniqueIdentifier startElementId,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    List<ReadResult> readResult;
    if (graphDescription instanceof NodeDescription nodeDescription) {
      var parameters = (NodeDescriptionParameters) nodeDescription.getParameters();
      if (!contextualGraph.nodeExists(startElementId, parameters.getNodeType())) {
        throw GraphReaderException.becauseStartingElementWasNotFoundInProvidedGraph(
            startElementId,
            parameters.getNodeType(),
            GraphBaseTypes.NODE_TYPE
        );
      }
      readResult = List.of(
          new NodeReadResult(contextualGraph.loadNode(startElementId, parameters.getNodeType())));
    } else if (graphDescription instanceof AbstractEdgeDescription edgeDescription) {
      var parameters = (EdgeDescriptionParameters) edgeDescription.getParameters();
      if (!contextualGraph.edgeExists(startElementId, parameters.getEdgeType())) {
        throw GraphReaderException.becauseStartingElementWasNotFoundInProvidedGraph(
            startElementId,
            parameters.getEdgeType(),
            GraphBaseTypes.EDGE_TYPE
        );
      }
      readResult = List.of(
          new EdgeReadResult(contextualGraph.loadEdge(startElementId, parameters.getEdgeType())));
    } else {
      throw GraphReaderException.becauseProvidedGraphDescriptionCanNotBeFirst(graphDescription);
    }
    return readResult;
  }

  @NotNull
  private List<ReadResult> guessFirstReadResult(
      UniqueIdentifier startElementId,
      InMemoryGraphRepository contextualGraph
  ) {
    List<ReadResult> readResult = null;
    try {
      var node = contextualGraph.loadNode(startElementId);
      readResult = List.of(new NodeReadResult(node));
    } catch (RuntimeException ignored) {
    }
    try {
      var edge = contextualGraph.loadEdge(startElementId);
      readResult = List.of(new EdgeReadResult(edge));
    } catch (RuntimeException ignore) {
    }

    if (readResult == null) {
      throw GraphReaderException.becauseStartingElementWasNotFoundInProvidedGraph(
          startElementId);
    }
    return readResult;
  }

  @NotNull
  private List<ReadResult> resolveGraphDescription(
      List<ReadResult> readResults,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository graph
  ) {
    var resolver = this.getSupportingResolver(graphDescription);
    List<ReadResult> flatReadResults = new ArrayList<>();
    for (ReadResult readResult : readResults) {
      var results = resolver.resolve(
          readResult,
          graphDescription,
          graph
      );
      flatReadResults.addAll(results);
    }
    return flatReadResults;
  }

  private void ensureGraphDescriptionContainsOnlyUpsertDescriptions(
      PositiveGraphDescription graphDescription) {
    var removalDescription = GraphDescriptionBuilder.getGraphDescriptionAsStream(graphDescription)
        .filter(description -> description instanceof RemovalGraphDescription)
        .findAny();
    removalDescription.ifPresent(
        description -> {
          throw GraphReaderException.becauseGraphDescriptionContainsRemovalGraphDescription(
              description);
        }
    );
  }

  private GraphDescriptionReadResolver getSupportingResolver(PositiveGraphDescription mappingPart) {
    var supportingResolvers = this.graphDescriptionResolvers.stream()
        .filter(resolver -> resolver.supports(mappingPart))
        .toList();

    if (supportingResolvers.size() == 0) {
      throw GraphReaderException.becauseThereIsNoSupportingResolverForMappingPart(mappingPart);
    }
    if (supportingResolvers.size() > 1) {
      throw GraphReaderException.becauseThereIsMultipleSupportingResolverForMappingPart(
          mappingPart);
    }
    return supportingResolvers.get(0);
  }

}
