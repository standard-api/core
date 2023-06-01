package ai.stapi.graph.graphelements;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.AttributeContainerTest;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphNodesCannotBeMerged;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class NodeTest extends AttributeContainerTest {

  @Override
  protected AbstractAttributeContainer getAttributeContainer() {
    return new Node("test_node_type");
  }

  @Test
  void itCanCreateNode() {
    //Given
    var expectedNodeType = "nodeType";
    //When
    var node = new Node(expectedNodeType);
    //Then
    Assertions.assertEquals(expectedNodeType, node.getType());
  }

  @Test
  void itCanCreateNodeWithCustomId() {
    //Given
    var expectedId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNodeType = "node_type";
    //When
    var node = new Node(expectedId, expectedNodeType);
    //Then
    Assertions.assertEquals(expectedId, node.getId());
    Assertions.assertEquals(expectedNodeType, node.getType());
  }

  @Test
  void itCanCreateNodeWithAttributes() {
    //When
    var node = new Node(
        "node_type",
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    //Then
    Assertions.assertEquals(true, node.getAttribute("test_boolean").getValue());
    Assertions.assertEquals(10d, node.getAttribute("test_double").getValue());
    Assertions.assertEquals(15, node.getAttribute("test_integer").getValue());
  }

  @Test
  void itCannotMergeWithOtherNodeOfDifferentType() {
    //Given
    var node1 = new Node("first_node_type");
    var node2 = new Node("different_type");
    //When
    Executable executable = () -> node1.mergeOverwrite(node2);
    //Then
    Assertions.assertThrows(GraphNodesCannotBeMerged.class, executable);
  }

  @Test
  void itCannotMergeWithOtherNodeOfDifferentIds() {
    //Given
    var node1 = new Node(UniversallyUniqueIdentifier.randomUUID(), "first_node_type");
    var node2 = new Node(node1.getId(), "irrelevant_type");
    //When
    Executable executable = () -> node1.mergeOverwrite(node2);
    //Then
    Assertions.assertThrows(GraphNodesCannotBeMerged.class, executable);
  }

  @Test
  void itCanMergeWithOtherNode() {
    //Given
    var firstNode = new Node(
        "merged_node_type",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var otherNode = new Node(
        firstNode.getId(),
        "merged_node_type",
        new ImmutableVersionedAttributeGroup(
            new LeafAttribute<>("original", new StringAttributeValue("original value")),
            new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
            new LeafAttribute<>("new", new StringAttributeValue("new value"))
        )
    );

    var expectedNode = new Node(
        firstNode.getId(),
        "merged_node_type",
        new ImmutableVersionedAttributeGroup(
            new LeafAttribute<>("original", new StringAttributeValue("original value")),
            new LeafAttribute<>("updated", new StringAttributeValue("old value")),
            new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
            new LeafAttribute<>("new", new StringAttributeValue("new value"))
        )
    );
    //When
    var actualNode = firstNode.mergeOverwrite(otherNode);

    //Then
    Assertions.assertEquals(expectedNode, actualNode);
  }
}
