package ai.stapi.graphoperations.serializableGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import ai.stapi.graph.Graph;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class SerializableGraph {

  private final Map<String, SerializableNode> nodes;
  private final Map<String, SerializableEdge> edges;

  public static SerializableGraph fromInMemory(Graph graph) {
    var nodes = new HashMap<String, SerializableNode>();
    graph.getAllNodes()
        .stream()
        .map(SerializableNode::fromInputNode)
        .forEach(node -> nodes.put(node.getGlobalId(), node));

    var edges = new HashMap<String, SerializableEdge>();
    graph.getAllEdges()
        .stream()
        .map(SerializableEdge::fromInputEdge)
        .forEach(edge -> edges.put(edge.getGlobalId(), edge));

    return new SerializableGraph(nodes, edges);
  }

  @JsonCreator
  public SerializableGraph(
      @JsonProperty("nodes") Map<String, SerializableNode> nodes,
      @JsonProperty("edges") Map<String, SerializableEdge> edges
  ) {
    this.nodes = nodes;
    this.edges = edges;
  }

  public Map<String, SerializableNode> getNodes() {
    return nodes;
  }

  public Map<String, SerializableEdge> getEdges() {
    return edges;
  }
}
