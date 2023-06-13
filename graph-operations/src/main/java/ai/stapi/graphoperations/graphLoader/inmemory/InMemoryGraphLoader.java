package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.EdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.GraphElementQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.GraphLoaderFindAsObjectOutput;
import ai.stapi.graphoperations.graphLoader.GraphLoaderGetAsObjectOutput;
import ai.stapi.graphoperations.graphLoader.GraphLoaderReturnType;
import ai.stapi.graphoperations.graphLoader.exceptions.GraphLoaderException;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class InMemoryGraphLoader implements GraphLoader {

  private final InMemoryGraphRepository inMemoryGraphRepository;
  private final InMemoryGenericSearchOptionResolver searchOptionResolver;
  private final StructureSchemaFinder structureSchemaFinder;
  private final ObjectMapper objectMapper;

  public InMemoryGraphLoader(
      InMemoryGraphRepository inMemoryGraphRepository,
      InMemoryGenericSearchOptionResolver searchOptionResolver,
      StructureSchemaFinder structureSchemaFinder,
      ObjectMapper objectMapper
  ) {
    this.inMemoryGraphRepository = inMemoryGraphRepository;
    this.searchOptionResolver = searchOptionResolver;
    this.structureSchemaFinder = structureSchemaFinder;
    this.objectMapper = objectMapper;
  }

  public InMemoryGraphLoader(
      Graph graph,
      InMemoryGenericSearchOptionResolver searchOptionResolver,
      StructureSchemaFinder structureSchemaFinder,
      ObjectMapper objectMapper
  ) {
    this.inMemoryGraphRepository = new InMemoryGraphRepository(graph);
    this.searchOptionResolver = searchOptionResolver;
    this.structureSchemaFinder = structureSchemaFinder;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<TraversableGraphElement> findAsTraversable(GraphElementQueryDescription graphDescription) {
    var output = this.find(
        graphDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    return output.getGraphLoaderFindAsGraphOutput().getFoundGraphElementIds()
        .stream()
        .map(uuid -> output.getGraphLoaderFindAsGraphOutput().getGraph().loadGraphElement(uuid))
        .toList();
  }

  @Override
  public TraversableGraphElement getAsTraversable(
      UniqueIdentifier elementId,
      GraphElementQueryDescription graphDescription
  ) {
    var graph = this.get(
        elementId,
        graphDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    return graph.getGraph().loadGraphElement(elementId);
  }

  @Override
  public <T> GraphLoaderFindAsObjectOutput<T> find(
      GraphElementQueryDescription graphElementQueryDescription,
      Class<T> objectClass,
      GraphLoaderReturnType... returnTypes
  ) {
    var initialElements = this.getTraversableElements(graphElementQueryDescription);
    var graph = new InMemoryGraphRepository();
    var data = new ArrayList<T>();
    initialElements.stream()
        .map(element -> this.resolveStep(element, graphElementQueryDescription, this.convertReturnTypes(returnTypes)))
        .forEach(
            stepOutput -> {
              graph.merge(stepOutput.getGraph());
              if (!stepOutput.getObject().isEmpty()) {
                data.add(this.objectMapper.convertValue(stepOutput.getObject(), objectClass));
              }
            }
        );

    return new GraphLoaderFindAsObjectOutput<>(
        data,
        initialElements.stream().map(TraversableGraphElement::getId).toList(),
        graph
    );
  }

  private Set<GraphLoaderReturnType> convertReturnTypes(GraphLoaderReturnType[] returnTypes) {
    if (returnTypes.length == 0) {
      return Set.of(GraphLoaderReturnType.OBJECT);
    }
    return Arrays.stream(returnTypes).collect(Collectors.toSet());
  }

  @Override
  public <T> GraphLoaderGetAsObjectOutput<T> get(
      UniqueIdentifier elementId,
      GraphElementQueryDescription graphDescription,
      Class<T> objectClass,
      GraphLoaderReturnType... returnTypes
  ) {
    var element = this.getTraversableGraphElement(elementId, graphDescription);
    var graphLoaderStepOutput = this.resolveStep(
        element,
        graphDescription,
        this.convertReturnTypes(returnTypes)
    );
    return new GraphLoaderGetAsObjectOutput<>(
        this.objectMapper.convertValue(graphLoaderStepOutput.getObject(), objectClass),
        graphLoaderStepOutput.getGraph().traversable()
    );
  }

  public Object getSearchOptionAttributeValue(
      UniqueIdentifier elementId,
      PositiveGraphDescription graphDescription
  ) {
    var element = this.getTraversableGraphElement(elementId, graphDescription);
    return this.resolveSearchOptionStep(
        element,
        graphDescription
    );
  }

  @NotNull
  private TraversableGraphElement getTraversableGraphElement(
      UniqueIdentifier elementId,
      PositiveGraphDescription graphDescription
  ) {
    if (graphDescription instanceof AbstractNodeDescription) {
      var param = (NodeDescriptionParameters) graphDescription.getParameters();
      if (this.inMemoryGraphRepository.nodeExists(elementId, param.getNodeType())) {
        return this.inMemoryGraphRepository.loadNode(elementId, param.getNodeType());
      }
    }
    if (graphDescription instanceof AbstractEdgeDescription) {
      var param = (EdgeDescriptionParameters) graphDescription.getParameters();
      if (this.inMemoryGraphRepository.edgeExists(elementId, param.getEdgeType())) {
        return this.inMemoryGraphRepository.loadEdge(elementId, param.getEdgeType());
      }
    }
    throw GraphLoaderException.becauseThereIsNoGraphElementWithProvidedUuid(
        elementId,
        graphDescription
    );
  }

  private List<TraversableGraphElement> getTraversableElements(GraphElementQueryDescription graphDescription) {
    var searchQueryParameters = graphDescription.getSearchQueryParameters();
    if (graphDescription instanceof AbstractNodeDescription) {
      var params = (NodeDescriptionParameters) graphDescription.getParameters();
      var traversableGraphElements = inMemoryGraphRepository.loadAllNodes(params.getNodeType())
          .stream()
          .map(TraversableGraphElement.class::cast)
          .toList();

      return this.applySearchQueryParameters(
          traversableGraphElements,
          searchQueryParameters,
          graphDescription
      );
    }

    if (graphDescription instanceof AbstractEdgeDescription) {
      var params = (EdgeDescriptionParameters) graphDescription.getParameters();
      var traversableGraphElements = inMemoryGraphRepository.loadAllEdges(params.getEdgeType())
          .stream()
          .map(TraversableGraphElement.class::cast)
          .toList();

      return this.applySearchQueryParameters(
          traversableGraphElements,
          searchQueryParameters,
          graphDescription
      );
    }
    throw new RuntimeException("There should never be any other graph descriptions");
  }

  private List<TraversableGraphElement> applySearchQueryParameters(
      List<TraversableGraphElement> traversableGraphElements,
      SearchQueryParameters searchQueryParameters,
      PositiveGraphDescription positiveGraphDescription
  ) {
    var context = new InMemorySearchResolvingContext(
        traversableGraphElements.stream(),
        this.inMemoryGraphRepository,
        positiveGraphDescription
    );
    for (var filter : searchQueryParameters.getFilterOptions()) {
      context = this.searchOptionResolver.resolve(filter, context);
    }
    for (var sort : searchQueryParameters.getSortOptions()) {
      context = this.searchOptionResolver.resolve(sort, context);
    }
    context.applySort();
    var paginationOption = searchQueryParameters.getPaginationOption();
    if (paginationOption != null) {
      return this.searchOptionResolver.resolve(paginationOption, context).getGraphElements();
    }
    return context.getGraphElements();
  }

  private GraphLoaderStepOutput resolveStep(
      TraversableGraphElement element,
      GraphDescription graphDescription,
      Set<GraphLoaderReturnType> returnTypes
  ) {
    if (element instanceof TraversableNode node) {
      return this.resolveTraversableNode(
          node,
          (AbstractNodeDescription) graphDescription,
          returnTypes
      );
    }
    if (element instanceof TraversableEdge edge) {
      return this.resolveTraversableEdge(
          edge,
          (AbstractEdgeDescription) graphDescription,
          returnTypes
      );
    }
    throw new RuntimeException("There should never be any other graph element.");
  }

  private GraphLoaderStepOutput resolveTraversableNode(
      TraversableNode node,
      AbstractNodeDescription graphDescription,
      Set<GraphLoaderReturnType> returnTypes
  ) {
    var object = new HashMap<String, Object>();
    var graph = new Graph();
    var attributes = this.resolveAttributes(node, graphDescription);
    if (returnTypes.contains(GraphLoaderReturnType.GRAPH)) {
      var inputNode = new Node(node.getId(), node.getType());
      for (Attribute<?> attribute : attributes) {
        inputNode = inputNode.add(attribute);
      }
      graph = graph.with(inputNode);
    }
    var childGraphDescriptions = graphDescription.getChildGraphDescriptions();
    if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
      if (childGraphDescriptions.stream().anyMatch(UuidIdentityDescription.class::isInstance)) {
        object.put("id", node.getId().getId());
      }
      attributes.forEach(attribute -> object.put(attribute.getName(), attribute.getValue()));
    }
    for (var childDescription : childGraphDescriptions) {
      if (childDescription instanceof AbstractEdgeDescription edgeDescription) {
        var parameters = (EdgeDescriptionParameters) edgeDescription.getParameters();
        List<TraversableEdge> edges;
        if (edgeDescription.isIngoing()) {
          edges = node.getIngoingEdges(parameters.getEdgeType());
        } else {
          edges = node.getOutgoingEdges(parameters.getEdgeType());
        }
        SearchQueryParameters searchQueryParameters;
        if (edgeDescription instanceof EdgeQueryDescription edgeQueryDescription) {
          searchQueryParameters = edgeQueryDescription.getSearchQueryParameters();
        } else {
          searchQueryParameters = SearchQueryParameters.from();
        }
        ArrayList<TraversableGraphElement> graphElements = new ArrayList<>(edges);
        var filteredGraphElements = this.applySearchQueryParameters(
            graphElements,
            searchQueryParameters,
            edgeDescription
        );
        var filteredEdges = filteredGraphElements.stream().map(TraversableEdge.class::cast).toList();
        var fieldSchema = this.structureSchemaFinder.getFieldDefinitionOrFallback(
            node.getType(),
            parameters.getEdgeType()
        );
        if (fieldSchema.isList()) {
          var childObjects = new ArrayList<>();
          for (var edge : filteredEdges) {
            var child = this.resolveTraversableEdge(edge, edgeDescription, returnTypes);
            if (returnTypes.contains(GraphLoaderReturnType.GRAPH)) {
              graph = graph.merge(child.getGraph());
            }
            if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
              childObjects.add(child.getObject());
            }
          }
          if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
            if (
                edgeDescription instanceof EdgeQueryDescription edgeQueryDescription
                    && !edgeQueryDescription.isCompact()
            ) {
              object.put(
                  String.format("%sConnections", parameters.getEdgeType()),
                  childObjects
              );
            } else {
              object.put(parameters.getEdgeType(), childObjects);
            }
          }
        } else {
          Map<String, Object> childObject = null;
          if (!filteredEdges.isEmpty()) {
            var edge = filteredEdges.get(0);
            var child = this.resolveTraversableEdge(edge, edgeDescription, returnTypes);
            if (returnTypes.contains(GraphLoaderReturnType.GRAPH)) {
              graph = graph.merge(child.getGraph());
            }
            if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
              childObject = child.getObject();
            }
          }
          if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
            if (
                edgeDescription instanceof EdgeQueryDescription edgeQueryDescription
                    && !edgeQueryDescription.isCompact()
            ) {
              object.put(
                  String.format("%sConnections", parameters.getEdgeType()),
                  childObject
              );
            } else {
              object.put(parameters.getEdgeType(), childObject);
            }
          }
        }
      }
    }

    return new GraphLoaderStepOutput(object, graph);
  }

  private GraphLoaderStepOutput resolveTraversableEdge(
      TraversableEdge edge,
      AbstractEdgeDescription graphDescription,
      Set<GraphLoaderReturnType> returnTypes
  ) {
    var childNodeDescriptions = graphDescription.getChildGraphDescriptions().stream()
        .filter(AbstractNodeDescription.class::isInstance)
        .map(AbstractNodeDescription.class::cast)
        .toList();

    var graph = new Graph();
    var attributes = this.resolveAttributes(edge, graphDescription);
    if (returnTypes.contains(GraphLoaderReturnType.GRAPH)) {
      var nodeFrom = new Node(edge.getNodeFromId(), edge.getNodeFromType());
      var nodeTo = new Node(edge.getNodeToId(), edge.getNodeToType());
      var inputEdge = new Edge(edge.getId(), nodeFrom, edge.getType(), nodeTo);
      for (Attribute<?> attribute : attributes) {
        inputEdge = inputEdge.add(attribute);
      }
      graph = new Graph(nodeFrom, nodeTo, inputEdge);
    }
    Map<String, Object> object = new HashMap<>();
    Map<String, Object> edgesObject = new HashMap<>();
    if (this.isMappingToConnectionObject(graphDescription, returnTypes)) {
        object.put("edges", edgesObject);
        attributes.forEach(
            attribute -> edgesObject.put(attribute.getName(), attribute.getValue())
        );
        if (
            graphDescription.getChildGraphDescriptions().stream().anyMatch(UuidIdentityDescription.class::isInstance)
        ) {
          edgesObject.put("id", edge.getId().getId());
        }
    }
    if (childNodeDescriptions.isEmpty()) {
      return new GraphLoaderStepOutput(object, graph);
    }
    TraversableNode otherNode;
    if (graphDescription.isIngoing()) {
      otherNode = edge.getNodeFrom();
    } else {
      otherNode = edge.getNodeTo();
    }
    var foundNodeDescription = childNodeDescriptions.stream()
        .filter(
            nodeDescription -> ((NodeDescriptionParameters) nodeDescription.getParameters())
                .getNodeType()
                .equals(otherNode.getType())
        ).findFirst();

    if (foundNodeDescription.isPresent()) {
      var graphLoaderStepOutput = this.resolveTraversableNode(
          otherNode,
          foundNodeDescription.get(),
          returnTypes
      );
      if (returnTypes.contains(GraphLoaderReturnType.GRAPH)) {
        graph = graph.merge(graphLoaderStepOutput.getGraph());
      }
      if (returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
        if (
            graphDescription instanceof EdgeQueryDescription edgeQueryDescription
            && !edgeQueryDescription.isCompact()
        ) {
          edgesObject.put("node", graphLoaderStepOutput.getObject());
        } else {
          object = graphLoaderStepOutput.getObject();
        }
      }
      return new GraphLoaderStepOutput(object, graph);
    } else {
      return new GraphLoaderStepOutput(new HashMap<>(), new Graph());
    }
  }

  private List<Attribute<?>> resolveAttributes(
      AttributeContainer graphElement,
      GraphDescription graphDescription
  ) {
    var childGraphDescriptions = graphDescription.getChildGraphDescriptions();
    if (childGraphDescriptions.stream().anyMatch(AllAttributesDescription.class::isInstance)) {
      return graphElement.getVersionedAttributes()
          .getVersionedAttributeList()
          .stream()
          .map(VersionedAttribute::getCurrent)
          .collect(Collectors.toList());
    }
    List<Attribute<?>> list = new ArrayList<>();
    childGraphDescriptions.stream()
        .filter(AbstractAttributeDescription.class::isInstance)
        .map(description -> (AttributeDescriptionParameters) description.getParameters())
        .filter(parameters -> graphElement.containsAttribute(parameters.getAttributeName()))
        .forEach(parameters -> list.add(graphElement.getAttribute(parameters.getAttributeName())));

    return list;
  }

  private Object resolveSearchOptionStep(
      TraversableGraphElement element,
      PositiveGraphDescription graphDescription
  ) {
    if (element instanceof TraversableNode node) {
      return this.resolveNodeSearchOptionStep(
          node,
          (AbstractNodeDescription) graphDescription
      );
    }
    if (element instanceof TraversableEdge edge) {
      return this.resolveEdgeSearchOptionStep(
          edge,
          (AbstractEdgeDescription) graphDescription
      );
    }
    throw new RuntimeException("There should never be any other graph element.");
  }

  private Object resolveNodeSearchOptionStep(
      TraversableNode node,
      AbstractNodeDescription graphDescription
  ) {
    var childDescription = graphDescription.getChildGraphDescriptions().get(0);
    if (childDescription instanceof AbstractEdgeDescription edgeDescription) {
      var edgeParam = (EdgeDescriptionParameters) edgeDescription.getParameters();
      List<TraversableEdge> edges;
      if (edgeDescription.isIngoing()) {
        edges = node.getIngoingEdges(edgeParam.getEdgeType());
      } else {
        edges = node.getOutgoingEdges(edgeParam.getEdgeType());
      }
      SearchQueryParameters searchQueryParameters;
      if (edgeDescription instanceof EdgeQueryDescription edgeQueryDescription) {
        searchQueryParameters = edgeQueryDescription.getSearchQueryParameters();
      } else {
        searchQueryParameters = SearchQueryParameters.from();
      }
      var graphElements = new ArrayList<TraversableGraphElement>(edges);
      var filteredGraphElements = this.applySearchQueryParameters(
          graphElements,
          searchQueryParameters,
          edgeDescription
      );
      return filteredGraphElements.stream()
          .map(edge -> this.resolveEdgeSearchOptionStep((TraversableEdge) edge, edgeDescription))
          .collect(Collectors.toList());
    }
    if (childDescription instanceof AbstractAttributeDescription attributeDescription) {
      var attributeParam = (AttributeDescriptionParameters) attributeDescription.getParameters();
      if (!node.hasAttribute(attributeParam.getAttributeName())) {
        return null;
      }
      return node.getAttribute(attributeParam.getAttributeName());
    }
    if (childDescription instanceof UuidIdentityDescription) {
      return new LeafAttribute<>("id", new IdAttributeValue(node.getId().getId()));
    }
    throw new RuntimeException("Should not ever happen, already validated.");
  }

  private Object resolveEdgeSearchOptionStep(
      TraversableEdge edge,
      AbstractEdgeDescription graphDescription
  ) {
    var childDescription = graphDescription.getChildGraphDescriptions().get(0);
    if (childDescription instanceof AbstractNodeDescription nodeDescription) {
      var nodeParam = (NodeDescriptionParameters) nodeDescription.getParameters();
      TraversableNode otherNode;
      if (graphDescription.isIngoing()) {
        otherNode = edge.getNodeFrom();
      } else {
        otherNode = edge.getNodeTo();
      }
      if (!otherNode.getType().equals(nodeParam.getNodeType())) {
        return null;
      }
      return this.resolveNodeSearchOptionStep(otherNode, nodeDescription);
    }
    if (childDescription instanceof AbstractAttributeDescription attributeDescription) {
      var attributeParam = (AttributeDescriptionParameters) attributeDescription.getParameters();
      if (!edge.hasAttribute(attributeParam.getAttributeName())) {
        return null;
      }
      return edge.getAttribute(attributeParam.getAttributeName());
    }
    if (childDescription instanceof UuidIdentityDescription) {
      return new LeafAttribute<>("id", new IdAttributeValue(edge.getId().getId()));
    }
    throw new RuntimeException("Should not ever happen, already validated.");
  }

  private boolean isMappingToConnectionObject(
      AbstractEdgeDescription graphDescription, 
      Set<GraphLoaderReturnType> returnTypes
  ) {
    if (!returnTypes.contains(GraphLoaderReturnType.OBJECT)) {
      return false;
    }
    if (graphDescription instanceof EdgeQueryDescription edgeQueryDescription) {
      return !edgeQueryDescription.isCompact();
    }
    return false;
  }
  private static class GraphLoaderStepOutput {



    private final Map<String, Object> object;
    private final Graph graph;

    public GraphLoaderStepOutput(Map<String, Object> object, Graph graph) {
      this.object = object;
      this.graph = graph;
    }

    public GraphLoaderStepOutput() {
      this(new HashMap<>(), new Graph());
    }

    public Map<String, Object> getObject() {
      return object;
    }

    public Graph getGraph() {
      return graph;
    }
  }
}
