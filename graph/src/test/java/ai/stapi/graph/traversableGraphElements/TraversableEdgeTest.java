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
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TraversableEdgeTest extends AttributeContainerTest {

  @Override
  protected AbstractAttributeContainer getAttributeContainer() {
    return new TraversableEdge(
        new InputNode("test_node_from"),
        "edge_type",
        new InputNode("test_node_to")
    );
  }

  @Test
  public void itCanCreateEdge_WithInputNodes() {
    //Given
    var expectedEdgeType = "edge_type";
    //When
    var inputNodeFrom = new InputNode("input_node_from");
    var inputNodeTo = new InputNode("input_node_to");
    var edge = new TraversableEdge(
        inputNodeFrom,
        expectedEdgeType,
        inputNodeTo
    );
    //Then
    Assertions.assertEquals(expectedEdgeType, edge.getType());
    Assertions.assertEquals(inputNodeFrom.getId(), edge.getNodeFromId());
    Assertions.assertEquals(inputNodeTo.getId(), edge.getNodeToId());
    Assertions.assertEquals(inputNodeFrom.getType(), edge.getNodeFromType());
    Assertions.assertEquals(inputNodeTo.getType(), edge.getNodeToType());
  }

  @Test
  public void itCanCreateEdge_WithVersionedAttributes() {
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
  public void itHasDeterministicHashCode() {

    var versionedAttributeGroup = new ImmutableVersionedAttributeGroup(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );

    var node1 = new InputNode("irrelevant");
    var node2 = new InputNode("irrelevant");
    var savedEdge = new InputEdge(
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
  public void itCanGetNodeFromFromBuilder() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFrom = new InputNode(
        "from_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeFrom attribute value"))
    );
    var expectedNodeTo = new InputNode(
        "to_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeTo attribute value"))
    );
    //When
    var edge = new InputEdge(
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

    TraversableNode nodeFrom = traversableEdge.getNodeFrom();
    this.thenNodeApproved(nodeFrom);
  }

  @Test
  public void itCanGetNodeToFromBuilder() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFrom = new InputNode(
        "from_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeFrom attribute value"
        ))
    );
    var expectedNodeTo = new InputNode(
        "to_node_type",
        new LeafAttribute<>("test_string", new StringAttributeValue("NodeTo attribute value"
        ))
    );
    //When
    var edge = new InputEdge(
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
