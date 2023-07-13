package ai.stapi.graph;

import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.EdgeWithSameIdAndTypeAlreadyExists;
import ai.stapi.graph.exceptions.MoreThanOneNodeOfTypeFoundException;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeOfTypeNotFoundException;
import ai.stapi.graph.exceptions.NodeWithSameIdAndTypeAlreadyExists;
import ai.stapi.graph.exceptions.OneOrBothNodesOnEdgeDoesNotExist;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.inMemoryGraph.DeduplicateOptions;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.inMemoryGraph.exceptions.CannotCreateGraphWithOtherThanGraphElements;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.LinkedMap;

public class Graph {

  private ImmutableMap<GloballyUniqueIdentifier, Node> nodeMap;
  private ImmutableMap<GloballyUniqueIdentifier, Edge> edgeMap;

  private ImmutableMap<String, Long> nodeTypeCounts;

  private ImmutableMap<String, Long> edgeTypeCounts;

  public Graph() {
    this.nodeMap = ImmutableMap.of();
    this.edgeMap = ImmutableMap.of();
    this.nodeTypeCounts = ImmutableMap.of();
    this.edgeTypeCounts = ImmutableMap.of();
  }

  public Graph(AttributeContainer... graphElements) {
    this();
    var newInMemoryGraph = this.withAll(graphElements);
    this.nodeMap = ImmutableMap.copyOf(newInMemoryGraph.nodeMap);
    this.edgeMap = ImmutableMap.copyOf(newInMemoryGraph.edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(newInMemoryGraph.nodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(newInMemoryGraph.edgeTypeCounts);
  }

  private Graph(
      Map<GloballyUniqueIdentifier, Node> nodeMap,
      Map<GloballyUniqueIdentifier, Edge> edgeMap,
      Map<String, Long> nodeTypeCounts,
      Map<String, Long> edgeTypeCounts
  ) {
    this.nodeMap = ImmutableMap.copyOf(nodeMap);
    this.edgeMap = ImmutableMap.copyOf(edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(nodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(edgeTypeCounts);
  }

  private Graph(
      Map<GloballyUniqueIdentifier, Node> nodeMap,
      Map<GloballyUniqueIdentifier, Edge> edgeMap
  ) {
    HashMap<String, Long> newNodeTypeCounts = new HashMap<>();
    HashMap<String, Long> newEdgeTypeCounts = new HashMap<>();

    nodeMap.values().forEach(
        node -> newNodeTypeCounts.put(
            node.getType(),
            newNodeTypeCounts.getOrDefault(node.getType(), 0L) + 1
        )
    );
    edgeMap.values().forEach(
        edge -> newEdgeTypeCounts.put(
            edge.getType(),
            newEdgeTypeCounts.getOrDefault(edge.getType(), 0L) + 1
        )
    );
    this.nodeMap = ImmutableMap.copyOf(nodeMap);
    this.edgeMap = ImmutableMap.copyOf(edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(newNodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(newEdgeTypeCounts);
  }

  public static Graph unsafe(
      Map<GloballyUniqueIdentifier, Node> nodeMap,
      Map<GloballyUniqueIdentifier, Edge> edgeMap
  ) {
    return new Graph(nodeMap, edgeMap);
  }

  public InMemoryGraphRepository traversable() {
    return new InMemoryGraphRepository(this);
  }

  public Graph with(Node node) {
    if (this.nodeExists(node.getId(), node.getType())) {
      throw new NodeWithSameIdAndTypeAlreadyExists(node.getId(), node.getType());
    }

    Map<GloballyUniqueIdentifier, Node> newNodeMap = new LinkedMap<>(this.nodeMap);
    newNodeMap.put(
        new GloballyUniqueIdentifier(node.getId(), node.getType()),
        node
    );

    Map<String, Long> newNodeTypeCounts = new LinkedMap<>(this.nodeTypeCounts);
    newNodeTypeCounts.put(
        node.getType(),
        newNodeTypeCounts.getOrDefault(
            node.getType(),
            0L
        ) + 1
    );

    return new Graph(
        newNodeMap,
        this.edgeMap,
        newNodeTypeCounts,
        this.edgeTypeCounts
    );
  }

  public Graph with(Edge edge) {
    this.ensureEdgeWithSameIdAndTypeDoesNotExistsAlready(edge.getId(), edge.getType());
    this.ensureContainsBothNodesOnEdge(edge);

    var newEdgeMap = new LinkedMap<>(this.edgeMap);
    newEdgeMap.put(new GloballyUniqueIdentifier(edge.getId(), edge.getType()), edge);

    var newEdgeTypeCounts = new LinkedMap<>(this.edgeTypeCounts);
    newEdgeTypeCounts.put(
        edge.getType(),
        newEdgeTypeCounts.getOrDefault(edge.getType(), 0L) + 1
    );

    return new Graph(
        this.nodeMap,
        newEdgeMap,
        this.nodeTypeCounts,
        newEdgeTypeCounts
    );
  }

  private void ensureEdgeWithSameIdAndTypeDoesNotExistsAlready(UniqueIdentifier id, String edgeType) {
    if (this.edgeExists(id, edgeType)) {
      throw new EdgeWithSameIdAndTypeAlreadyExists(id.getId(), edgeType);
    }
  }

  public Graph withAll(AttributeContainer... graphElements) {
    Graph newGraph = this;
    var invalidObjects = Arrays.stream(graphElements)
        .filter(graphElement ->
            !(graphElement instanceof Node)
                && !(graphElement instanceof Edge))
        .count();
    if (invalidObjects > 0) {
      throw new CannotCreateGraphWithOtherThanGraphElements();
    }
    for (AttributeContainer graphElement : graphElements) {
      if (graphElement instanceof Node node) {
        newGraph = newGraph.with(node);
      }
      if (graphElement instanceof Edge edge) {
        newGraph = newGraph.with(edge);
      }
    }
    return newGraph;
  }

  public Node getNode(UniqueIdentifier uniqueIdentifier, String nodeType) {
    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(uniqueIdentifier, nodeType);
    if (!this.nodeMap.containsKey(globallyUniqueIdentifier)) {
      throw new NodeNotFound(uniqueIdentifier);
    }
    var foundNode = this.nodeMap.get(globallyUniqueIdentifier);
    if (foundNode == null || !foundNode.getType().equals(nodeType)) {
      throw new NodeNotFound(uniqueIdentifier, nodeType);
    }
    return foundNode;
  }

  public Edge getEdge(UniqueIdentifier id, String edgeType) {
    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(id, edgeType);
    if (!this.edgeMap.containsKey(globallyUniqueIdentifier)) {
      throw new EdgeNotFound(id);
    }
    var foundEdge = this.edgeMap.get(globallyUniqueIdentifier);
    if (foundEdge == null || !foundEdge.getType().equals(edgeType)) {
      throw new EdgeNotFound(id, edgeType);
    }
    return foundEdge;
  }


  public Node getExactlyOneNodeOfType(String nodeType) {
    var foundNodes = this.getAllNodes(nodeType);
    if (foundNodes.isEmpty()) {
      throw new NodeOfTypeNotFoundException(nodeType);
    }
    if (foundNodes.size() > 1) {
      throw new MoreThanOneNodeOfTypeFoundException(nodeType);
    }
    return foundNodes.get(0);
  }

  public List<Node> getAllNodes() {
    return this.nodeMap.values().stream().toList();
  }

  public List<Node> getAllNodes(String nodeType) {
    return this.nodeMap.values().stream()
        .filter(node -> node.getType().equals(nodeType))
        .toList();
  }

  public List<Edge> getAllEdges() {
    return this.edgeMap.values().stream().toList();
  }

  public List<Edge> loadAllEdges(String edgeType) {
    return this.edgeMap.values().stream()
        .filter(edge -> edge.getType().equals(edgeType))
        .toList();
  }

  public Graph replace(Node node) {
    var newNodeMap = new LinkedMap<>(this.nodeMap);
    var newNodeTypeCounts = new LinkedMap<>(this.nodeTypeCounts);

    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(node.getId(), node.getType());
    var oldNode = this.nodeMap.get(globallyUniqueIdentifier);

    newNodeMap.put(globallyUniqueIdentifier, node);

    if (newNodeTypeCounts.containsKey(Objects.requireNonNull(oldNode).getType())) {
      long oldCount = newNodeTypeCounts.get(oldNode.getType());
      newNodeTypeCounts.put(oldNode.getType(), oldCount - 1);
    }

    if (newNodeTypeCounts.containsKey(node.getType())) {
      long newCount = newNodeTypeCounts.get(node.getType());
      newNodeTypeCounts.put(node.getType(), newCount + 1);
    } else {
      newNodeTypeCounts.put(node.getType(), 1L);
    }

    return new Graph(
        newNodeMap,
        this.edgeMap,
        newNodeTypeCounts,
        this.edgeTypeCounts
    );
  }

  public Graph removeNode(UniqueIdentifier id, String nodeType) {
    if (!this.nodeExists(id, nodeType)) {
      return this;
    }

    var newEdgeMap = new LinkedMap<>(this.edgeMap);
    Set<TraversableEdge> inAndOutEdgesForNode = this.traversable().findInAndOutEdgesForNode(
        id,
        nodeType
    );
    inAndOutEdgesForNode.forEach(
        traversableEdge -> newEdgeMap.remove(
            new GloballyUniqueIdentifier(traversableEdge.getId(), traversableEdge.getType())
        )
    );

    var newNodeMap = new LinkedMap<>(this.nodeMap);
    newNodeMap.remove(new GloballyUniqueIdentifier(id, nodeType));

    return new Graph(
        newNodeMap,
        newEdgeMap
    );
  }

  public Graph removeNode(NodeForRemoval nodeForRemoval) {
    return this.removeNode(nodeForRemoval.getGraphElementId(),
        nodeForRemoval.getGraphElementType());
  }

  public boolean nodeExists(UniqueIdentifier id, String type) {
    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(id, type);
    return this.nodeMap.containsKey(globallyUniqueIdentifier)
        && Objects.requireNonNull(this.nodeMap.get(globallyUniqueIdentifier)).getType().equals(type);
  }

  public boolean containsNodeOfType(String nodeType) {
    return this.nodeTypeCounts.containsKey(nodeType);
  }

  public boolean edgeExists(UniqueIdentifier id, String type) {
    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(id, type);
    return this.edgeMap.containsKey(globallyUniqueIdentifier)
        && Objects.requireNonNull(this.edgeMap.get(globallyUniqueIdentifier)).getType().equals(type);
  }

  public List<NodeTypeInfo> getNodeTypeInfos() {
    return this.nodeTypeCounts.entrySet().stream()
        .map(entry -> new NodeTypeInfo(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  public List<EdgeTypeInfo> getEdgeTypeInfos() {
    return this.edgeTypeCounts.entrySet().stream()
        .map(entry -> new EdgeTypeInfo(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  public List<NodeInfo> getNodeInfosBy(String nodeType) {
    return this.nodeMap.values().stream()
        .filter(graphNode -> graphNode.getType().equals(nodeType))
        .map(graphNode ->
            new NodeInfo(
                graphNode.getId(),
                graphNode.getType(),
                this.traversable().loadNode(graphNode.getId(), graphNode.getType())
                    .getSortingNameWithNodeTypeFallback()
            )
        ).sorted(Comparator.comparing(NodeInfo::getName))
        .collect(Collectors.toList());
  }

  public Graph replace(Edge edge) {
    var newEdgeMap = new LinkedMap<>(this.edgeMap);
    newEdgeMap.put(new GloballyUniqueIdentifier(edge.getId(), edge.getType()), edge);

    return new Graph(
        this.nodeMap,
        newEdgeMap,
        this.nodeTypeCounts,
        this.edgeTypeCounts
    );
  }

  public Graph removeEdge(UniqueIdentifier edgeId, String edgeType) {
    if (!this.edgeExists(edgeId, edgeType)) {
      return this;
    }

    var globallyUniqueIdentifier = new GloballyUniqueIdentifier(edgeId, edgeType);
    var newEdgeMap = new LinkedMap<>(this.edgeMap);
    newEdgeMap.remove(globallyUniqueIdentifier);

    Map<String, Long> newEdgeTypeCounts = new LinkedMap<>(this.edgeTypeCounts);
    newEdgeTypeCounts.put(
        edgeType,
        newEdgeTypeCounts.getOrDefault(edgeType, 1L) - 1
    );

    return new Graph(
        this.nodeMap,
        newEdgeMap,
        this.nodeTypeCounts,
        newEdgeTypeCounts
    );
  }

  public Graph removeEdge(EdgeForRemoval edgeForRemoval) {
    return this.removeEdge(edgeForRemoval.getGraphElementId(),
        edgeForRemoval.getGraphElementType());
  }

  public Edge findEdgeByTypeAndNodes(
      String edgeType,
      NodeIdAndType nodeFromIdAndType,
      NodeIdAndType nodeToIdAndType
  ) {
    return this.edgeMap.values().stream()
        .filter(edge ->
            edge.getType().equals(edgeType)
                && edge.getNodeFromIdAndType().equals(nodeFromIdAndType)
                && edge.getNodeToIdAndType().equals(nodeToIdAndType)
        ).map(edge -> this.getEdge(edge.getId(), edge.getType()))
        .findFirst()
        .orElse(null);
  }

  public Graph merge(Graph otherGraph) {
    var newGraph = this;
    for (Node node : otherGraph.nodeMap.values()) {
      newGraph = newGraph.mergeNodeById(node);
    }
    for (Edge edge : otherGraph.edgeMap.values()) {
      newGraph = newGraph.merge(edge);
    }
    return newGraph;
  }

  public Graph removeGraphElements(
      GraphElementForRemoval... graphElementsForRemoval
  ) {
    return this.removeGraphElements(Arrays.stream(graphElementsForRemoval).toList());
  }

  public Graph removeGraphElements(
      List<GraphElementForRemoval> graphElementsForRemoval
  ) {
    var newGraph = this;
    for (GraphElementForRemoval graphElementForRemoval : graphElementsForRemoval) {
      newGraph = newGraph.removeGraphElement(graphElementForRemoval);
    }
    return newGraph;
  }

  public Graph removeGraphElement(GraphElementForRemoval graphElementForRemoval) {
    var newGraph = this;
    if (graphElementForRemoval instanceof NodeForRemoval nodeForRemoval) {
      newGraph = this.removeNode(nodeForRemoval);
    }
    if (graphElementForRemoval instanceof EdgeForRemoval edgeForRemoval) {
      newGraph = this.removeEdge(edgeForRemoval);
    }
    return newGraph;
  }


  public Graph merge(Edge otherEdge) {
    if (!this.nodeExists(
        otherEdge.getNodeFromId(),
        otherEdge.getNodeFromType()
    ) || !this.nodeExists(
        otherEdge.getNodeToId(),
        otherEdge.getNodeToType()
    )) {
      throw new OneOrBothNodesOnEdgeDoesNotExist();
    }
    var graph = this;
    var otherEdgeIdentifier = new GloballyUniqueIdentifier(otherEdge.getId(), otherEdge.getType());
    var foundEdge = graph.edgeMap.get(otherEdgeIdentifier);
    if (foundEdge == null) {
      var newEdgeMap = new LinkedMap<>(graph.edgeMap);
      newEdgeMap.put(otherEdgeIdentifier, otherEdge);

      Map<String, Long> newEdgeTypeCounts = new LinkedMap<>(graph.edgeTypeCounts);
      newEdgeTypeCounts.put(
          otherEdge.getType(),
          newEdgeTypeCounts.getOrDefault(otherEdge.getType(), 0L) + 1
      );

      return new Graph(
          graph.nodeMap,
          newEdgeMap,
          graph.nodeTypeCounts,
          newEdgeTypeCounts
      );
    } else {
      var newMergedEdge = foundEdge.mergeOverwrite(otherEdge);
      var newEdgeMap = new LinkedMap<>(graph.edgeMap);
      newEdgeMap.put(new GloballyUniqueIdentifier(newMergedEdge.getId(), newMergedEdge.getType()), newMergedEdge);

      return new Graph(
          graph.nodeMap,
          newEdgeMap,
          graph.nodeTypeCounts,
          graph.edgeTypeCounts
      );
    }
  }

  private void ensureContainsBothNodesOnEdge(Edge edge) {
    if (
        !this.nodeExists(edge.getNodeFromId(), edge.getNodeFromType()) 
            || !this.nodeExists(edge.getNodeToId(), edge.getNodeToType())
    ) {
      throw new OneOrBothNodesOnEdgeDoesNotExist();
    }
  }

  public Graph merge(
      Graph otherGraph,
      DeduplicateOptions options
  ) {
    if (options.equals(DeduplicateOptions.SAME_EDGE_TYPES_BETWEEN_SAME_NODES)) {
      return this.mergeWithEdgeDeduplication(otherGraph);
    }
    return this.merge(otherGraph);
  }

  private Graph mergeWithEdgeDeduplication(Graph otherGraph) {
    var newGraph = this;
    for (Node node : otherGraph.getAllNodes()) {
      newGraph = newGraph.mergeNodeById(node);
    }

    for (Edge otherGraphEdge : otherGraph.getAllEdges()) {

      List<Edge> localGraphEdges = newGraph.getAllEdges().stream().filter(processedEdge ->
          processedEdge.getNodeFromId().equals(otherGraphEdge.getNodeFromId()) &&
              processedEdge.getNodeToId().equals(otherGraphEdge.getNodeToId()) &&
              processedEdge.getType().equals(otherGraphEdge.getType())
      ).collect(Collectors.toList());

      if (localGraphEdges.size() > 1) {
        throw GraphEdgesCannotBeMerged.becauseThereIsMultipleEdgesGivenEdgeCouldBeMergeWith(
            localGraphEdges.stream().map(Edge::getId).toList()
        );
      }
      if (localGraphEdges.isEmpty()) {
        newGraph = newGraph.merge(otherGraphEdge);
      }
      if (localGraphEdges.size() == 1) {
        var newEdge =
            (Edge) localGraphEdges.get(0).mergeAttributesWithAttributesOf(otherGraphEdge);
        newGraph = newGraph.merge(newEdge);
      }
    }
    return newGraph;
  }

  public Graph mergeNodeById(Node otherNode) {
    var newNodeMap = new LinkedMap<>(this.nodeMap);
    var newNodeTypeCounts = new LinkedMap<>(this.nodeTypeCounts);

    var foundNode = newNodeMap.get(new GloballyUniqueIdentifier(otherNode.getId(), otherNode.getType()));
    Node newNode;
    if (foundNode == null) {
      newNode = otherNode;
      newNodeTypeCounts.put(
          newNode.getType(),
          newNodeTypeCounts.getOrDefault(newNode.getType(), 0L) + 1
      );
    } else {
      newNode = foundNode.mergeOverwrite(otherNode);
    }
    newNodeMap.put(new GloballyUniqueIdentifier(newNode.getId(), newNode.getType()), newNode);

    return new Graph(
        newNodeMap,
        this.edgeMap,
        newNodeTypeCounts,
        this.edgeTypeCounts
    );
  }

  public static class GloballyUniqueIdentifier {

    private final UniqueIdentifier uniqueIdentifier;
    private final String elementType;

    public GloballyUniqueIdentifier(UniqueIdentifier uniqueIdentifier, String elementType) {
      this.uniqueIdentifier = uniqueIdentifier;
      this.elementType = elementType;
    }

    public UniqueIdentifier getUniqueIdentifier() {
      return uniqueIdentifier;
    }

    public String getElementType() {
      return elementType;
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof GloballyUniqueIdentifier otherIdentifier)) {
        return false;
      }

      if (!this.getUniqueIdentifier().equals(otherIdentifier.getUniqueIdentifier())) {
        return false;
      }
      return this.getElementType().equals(otherIdentifier.getElementType());
    }

    @Override
    public int hashCode() {
      int result = this.getUniqueIdentifier().hashCode();
      result = 31 * result + this.getElementType().hashCode();
      return result;
    }
  }
}
