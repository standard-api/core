package ai.stapi.graphoperations.serializableGraph.deserializer;

import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graphoperations.serializableGraph.SerializableEdge;
import ai.stapi.identity.UniqueIdentifier;
import org.springframework.stereotype.Service;

@Service
public class SerializableEdgeDeserializer {

  private final SerializableAttributeDeserializer attributeDeserializer;

  public SerializableEdgeDeserializer(SerializableAttributeDeserializer attributeDeserializer) {
    this.attributeDeserializer = attributeDeserializer;
  }

  public Edge deserialize(SerializableEdge serializableEdge) {
    return new Edge(
        new UniqueIdentifier(serializableEdge.getId()),
        serializableEdge.getType(),
        this.attributeDeserializer.deserializeGroup(
            serializableEdge.getType(),
            serializableEdge.getAttributes()
        ),
        serializableEdge.getNodeFrom(),
        serializableEdge.getNodeTo()
    );
  }
}
