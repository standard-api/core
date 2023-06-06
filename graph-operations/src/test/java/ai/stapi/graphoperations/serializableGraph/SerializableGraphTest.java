package ai.stapi.graphoperations.serializableGraph;

import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.attribute.CreatedAtMetaData;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SerializableGraphTest extends SchemaIntegrationTestCase {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void itCanBeCreatedFromInMemoryGraph() {
    var nodeForm = new Node(
        new UniqueIdentifier("NodeFromId"),
        "node_from_type"
    );
    var nodeTo = new Node(
        new UniqueIdentifier("NodeToId"),
        "node_to_type"
    );
    var edge = new Edge(
        new UniqueIdentifier("EdgeId"),
        nodeForm,
        "edge_type",
        nodeTo
    );
    var attribute = new LeafAttribute<>(
        "attribute_name",
        Timestamp.valueOf("2023-05-04 21:00:55.68332646"),
        new StringAttributeValue("attribute value")
    );

    var originalGraph = new Graph(
        nodeForm.add(attribute),
        nodeTo.add(attribute),
        edge.add(attribute)
    );

    var serializable = SerializableGraph.fromInMemory(originalGraph);
    this.thenObjectApproved(serializable, List.of());
  }

  @Test
  void itCanBeAutomaticallySerializedFromInMemoryGraphWithObjectMapper() throws JsonProcessingException {
    var nodeForm = new Node(
        UniversallyUniqueIdentifier.randomUUID(),
        "node_from_type"
    );
    var nodeTo = new Node(
        UniversallyUniqueIdentifier.randomUUID(),
        "node_to_type"
    );
    var edge = new Edge(
        UniversallyUniqueIdentifier.randomUUID(),
        nodeForm,
        "edge_type",
        nodeTo
    );
    var attribute = new LeafAttribute<>(
        "attribute_name",
        Timestamp.valueOf("2023-05-04 21:00:55.68332646"),
        new StringAttributeValue("attribute value")
    );

    var originalGraph = new Graph(
        nodeForm.add(attribute),
        nodeTo.add(attribute),
        edge.add(attribute)
    );

    var serialized = this.objectMapper.writeValueAsString(originalGraph);

    this.thenObjectApproved(this.objectMapper.readValue(serialized, Object.class));
  }

  @Test
  void itCanBeDeserializedToInMemoryGraph() throws JsonProcessingException {
    var attribute_name = "attribute_name";
    var attribute = List.of(
        new SerializableAttributeVersion(
            List.of(
                new SerializableAttributeValue(
                    StringAttributeValue.SERIALIZATION_TYPE,
                    "attribute value"
                )
            ),
            Map.of(
                CreatedAtMetaData.NAME, List.of(
                    new SerializableAttributeValue(
                        InstantAttributeValue.SERIALIZATION_TYPE,
                        "2023-05-04 21:00:55.68332646"
                    )
                )
            )
        )
    );
    var nodeForm = new SerializableNode(
        "exampleNodeFromId",
        "node_from_type",
        Map.of(
            attribute_name, attribute
        )
    );
    var nodeTo = new SerializableNode(
        "exampleNodeToId",
        "node_to_type",
        Map.of(
            attribute_name, attribute
        )
    );
    var edge = new SerializableEdge(
        "exampleEdgeId",
        "edge_type",
        Map.of(
            attribute_name, attribute
        ),
        new NodeIdAndType(new UniqueIdentifier(nodeForm.getId()), nodeForm.getType()),
        new NodeIdAndType(new UniqueIdentifier(nodeTo.getId()), nodeTo.getType())
    );

    var serializableGraph = new SerializableGraph(
        Map.of(
            "node_from_type/exampleNodeFromId", nodeForm,
            "node_to_type/exampleToFromId", nodeTo
        ),
        Map.of(
            "edge_type/exampleEdgeId", edge
        )
    );

    var serialized = this.objectMapper.writeValueAsString(serializableGraph);
    var deserialized = this.objectMapper.readValue(
        serialized,
        Graph.class
    );
    this.thenGraphApproved(deserialized);
  }
}