package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.EdgeTypeInfo;
import ai.stapi.graph.Graph;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.NodeInfo;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.NodeTypeInfo;
import ai.stapi.graph.RepositoryEdgeLoader;
import ai.stapi.graph.RepositoryNodeLoader;
import ai.stapi.graph.exceptions.GraphElementNotFound;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import org.jetbrains.annotations.TestOnly;

public class InMemoryGraphRepository implements NodeRepository, EdgeRepository {

  private Graph graph;
  private ConcurrentHashMap<UniqueIdentifier, ConcurrentSkipListSet<TraversableEdge>> nodesEdgesMap;

  public InMemoryGraphRepository() {
    this(new Graph());
  }

  public InMemoryGraphRepository(Graph graph) {
    this.graph = graph;
    this.recalculateNodesEdgesMap();
  }

  public InMemoryGraphRepository(AttributeContainer... graphElements) {
    this(new Graph(graphElements));
  }

  @Override
  public void save(Edge edge) {
    this.graph = this.graph.with(edge);
    NodeEdgesMapOperation.upsertEdge(this.nodesEdgesMap, this.toTraversableEdge(edge));
  }

  @Override
  public void removeEdge(UniqueIdentifier edgeId, String edgeType) {
    var edgeToRemove = this.graph.getEdge(edgeId, edgeType);
    this.graph = this.graph.removeEdge(
        new EdgeForRemoval(edgeId, edgeType)
    );
    NodeEdgesMapOperation.removeEdge(this.nodesEdgesMap, edgeToRemove);

  }

  @Override
  public void replace(Edge edge) {
    var edgeToRemove = this.graph.getEdge(edge.getId(), edge.getType());

    var oldGraph = this.graph;
    this.graph = oldGraph.replace(edge);
    var newEdge = this.loadEdge(edge.getId(), edge.getType());
    NodeEdgesMapOperation.removeEdge(this.nodesEdgesMap, edgeToRemove);
    NodeEdgesMapOperation.upsertEdge(this.nodesEdgesMap, newEdge);
  }

  @Override
  public void removeEdge(EdgeForRemoval edgeForRemoval) {
    this.graph = this.graph.removeEdge(edgeForRemoval);
  }

  @Override
  public void replace(Node node) {
    this.graph = this.graph.replace(node);
  }

  @Override
  public void removeNode(UniqueIdentifier id, String nodeType) {
    this.graph = this.graph.removeNode(id, nodeType);
  }

  @Override
  public void removeNode(NodeForRemoval nodeForRemoval) {
    this.graph = this.graph.removeNode(nodeForRemoval);
  }

  public void removeAllElements() {
    this.graph = new Graph();
  }

  private TraversableNode toTraversableNode(Node node) {
    return new TraversableNode(
        node.getId(),
        node.getType(),
        node.getVersionedAttributes(),
        new RepositoryEdgeLoader(this)
    );
  }

  private TraversableEdge toTraversableEdge(Edge edge) {
    if (edge == null) {
      return null;
    }
    return new TraversableEdge(
        edge.getId(),
        this.loadNode(edge.getNodeFromId()),
        edge.getType(),
        this.loadNode(edge.getNodeToId()),
        edge.getVersionedAttributes(),
        new RepositoryNodeLoader(this)
    );
  }

  @Override
  public TraversableEdge loadEdge(UniqueIdentifier id, String type) {
    return this.toTraversableEdge(
        this.graph.getEdge(id, type)
    );
  }

  public TraversableEdge loadEdge(UniqueIdentifier id) {
    return this.toTraversableEdge(
        this.graph.getEdge(id)
    );
  }

  @Override
  public boolean edgeExists(UniqueIdentifier id, String type) {
    return this.graph.edgeExists(id, type);
  }

  protected boolean edgeExists(UniqueIdentifier id) {
    return this.graph.edgeExists(id);
  }

  @Override
  public List<EdgeTypeInfo> getEdgeTypeInfos() {
    return this.graph.getEdgeTypeInfos();
  }

