package ai.stapi.graphoperations.serializableGraph.deserializer;

import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.serializableGraph.SerializableNode;
import ai.stapi.identity.UniqueIdentifier;
import org.springframework.stereotype.Service;

@Service
public class SerializableNodeDeserializer {

  private final SerializableAttributeDeserializer attributeDeserializer;

  public SerializableNodeDeserializer(SerializableAttributeDeserializer attributeDeserializer) {
    this.attributeDeserializer = attributeDeserializer;
  }

  public Node deserialize(SerializableNode serializableNode) {
    return new Node(
        new UniqueIdentifier(serializableNode.getId()),
        serializableNode.getType(),
        this.attributeDeserializer.deserializeGroup(
            serializableNode.getType(),
            serializableNode.getAttributes()
        )
    );
  }
}
