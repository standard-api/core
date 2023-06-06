package ai.stapi.graphoperations.serializableGraph.deserializer;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.serializableGraph.SerializableEdge;
import ai.stapi.graphoperations.serializableGraph.SerializableGraph;
import ai.stapi.graphoperations.serializableGraph.SerializableNode;
import ai.stapi.identity.UniqueIdentifier;
import java.util.HashMap;
import org.springframework.stereotype.Service;

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
    var nodes = new HashMap<UniqueIdentifier, Node>();
    serializableGraph.getNodes().values()
        .stream()
        .map(this.nodeDeserializer::deserialize)
        .forEach(node -> nodes.put(node.getId(), node));

    var edges = new HashMap<UniqueIdentifier, Edge>();
    serializableGraph.getEdges().values()
        .stream()
        .map(this.edgeDeserializer::deserialize)
        .forEach(edge -> edges.put(edge.getId(), edge));

    return new Graph(nodes, edges);
  }

  public Edge deserializeEdge(SerializableEdge serializableEdge) {
    return this.edgeDeserializer.deserialize(serializableEdge);
  }

  public Node deserializeNode(SerializableNode serializableNode) {
    return this.nodeDeserializer.deserialize(serializableNode);
  }
}
