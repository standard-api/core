package ai.stapi.graph.traversableGraphElements;

import ai.stapi.graph.Graph;
import ai.stapi.graph.RepositoryEdgeLoader;
import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.AttributeContainerTest;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TraversableNodeTest extends AttributeContainerTest {

  @Override
  protected AbstractAttributeContainer getAttributeContainer() {
    return new TraversableNode(UniversallyUniqueIdentifier.randomUUID(), "irrelevant");
  }

  @Test
  public void itCanCreateNodeWithIdAndTypeAndVersionedAttributeGroupAndEdgeLoader() {
    //Given
    var expectedId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNodeType = "nodeType";
    var expectedVersionedAttributes = new ImmutableVersionedAttributeGroup(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15)),
        new LeafAttribute<>("test_string", new StringAttributeValue("version1")),
        new LeafAttribute<>("test_string", new StringAttributeValue("version2"))
    );
    var graph = new Graph();
    var expectedEdgeLoader =
        new RepositoryEdgeLoader(new InMemoryGraphRepository(graph));
    //When
    var node = new TraversableNode(
        expectedId,
        expectedNodeType,
        expectedVersionedAttributes,
        expectedEdgeLoader
    );
    //Then
    Assertions.assertEquals(expectedId, node.getId());
    Assertions.assertEquals(expectedNodeType, node.getType());
    Assertions.assertEquals(expectedVersionedAttributes, node.getVersionedAttributes());
  }

  @Test
  public void itCanCreateNodeWithCustomId() {
    //Given
    var expectedId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNodeType = "node_type";
    //When
    var node = new TraversableNode(
        expectedId,
        expectedNodeType
    );
    //Then
    Assertions.assertEquals(
        expectedId,
        node.getId()
    );
    Assertions.assertEquals(
        expectedNodeType,
        node.getType()
    );
  }

  @Test
  public void itCanGetEdges() throws GraphException {
    //Given

    var nodeFrom1 = new InputNode("nodeFrom1");
    var nodeFrom2 = new InputNode("nodeFrom2");
    var nodeTo = new InputNode("nodeTo");

    var firstTypeEdge = new InputEdge(
        nodeFrom1,
        "firstType",
        nodeTo
    );
    var otherTypeEdge = new InputEdge(
        nodeFrom2,
        "otherType",
        nodeTo
    );
    var inMemoryGraphStrucutre = new Graph(
        nodeTo,
        nodeFrom1,
        nodeFrom2,
        firstTypeEdge,
        otherTypeEdge
    );

    var loadedNode = inMemoryGraphStrucutre.traversable().loadNode(nodeTo.getId());
    //When
    var actualEdges = loadedNode.getEdges();
    //Then
    Assertions.assertEquals(
        2,
        actualEdges.size()
    );

    var actualEdge1 = actualEdges.stream().filter(
        edge -> edge.getType().equals("firstType")
    ).findFirst().get();

    var actualEdge2 = actualEdges.stream().filter(
        edge -> edge.getType().equals("otherType")
    ).findFirst().get();

    Assertions.assertEquals(
        "nodeFrom1",
        actualEdge1.getNodeFromType()
    );

    Assertions.assertEquals(
        "nodeFrom2",
        actualEdge2.getNodeFromType()
    );
  }

  @Test
  public void itCanGetEdgesOfCertainType() throws GraphException {
    //Given
    var expectedEdgeType = "certainEdgeType";
    var expectedNodeFromType1 = "nodeFrom1";
    var expectedNodeFromType2 = "nodeFrom2";
    var expectedNodeToType = "nodeTo";

    var nodeFrom1 = new InputNode(expectedNodeFromType1);
    var nodeFrom2 = new InputNode(expectedNodeFromType2);
    var nodeTo = new InputNode(expectedNodeToType);

    var edge1 = new InputEdge(
        nodeFrom1,
        expectedEdgeType,
        nodeTo
    );
    var edge2 = new InputEdge(
        nodeFrom2,
        expectedEdgeType,
        nodeTo
    );
    var otherTypeEdge = new InputEdge(
        nodeFrom2,
        "otherType",
        nodeTo
    );
    var inMemoryGraphStrucutre = new Graph(
        nodeTo,
        nodeFrom1,
        nodeFrom2,
        edge1,
        edge2,
        otherTypeEdge
    );

    var loadedNode = inMemoryGraphStrucutre.traversable().loadNode(nodeTo.getId());
    //When
    var actualEdges = loadedNode.getEdges(expectedEdgeType);
    //Then
    Assertions.assertEquals(
        2,
        actualEdges.size()
    );
    actualEdges.forEach(edge -> {
          Assertions.assertEquals(expectedEdgeType, edge.getType());
          Assertions.assertEquals(expectedNodeToType, edge.getNodeToType());
        }
    );
  }
}
