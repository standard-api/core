package ai.stapi.graph.traversableGraphElements;

import ai.stapi.graph.Graph;
import ai.stapi.graph.NullNodeLoader;
import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.AttributeContainerTest;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TraversableEdgeTest extends AttributeContainerTest {

  @Override
  protected AbstractAttributeContainer getAttributeContainer() {
    return new TraversableEdge(
        new Node("test_node_from"),
        "edge_type",
        new Node("test_node_to")
    );
  }

  @Test
  void itCanCreateEdge_WithNodes() {
    //Given
    var expectedEdgeType = "edge_type";
    //When
    var nodeFrom = new Node("node_from");
    var nodeTo = new Node("node_to");
    var edge = new TraversableEdge(
        nodeFrom,
        expectedEdgeType,
        nodeTo
    );
    //Then
    Assertions.assertEquals(expectedEdgeType, edge.getType());
    Assertions.assertEquals(nodeFrom.getId(), edge.getNodeFromId());
    Assertions.assertEquals(nodeTo.getId(), edge.getNodeToId());
    Assertions.assertEquals(nodeFrom.getType(), edge.getNodeFromType());
    Assertions.assertEquals(nodeTo.getType(), edge.getNodeToType());
  }

  @Test
  void itCanCreateEdge_WithVersionedAttributes() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFrom = new TraversableNode("irrelevant");
    var expectedNodeTo = new TraversableNode("irrelevant");
    var expectedVersionedAttributeGroup = new ImmutableVersionedAttributeGroup(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    //When
    var node = new TraversableEdge(
        UniversallyUniqueIdentifier.randomUUID(),
        expectedNodeFrom,
        expectedEdgeType,
        expectedNodeTo,
        expectedVersionedAttributeGroup,
        new NullNodeLoader()
    );
    //Then
    Assertions.assertEquals(
        expectedEdgeType,
        node.getType()
    );
    Assertions.assertEquals(
        expectedNodeFrom.getId(),
        node.getNodeFromId()
    );
    Assertions.assertEquals(
        expectedNodeFrom.getType(),
        node.getNodeFromType()
    );
    Assertions.assertEquals(
        expectedNodeTo.getId(),
        node.getNodeToId()
    );
    Assertions.assertEquals(
        expectedNodeTo.getType(),
        node.getNodeToType()
    );
    Assertions.assertEquals(
        expectedVersionedAttributeGroup,
        node.getVersionedAttributes()
    );
  }

  @Test
  void itHasDeterministicHashCode() {

    var versionedAttributeGroup = new ImmutableVersionedAttributeGroup(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );

    var node1 = new Node("irrelevant");
    var node2 = new Node("irrelevant");
    var savedEdge = new Edge(
        UniversallyUniqueIdentifier.fromString("0bac1abc-660e-4e92-94e2-3e8953873c58"),
        "edge_type",
        node1,
        node2,
        versionedAttributeGroup
    );

    var graph = new Graph(
        node1,
        node2,
        savedEdge
    );

    var loadedEdge = graph.getEdge(
        savedEdge.getId(),
        savedEdge.getType()
    );

    var firstActualHashCode = loadedEdge.getIdlessHashCode();
    var secondActualHashCode = loadedEdge.getIdlessHashCode();

    Assertions.assertEquals(
        firstActualHashCode,
        secondActualHashCode
    );
    Assertions.assertEquals(
        loadedEdge.hashCode(),
        loadedEdge.hashCode()
    );
    Assertions.assertEquals(
        1718053182,
        firstActualHashCode
    );
  }

  @Test
  void itCanGetNodeFromFromBuilder() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFrom = new Node(
        "from_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeFrom attribute value"))
    );
    var expectedNodeTo = new Node(
        "to_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeTo attribute value"))
    );
    //When
    var edge = new Edge(
        expectedNodeFrom,
        expectedEdgeType,
        expectedNodeTo
    );

    var graph = new Graph(
        expectedNodeFrom,
        expectedNodeTo,
        edge
    );

    var traversableEdge = graph.traversable().loadEdge(
        edge.getId(),
        edge.getType()
    );

    var nodeFrom = traversableEdge.getNodeFrom();
    this.thenNodeApproved(nodeFrom);
  }

  @Test
  void itCanGetNodeToFromBuilder() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFrom = new Node(
        "from_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeFrom attribute value"
        ))
    );
    var expectedNodeTo = new Node(
        "to_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeTo attribute value"
        ))
    );
    //When
    var edge = new Edge(
        expectedNodeFrom,
        expectedEdgeType,
        expectedNodeTo
    );

    var graph = new Graph(
        expectedNodeFrom,
        expectedNodeTo,
        edge
    );

    var traversableEdge = graph.traversable().loadEdge(
        edge.getId(),
        edge.getType()
    );

    this.thenNodeApproved(traversableEdge.getNodeTo());
  }
}
