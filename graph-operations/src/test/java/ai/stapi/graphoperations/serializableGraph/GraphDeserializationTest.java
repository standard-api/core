package ai.stapi.graphoperations.serializableGraph;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GraphDeserializationTest extends SchemaIntegrationTestCase {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void itCanSerializeAndDeserializeGraphWithJackson() throws IOException {
    var nodeFrom = new Node("node_type");
    var nodeTo = new Node("node_type_to");
    var edge = new Edge(
        nodeFrom,
        "edge_type",
        nodeTo
    );

    var attribute = new ListAttribute(
        "attribute",
        new StringAttributeValue("attribute value")
    );

    var givenGraph = new Graph(
        nodeFrom.add(attribute),
        nodeTo,
        edge.add(attribute)
    );

    var serialized = this.objectMapper.writer().writeValueAsString(givenGraph);
    var actualDeserializedGraph = this.objectMapper.readValue(
        serialized,
        Graph.class
    );
    this.thenIdLessGraphsAreEqual(
        givenGraph,
        actualDeserializedGraph
    );
    this.thenGraphApproved(actualDeserializedGraph);
  }
}