  public Set<TraversableEdge> findInAndOutEdgesForNode(
      UniqueIdentifier nodeId,
      String nodeType
  ) {

    return NodeEdgesMapOperation.getNodeEdges(this.nodesEdgesMap, nodeId, nodeType);
  }

  @Override
  public TraversableEdge findEdgeByTypeAndNodes(
      String edgeType, 
      NodeIdAndType nodeFrom,
      NodeIdAndType nodeTo
  ) {
    return this.toTraversableEdge(
        this.graph.findEdgeByTypeAndNodes(
            edgeType,
            nodeFrom,
            nodeTo
        )
    );
  }

  @Override
  public void save(Node node) {
    this.graph = this.graph.with(node);
  }

  @Override
  public TraversableNode loadNode(UniqueIdentifier uniqueIdentifier, String nodeType) {
    return this.toTraversableNode(
        this.graph.getNode(uniqueIdentifier, nodeType)
    );
  }

  @Override
  public TraversableNode loadNode(UniqueIdentifier uniqueIdentifier) {
    return this.toTraversableNode(
        this.graph.getNode(uniqueIdentifier)
    );
  }

  @Override
  public boolean nodeExists(UniqueIdentifier id, String nodeType) {
    return this.graph.nodeExists(id, nodeType);
  }

  public boolean nodeExists(UniqueIdentifier id) {
    return this.graph.nodeExists(id);
  }

  @Override
  public List<NodeTypeInfo> getNodeTypeInfos() {
    return this.graph.getNodeTypeInfos();
  }

  @Override
  public List<NodeInfo> getNodeInfosBy(String nodeType) {
    return this.graph.getNodeInfosBy(nodeType);
  }

  public List<TraversableNode> loadAllNodes(String nodeType) {
    return this.graph.getAllNodes().stream().filter(
        node -> node.getType().equals(nodeType)
    ).map(this::toTraversableNode).toList();
  }

  public List<TraversableNode> loadAllNodes() {
    return this.graph.getAllNodes().stream()
        .map(this::toTraversableNode).toList();
  }

  public TraversableGraphElement loadGraphElement(UniqueIdentifier uuid) {
    if (this.nodeExists(uuid)) {
      return this.loadNode(uuid);
    }
    if (this.edgeExists(uuid)) {
      return this.loadEdge(uuid);
    }

    throw new GraphElementNotFound(uuid);
  }

  public List<TraversableEdge> loadAllEdges() {
    List<Edge> allEdges = this.graph.getAllEdges();
    return allEdges.stream()
        .map(this::toTraversableEdge).toList();
  }

  public List<TraversableEdge> loadAllEdges(String edgeType) {
    return this.graph.getAllEdges().stream()
        .filter(edge -> edge.getType().equals(edgeType))
        .map(this::toTraversableEdge).toList();
  }

  public void merge(Graph otherGraph) {
    this.graph = this.graph.merge(otherGraph);
    this.recalculateNodesEdgesMap();

  }

  public void merge(InMemoryGraphRepository otherGraph) {
    this.merge(otherGraph.getGraph());
  }


  public void removeGraphElements(GraphElementForRemoval... graphElementsForRemoval) {
    this.graph =
        this.graph.removeGraphElements(graphElementsForRemoval);
    this.recalculateNodesEdgesMap();
  }

  public Graph getGraph() {
    return graph;
  }


  public void merge(Graph otherGraph, DeduplicateOptions options) {
    this.graph = this.graph.merge(otherGraph, options);
    this.recalculateNodesEdgesMap();
  }
  
  @TestOnly
  public void prune() {
    this.graph = new Graph();
  }

  private void recalculateNodesEdgesMap() {
    this.nodesEdgesMap = new ConcurrentHashMap<>();
    for (var edge : this.loadAllEdges()) {
      NodeEdgesMapOperation.upsertEdge(this.nodesEdgesMap, edge);
    }
  }
}
