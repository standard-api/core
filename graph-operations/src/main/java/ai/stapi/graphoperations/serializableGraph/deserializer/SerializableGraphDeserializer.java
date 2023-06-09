package ai.stapi.graphoperations.serializableGraph.deserializer;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.serializableGraph.SerializableEdge;
import ai.stapi.graphoperations.serializableGraph.SerializableGraph;
import ai.stapi.graphoperations.serializableGraph.SerializableNode;
import java.util.HashMap;

public class SerializableGraphDeserializer {

  private final SerializableNodeDeserializer nodeDeserializer;
  private final SerializableEdgeDeserializer edgeDeserializer;

  public SerializableGraphDeserializer(
      SerializableNodeDeserializer nodeDeserializer,
      SerializableEdgeDeserializer edgeDeserializer
  ) {
    this.nodeDeserializer = nodeDeserializer;
    this.edgeDeserializer = edgeDeserializer;
  }

  public Graph deserialize(SerializableGraph serializableGraph) {
    var nodes = new HashMap<Graph.GloballyUniqueIdentifier, Node>();
    serializableGraph.getNodes().values()
        .stream()
        .map(this.nodeDeserializer::deserialize)
        .forEach(node -> nodes.put(new Graph.GloballyUniqueIdentifier(node.getId(), node.getType()), node));

    var edges = new HashMap<Graph.GloballyUniqueIdentifier, Edge>();
    serializableGraph.getEdges().values()
        .stream()
        .map(this.edgeDeserializer::deserialize)
        .forEach(edge -> edges.put(new Graph.GloballyUniqueIdentifier(edge.getId(), edge.getType()), edge));

    return Graph.unsafe(nodes, edges);
  }

  public Edge deserializeEdge(SerializableEdge serializableEdge) {
    return this.edgeDeserializer.deserialize(serializableEdge);
  }

  public Node deserializeNode(SerializableNode serializableNode) {
    return this.nodeDeserializer.deserialize(serializableNode);
  }
}
