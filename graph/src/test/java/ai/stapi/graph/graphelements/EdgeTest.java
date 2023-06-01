package ai.stapi.graph.graphelements;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.AttributeContainerTest;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.inMemoryGraph.EdgeBuilder;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class EdgeTest extends AttributeContainerTest {

  @Override
  protected AbstractAttributeContainer getAttributeContainer() {
    return new Edge(
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
    var edge = new Edge(
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
  void itCanCreateEdgeWithDefaultAttributeMap() {
    //Given
    var expectedEdgeType = "edge_type";
    var expectedNodeFromId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNodeToId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNodeFromType = "node_from_type";
    var expectedNodeToType = "node_to_type";
    var expectedVersionAttributes = new ImmutableVersionedAttributeGroup(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    //When
    var edge = EdgeBuilder.withAny()
        .setEdgeType(expectedEdgeType)
        .setNodeFromId(expectedNodeFromId)
        .setNodeFromType(expectedNodeFromType)
        .setNodeToId(expectedNodeToId)
        .setNodeToType(expectedNodeToType)
        .setVersionedAttributes(expectedVersionAttributes)
        .create();

    //Then
    Assertions.assertEquals(expectedEdgeType, edge.getType());
    Assertions.assertEquals(expectedNodeFromId, edge.getNodeFromId());
    Assertions.assertEquals(expectedNodeFromType, edge.getNodeFromType());
    Assertions.assertEquals(expectedNodeToId, edge.getNodeToId());
    Assertions.assertEquals(expectedNodeToType, edge.getNodeToType());
    Assertions.assertEquals(expectedVersionAttributes, edge.getVersionedAttributes());
  }


  @Test
  void itCannotMergeWithOtherEdgeOfDifferentType() {
    //Given
    var edge1 = EdgeBuilder.withAny()
        .setEdgeType("edge_type")
        .create();
    var edge2 = EdgeBuilder.withAny()
        .setEdgeId(edge1.getId())
        .setEdgeType("wrong_edge_type")
        .setNodeFromId(edge1.getNodeFromId())
        .setNodeToId(edge1.getNodeToId())
        .create();
    //When
    Executable executable = () -> edge1.mergeOverwrite(edge2);
    //Then
    Assertions.assertThrows(GraphEdgesCannotBeMerged.class, executable);
  }

  @Test
  void itCannotMergeWithOtherEdgeOfDifferentIds() {
    //Given
    var edge1 = EdgeBuilder.withAny()
        .setEdgeId(UniversallyUniqueIdentifier.randomUUID())
        .create();
    var edge2 = EdgeBuilder.withAny()
        .setEdgeId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeFromId(edge1.getNodeFromId())
        .setNodeToId(edge1.getNodeToId())
        .create();
    //When
    Executable executable = () -> edge1.mergeOverwrite(edge2);
    //Then
    Assertions.assertThrows(GraphEdgesCannotBeMerged.class, executable);
  }

  @Test
  void itCannotMergeWithOtherEdgeOfDifferentNodeIds() {
    //Given
    var edge1 = EdgeBuilder.withAny()
        .setNodeFromId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeToId(UniversallyUniqueIdentifier.randomUUID())
        .create();
    var edge2 = EdgeBuilder.withAny()
        .setEdgeId(edge1.getId())
        .setNodeFromId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeToId(UniversallyUniqueIdentifier.randomUUID())
        .create();
    //When
    Executable executable = () -> edge1.mergeOverwrite(edge2);
    //Then
    Assertions.assertThrows(GraphEdgesCannotBeMerged.class, executable);
  }

  @Test
  void itCanMergeWithOtherEdge() {
    //Given
    var mergedEdge = EdgeBuilder.withAny()
        .setVersionedAttributes(
            new ImmutableVersionedAttributeGroup(
                new LeafAttribute<>("original", new StringAttributeValue("original value")),
                new LeafAttribute<>("updated", new StringAttributeValue("old value"))
            )
        ).create();

    var otherEdge = EdgeBuilder.withAny()
        .setEdgeId(mergedEdge.getId())
        .setNodeFromId(mergedEdge.getNodeFromId())
        .setNodeToId(mergedEdge.getNodeToId())
        .setVersionedAttributes(
            new ImmutableVersionedAttributeGroup(
                new LeafAttribute<>("original", new StringAttributeValue("original value")),
                new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
                new LeafAttribute<>("new", new StringAttributeValue("new value"))
            )
        ).create();

    //When
    mergedEdge = mergedEdge.mergeOverwrite(otherEdge);
    //Then
    var expectedEdge = EdgeBuilder.withAny()
        .setEdgeId(mergedEdge.getId())
        .setNodeFromId(mergedEdge.getNodeFromId())
        .setNodeToId(mergedEdge.getNodeToId())
        .setVersionedAttributes(
            new ImmutableVersionedAttributeGroup(
                new LeafAttribute<>("original", new StringAttributeValue("original value")),
                new LeafAttribute<>("updated", new StringAttributeValue("old value")),
                new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
                new LeafAttribute<>("new", new StringAttributeValue("new value"))
            )
        ).create();
    Assertions.assertEquals(mergedEdge, expectedEdge);
  }
}
