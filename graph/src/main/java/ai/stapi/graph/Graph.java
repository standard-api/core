package ai.stapi.graph;

import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.EdgeWithSameIdAlreadyExists;
import ai.stapi.graph.exceptions.MoreThanOneNodeOfTypeFoundException;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeOfTypeNotFoundException;
import ai.stapi.graph.exceptions.NodeWithSameIdAlreadyExists;
import ai.stapi.graph.exceptions.OneOrBothNodesOnEdgeDoesNotExist;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.inMemoryGraph.DeduplicateOptions;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.inMemoryGraph.exceptions.CannotCreateGraphWithOtherThanInputElements;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.inMemoryGraph.exceptions.MoreThanOneNodeWithAttributeFoundException;
import ai.stapi.graph.inMemoryGraph.exceptions.NodeWithAttributeNotFound;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
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

  private ImmutableMap<UniqueIdentifier, InputNode> nodeMap;
  private ImmutableMap<UniqueIdentifier, InputEdge> edgeMap;

  private ImmutableMap<String, Long> nodeTypeCounts;

  private ImmutableMap<String, Long> edgeTypeCounts;

  public Graph() {
    this.nodeMap = ImmutableMap.of();
    this.edgeMap = ImmutableMap.of();
    this.nodeTypeCounts = ImmutableMap.of();
    this.edgeTypeCounts = ImmutableMap.of();
  }

  private Graph(
      Map<UniqueIdentifier, InputNode> nodeMap,
      Map<UniqueIdentifier, InputEdge> edgeMap,
      Map<String, Long> nodeTypeCounts,
      Map<String, Long> edgeTypeCounts
  ) {
    this.nodeMap = ImmutableMap.copyOf(nodeMap);
    this.edgeMap = ImmutableMap.copyOf(edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(nodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(edgeTypeCounts);
  }

  public Graph(AttributeContainer... inputGraphElements) {
    this();
    var newInMemoryGraph = this.withAll(inputGraphElements);
    this.nodeMap = ImmutableMap.copyOf(newInMemoryGraph.nodeMap);
    this.edgeMap = ImmutableMap.copyOf(newInMemoryGraph.edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(newInMemoryGraph.nodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(newInMemoryGraph.edgeTypeCounts);
  }

  public Graph(
      Map<UniqueIdentifier, InputNode> nodeMap,
      Map<UniqueIdentifier, InputEdge> edgeMap
  ) {
    HashMap<String, Long> nodeTypeCounts = new HashMap<>();
    HashMap<String, Long> edgeTypeCounts = new HashMap<>();

    nodeMap.values().stream().forEach(
        inputNode -> nodeTypeCounts.put(
            inputNode.getType(),
            nodeTypeCounts.getOrDefault(inputNode.getType(), 0L) + 1
        )
    );
    edgeMap.values().stream().forEach(
        inputNode -> edgeTypeCounts.put(
            inputNode.getType(),
            edgeTypeCounts.getOrDefault(inputNode.getType(), 0L) + 1
        )
    );
    this.nodeMap = ImmutableMap.copyOf(nodeMap);
    this.edgeMap = ImmutableMap.copyOf(edgeMap);
    this.nodeTypeCounts = ImmutableMap.copyOf(nodeTypeCounts);
    this.edgeTypeCounts = ImmutableMap.copyOf(edgeTypeCounts);
  }

  public InMemoryGraphRepository traversable() {
    return new InMemoryGraphRepository(this);
  }

  public Graph withNode(InputNode node) {
    if (this.nodeExists(node.getId())) {
      throw new NodeWithSameIdAlreadyExists(node.getId(), node.getType());
    }

    Map<UniqueIdentifier, InputNode> newNodeMap = new LinkedMap<>(this.nodeMap);
    newNodeMap.put(
        node.getId(),
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

  public Graph withEdge(InputEdge inputEdge) {
    ensureEdgeWithSameIdDoesNotExistsAlready(inputEdge.getId(), inputEdge.getType());
    this.ensureContainsBothNodesOnEdge(inputEdge);

    Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(this.edgeMap);
    InputEdge graphEdge = inputEdge;
    newEdgeMap.put(inputEdge.getId(), graphEdge);

    Map<String, Long> newEdgeTypeCounts = new LinkedMap<>(this.edgeTypeCounts);
    newEdgeTypeCounts.put(
        inputEdge.getType(),
        newEdgeTypeCounts.getOrDefault(inputEdge.getType(), 0L) + 1
    );

    return new Graph(
        this.nodeMap,
        newEdgeMap,
        this.nodeTypeCounts,
        newEdgeTypeCounts
    );
  }

  private void ensureEdgeWithSameIdDoesNotExistsAlready(UniqueIdentifier id, String edgeType) {
    if (this.edgeExists(id)) {
      throw new EdgeWithSameIdAlreadyExists(id.getId(), edgeType);
    }
  }

  public Graph withAll(AttributeContainer... inputGraphElements) {
    Graph newGraph = this;
    var invalidObjects = Arrays.stream(inputGraphElements)
        .filter(inputGraphElement ->
            !(inputGraphElement instanceof InputNode)
                && !(inputGraphElement instanceof InputEdge))
        .count();
    if (invalidObjects > 0) {
      throw new CannotCreateGraphWithOtherThanInputElements();
    }
    for (AttributeContainer inputGraphElement : inputGraphElements) {
      if (inputGraphElement instanceof InputNode inputNode) {
        newGraph = newGraph.withNode(inputNode);
      }
      if (inputGraphElement instanceof InputEdge inputEdge) {
        newGraph = newGraph.withEdge(inputEdge);
      }
    }
    return newGraph;
  }

  public InputNode getNode(UniqueIdentifier UniqueIdentifier, String nodeType) {
    if (!this.nodeMap.containsKey(UniqueIdentifier)) {
      throw new NodeNotFound(UniqueIdentifier);
    }
    var foundNode = this.nodeMap.get(UniqueIdentifier);
    if (foundNode == null || !foundNode.getType().equals(nodeType)) {
      throw new NodeNotFound(UniqueIdentifier, nodeType);
    }
    return foundNode;
  }

  public InputNode getNode(UniqueIdentifier UniqueIdentifier) {
    if (!this.nodeMap.containsKey(UniqueIdentifier)) {
      throw new NodeNotFound(UniqueIdentifier);
    }
    return this.nodeMap.get(UniqueIdentifier);
  }

  public InputEdge getEdge(UniqueIdentifier UniqueIdentifier) {
    if (!this.edgeMap.containsKey(UniqueIdentifier)) {
      throw new EdgeNotFound(UniqueIdentifier);
    }
    return this.edgeMap.get(UniqueIdentifier);
  }

  public InputEdge getEdge(UniqueIdentifier id, String edgeType) {
    if (!this.edgeMap.containsKey(id)) {
      throw new EdgeNotFound(id);
    }
    var foundEdge = this.edgeMap.get(id);
    if (foundEdge == null || !foundEdge.getType().equals(edgeType)) {
      throw new EdgeNotFound(id, edgeType);
    }
    return foundEdge;
  }


  public InputNode getExactlyOneNodeOfType(String nodeType) {
    var foundNodes = this.getAllNodes(nodeType);
    if (foundNodes.isEmpty()) {
      throw new NodeOfTypeNotFoundException(nodeType);
    }
    if (foundNodes.size() > 1) {
      throw new MoreThanOneNodeOfTypeFoundException(nodeType);
    }
    return foundNodes.get(0);
  }

  public List<InputNode> getAllNodes() {
    return this.nodeMap.values().stream().toList();
  }

  public List<InputNode> getAllNodes(String nodeType) {
    return this.nodeMap.values().stream()
        .filter(node -> node.getType().equals(nodeType))
        .toList();
  }

  public InputNode getExactlyOneNodeByAttribute(String attributeName, Object attributeValue) {
    var foundNodesByAttribute = this.nodeMap.values().stream()
        .filter(node -> node.containsAttribute(attributeName, attributeValue))
        .toList();
    if (foundNodesByAttribute.size() > 1) {
      throw new MoreThanOneNodeWithAttributeFoundException();
    }
    if (foundNodesByAttribute.isEmpty()) {
      throw new NodeWithAttributeNotFound();
    }
    return foundNodesByAttribute.get(0);
  }

  public List<InputEdge> getAllEdges() {
    return this.edgeMap.values().stream().toList();
  }

  public List<InputEdge> loadAllEdges() {
    return this.edgeMap.values().stream().toList();
  }

  public List<InputEdge> loadAllEdges(String edgeType) {
    return this.edgeMap.values().stream()
        .filter(edge -> edge.getType().equals(edgeType))
        .toList();
  }

  public Graph replace(InputNode node) {
    Map<UniqueIdentifier, InputNode> newNodeMap = new LinkedMap<>(this.nodeMap);
    Map<String, Long> newNodeTypeCounts = new LinkedMap<>(this.nodeTypeCounts);

    InputNode oldNode = this.nodeMap.get(node.getId());

    newNodeMap.put(node.getId(), node);

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

    Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(this.edgeMap);
    Set<TraversableEdge> inAndOutEdgesForNode = this.traversable().findInAndOutEdgesForNode(
        id,
        nodeType
    );
    inAndOutEdgesForNode.forEach(
        traversableEdge -> newEdgeMap.remove(traversableEdge.getId())
    );

    Map<UniqueIdentifier, InputNode> newNodeMap = new LinkedMap<>(this.nodeMap);
    newNodeMap.remove(id);

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
    return this.nodeMap.containsKey(id)
        && Objects.requireNonNull(this.nodeMap.get(id)).getType().equals(type);
  }

  public boolean nodeExists(UniqueIdentifier id) {
    return this.nodeMap.containsKey(id);
  }

  public boolean containsNodeOfType(String nodeType) {
    return this.nodeTypeCounts.containsKey(nodeType);
  }

  public boolean edgeExists(UniqueIdentifier id, String type) {
    return this.edgeMap.containsKey(id)
        && Objects.requireNonNull(this.edgeMap.get(id)).getType().equals(type);
  }

  public boolean edgeExists(UniqueIdentifier id) {
    return this.edgeMap.containsKey(id);
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

  public Graph replace(InputEdge inputEdge) {
    Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(this.edgeMap);
    newEdgeMap.put(inputEdge.getId(), inputEdge);

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

    Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(this.edgeMap);
    newEdgeMap.remove(edgeId);

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

  public InputEdge findEdgeByTypeAndNodes(
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
    for (InputNode inputNode : otherGraph.nodeMap.values()) {
      newGraph = newGraph.mergeNodeById(inputNode);
    }
    for (InputEdge inputEdge : otherGraph.edgeMap.values()) {
      newGraph = newGraph.mergeEdge(inputEdge);
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


  public Graph mergeEdge(InputEdge otherEdge) {
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
    var foundEdge = graph.edgeMap.get(otherEdge.getId());
    if (foundEdge == null) {
      Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(graph.edgeMap);
      newEdgeMap.put(otherEdge.getId(), otherEdge);

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
      Map<UniqueIdentifier, InputEdge> newEdgeMap = new LinkedMap<>(graph.edgeMap);
      newEdgeMap.put(newMergedEdge.getId(), newMergedEdge);

      return new Graph(
          graph.nodeMap,
          newEdgeMap,
          graph.nodeTypeCounts,
          graph.edgeTypeCounts
      );
    }
  }

  private void ensureContainsBothNodesOnEdge(InputEdge edge) {
    if (
        !this.nodeExists(edge.getNodeFromId())
            || !this.nodeExists(edge.getNodeToId())
    ) {
      throw new OneOrBothNodesOnEdgeDoesNotExist();
    }
  }

  public Graph merge(Graph otherGraph,
                                      DeduplicateOptions options) {
    if (options.equals(DeduplicateOptions.SAME_EDGE_TYPES_BETWEEN_SAME_NODES)) {
      return this.mergeWithEdgeDeduplication(otherGraph);
    }
    return this.merge(otherGraph);
  }

  private Graph mergeWithEdgeDeduplication(Graph otherGraph) {
    var newGraph = this;
    for (InputNode inputNode : otherGraph.getAllNodes()) {
      newGraph = newGraph.mergeNodeById(inputNode);
    }

    for (InputEdge otherGraphEdge : otherGraph.getAllEdges()) {

      List<InputEdge> localGraphEdges = newGraph.getAllEdges().stream().filter(processedEdge ->
          processedEdge.getNodeFromId().equals(otherGraphEdge.getNodeFromId()) &&
              processedEdge.getNodeToId().equals(otherGraphEdge.getNodeToId()) &&
              processedEdge.getType().equals(otherGraphEdge.getType())
      ).collect(Collectors.toList());

      if (localGraphEdges.size() > 1) {
        throw GraphEdgesCannotBeMerged.becauseThereIsMultipleEdgesGivenEdgeCouldBeMergeWith(
            localGraphEdges.stream().map(InputEdge::getId).toList()
        );
      }
      if (localGraphEdges.isEmpty()) {
        newGraph = newGraph.mergeEdge(otherGraphEdge);
      }
      if (localGraphEdges.size() == 1) {
        var newEdge =
            (InputEdge) localGraphEdges.get(0).mergeAttributesWithAttributesOf(otherGraphEdge);
        newGraph = newGraph.mergeEdge(newEdge);
      }
    }
    return newGraph;
  }
  public Graph mergeNodeById(
      InputNode otherNode
  ) {
    var nodeMap = new LinkedMap<>(this.nodeMap);
    var nodeTypeCounts = new LinkedMap<>(this.nodeTypeCounts);

    var foundNode = nodeMap.get(otherNode.getId());
    InputNode newNode;
    if (foundNode == null) {
      newNode = otherNode;
      nodeTypeCounts.put(
          newNode.getType(),
          nodeTypeCounts.getOrDefault(newNode.getType(), 0L) + 1
      );
    } else {
      newNode = foundNode.mergeOverwrite(otherNode);
    }
    nodeMap.put(newNode.getId(), newNode);

    return new Graph(
        nodeMap,
        this.edgeMap,
        nodeTypeCounts,
        this.edgeTypeCounts
    );
  }
}
