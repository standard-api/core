package ai.stapi.graph;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.MoreThanOneNodeOfTypeFoundException;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeOfTypeNotFoundException;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.inMemoryGraph.DeduplicateOptions;
import ai.stapi.graph.inMemoryGraph.exceptions.CannotCreateGraphWithOtherThanGraphElements;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class GraphTest extends UnitTestCase {

  @Test
  void itCanCreateWithNodes() {
    var actualGraph = new Graph(
        new Node("A"),
        new Node("B")
    );
  }

  @Test
  void itCannotCreateWithTraversableNodes() {
    Executable executable = () -> new Graph(
        new TraversableNode("A")
    );

    Assertions.assertThrows(
        CannotCreateGraphWithOtherThanGraphElements.class,
        executable
    );
  }

  @Test
  void itCanMergeOverwriteWithOtherGraph_WithNodes() {
    var nodeInGraph1 = new Node(
        "merged_node_type",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var nodeInGraph2 = new Node(
        nodeInGraph1.getId(),
        "merged_node_type",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
        new LeafAttribute<>("new", new StringAttributeValue("new value"))
    );

    var graphG1 = new Graph(
        nodeInGraph1,
        new Node("already saved node")
    );

    var graphG2 = new Graph(
        nodeInGraph2,
        new Node("node from other graph")
    );

    graphG1 = graphG1.merge(graphG2);
    this.thenGraphApproved(graphG1);
  }

  @Test
  void itCanLoadNodesByNodeType() {
    var nodeA = new Node(
        "type_A",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var nodeB = new Node(
        "type_A",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
        new LeafAttribute<>("new", new StringAttributeValue("new value"))
    );

    var graph = new Graph(
        nodeA,
        nodeB
    );

    Assertions.assertEquals(2, graph.getAllNodes("type_A").size());

  }

  @Test
  void itThrowsExceptionWhenLoadingExactlyOneNodeOfTypeButMorePresent() {
    var nodeA = new Node("type_A");
    var nodeB = new Node("type_A");

    var graph = new Graph(
        nodeA,
        nodeB
    );
    //When
    Executable executable = () -> graph.getExactlyOneNodeOfType("type_A");
    //Then
    Assertions.assertThrows(MoreThanOneNodeOfTypeFoundException.class, executable);
  }

  @Test
  void itThrowsExceptionWhenLoadingExactlyOneNodeOfTypeButNonePresent() {
    var nodeA = new Node("type_A");
    var nodeB = new Node("type_A");

    var graph = new Graph(
        nodeA,
        nodeB
    );
    //When
    Executable executable = () -> graph.getExactlyOneNodeOfType("type_B");
    //Then
    Assertions.assertThrows(NodeOfTypeNotFoundException.class, executable);
  }

  @Test
  void itLoadsExactlyOneNodeOfType() {
    var nodeA = new Node("type_A");
    var nodeB = new Node("type_B");

    var graph = new Graph(
        nodeA,
        nodeB
    );
    //When
    var actualNode = graph.getExactlyOneNodeOfType("type_A");
    //Then
    Assertions.assertEquals("type_A", actualNode.getType());
    Assertions.assertEquals(nodeA.getId(), actualNode.getId());
  }

  @Test
  void itCanTellWhetherSomeNodeOfTypeExists() {
    var node = new Node("type_A");
    var graph = new Graph(node);
    Assertions.assertTrue(graph.containsNodeOfType("type_A"));
    Assertions.assertFalse(graph.containsNodeOfType("type_B"));
  }

  @Test
  void itCanCreateWithEdges() {
    var nodeA = new Node("nodeA");
    var nodeB = new Node("nodeB");
    var nodeC = new Node("nodeC");
    var nodeD = new Node("nodeD");
    var actualGraph = new Graph(
        nodeA,
        nodeB,
        nodeC,
        nodeD,
        new Edge(
            nodeA,
            "edgeA",
            nodeB
        ),
        new Edge(
            nodeC,
            "edgeB",
            nodeD
        )
    );
  }

  @Test
  void itCanLoadEdgesOfType() {
    var nodeA = new Node("nodeA");
    var nodeB = new Node("nodeB");
    var nodeC = new Node("nodeC");
    var nodeD = new Node("nodeD");
    var actualGraph = new Graph(
        nodeA,
        nodeB,
        nodeC,
        nodeD,
        new Edge(
            nodeA,
            "edgeA",
            nodeB
        ),
        new Edge(
            nodeC,
            "edgeB",
            nodeD
        ),
        new Edge(
            nodeC,
            "edgeA",
            nodeD
        )
    );
    var edgesOfType = actualGraph.loadAllEdges("edgeA");
    Assertions.assertEquals(2, edgesOfType.size());
  }

  @Test
  void itShouldMergeTwoGraphsByIds() {
    var nodeFromInGraphG1 = new Node("merged_node_type");
    var nodeToInGraphG1 = new Node("node_type_to");
    var edgeE1InGraphG1 = new Edge(
        nodeFromInGraphG1,
        "edge_type",
        nodeToInGraphG1
    );

    var nodeFromInGraphG2 = new Node(
        nodeFromInGraphG1.getId(),
        nodeFromInGraphG1.getType()
    );
    var nodeToInGraphG2 = new Node(
        nodeToInGraphG1.getId(),
        nodeToInGraphG1.getType()
    );
    var edgeE1InGraphG2 = new Edge(
        edgeE1InGraphG1.getId(),
        nodeFromInGraphG2,
        edgeE1InGraphG1.getType(),
        nodeToInGraphG2
    );

    var originalAttribute =
        new LeafAttribute<>("original", new StringAttributeValue("original value"));
    var originalBAttribute =
        new LeafAttribute<>("original", new StringAttributeValue("original value"));
    var updatedOldAttribute = new LeafAttribute<>("updated", new StringAttributeValue("old value"));
    var updatedNewAttribute =
        new LeafAttribute<>("updated", new StringAttributeValue("updated value"));
    var newAttribute =
        new LeafAttribute<>("new", new StringAttributeValue("new value"));

    var graphG1 = new Graph(
        nodeFromInGraphG1.add(updatedOldAttribute).add(originalAttribute),
        nodeToInGraphG1,
        edgeE1InGraphG1.add(updatedOldAttribute).add(originalAttribute)
    );
    var graphG2 = new Graph(
        nodeFromInGraphG2.add(updatedNewAttribute).add(newAttribute).add(originalBAttribute),
        nodeToInGraphG2,
        edgeE1InGraphG2.add(updatedNewAttribute).add(newAttribute).add(originalBAttribute)
    );
    var actualGraph = graphG1.merge(graphG2);

    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanMergeOverwriteWithOtherGraph_WithEdges() {
    var mergedNodeFrom = new Node("merged_node_from_type");
    var mergedNodeTo = new Node("merged_node_to_type");

    var mergedEdge = new Edge(
        UniversallyUniqueIdentifier.randomUUID(),
        mergedNodeFrom,
        "same_type",
        mergedNodeTo,
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var otherEdge = new Edge(
        mergedEdge.getId(),
        mergedNodeFrom,
        "same_type",
        mergedNodeTo,
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
        new LeafAttribute<>("new", new StringAttributeValue("new value"))
    );

    var alreadySavedNodeFrom = new Node("already_saved_from");
    var alreadySavedNodeTo = new Node("already_saved_to");

    var graphG1 = new Graph(
        mergedNodeFrom,
        mergedNodeTo,
        mergedEdge,
        alreadySavedNodeFrom,
        alreadySavedNodeTo,
        new Edge(alreadySavedNodeFrom, "already_saved_edge", alreadySavedNodeTo)
    );

    var newSavedNodeFrom = new Node("new_saved_from");
    var newSavedNodeTo = new Node("new_saved_to");
    var graphG2 = new Graph(
        mergedNodeFrom,
        mergedNodeTo,
        otherEdge,
        newSavedNodeFrom,
        newSavedNodeTo,
        new Edge(newSavedNodeFrom, "new_edge", newSavedNodeTo)
    );

    graphG1 = graphG1.merge(graphG2);
    this.thenGraphApproved(graphG1);
  }


  @Test
  void itShouldReplaceNodes() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var nodeA = new Node(nodeId, "SameType");
    var nodeB = new Node(nodeId, "SameType");

    nodeA = nodeA.add(
        new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value"))
    );
    nodeB = nodeB.add(
        new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value"))
    );

    var graph = new Graph(nodeA);

    var actualGraph = graph.replace(nodeB);
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itShouldAllowToReplaceNodeOfDifferentType() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var nodeA = new Node(
        nodeId,
        "type"
    );

    var nodeB = new Node(
        nodeId,
        "different_type"
    );

    nodeA = nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
    nodeB = nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));

    var graph = new Graph(nodeA);

    var finalNodeB = nodeB;
    var actualGraph = graph.replace(finalNodeB);
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itShouldRemoveEdge_AsGraphElementForRemoval() {
    var nodeFrom = new Node("node_from");
    var nodeTo = new Node("node_to");

    var alreadySavedEdge = new Edge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var givenGraph = new Graph(nodeFrom, nodeTo, alreadySavedEdge);
    var edgeForRemoval = new EdgeForRemoval(alreadySavedEdge.getId(), alreadySavedEdge.getType());

    //When
    var actualGraph = givenGraph.removeGraphElement(edgeForRemoval);

    //Then
    Edge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> actualGraph.getEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveNode_AsGraphElementForRemoval() {
    var alreadySavedNode = new Node("test_node");
    alreadySavedNode = alreadySavedNode.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(alreadySavedNode.getId(), alreadySavedNode.getType());

    var givenGraph = new Graph(alreadySavedNode);
    //When
    var actualGraph = givenGraph.removeNode(nodeForRemoval);
    //Then
    Node finalAlreadySavedNode = alreadySavedNode;
    Executable executable = () -> actualGraph.getNode(finalAlreadySavedNode.getId(), "test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldMergeEdgesOfSameTypeBetweenSameNodes() {
    var originalGraph = new Graph();
    var firstOriginalNode = new Node("original_first_node");
    var secondOriginalNode = new Node("original_second_node");
    var firstOriginalEdge = new Edge(firstOriginalNode, "original_edge", secondOriginalNode);
    firstOriginalEdge = firstOriginalEdge.add(
        new LeafAttribute<>("original_attribute", new StringAttributeValue("anyValue")));
    originalGraph = originalGraph.withAll(firstOriginalNode, secondOriginalNode, firstOriginalEdge);

    var graphToBeMerged = new Graph();
    var edgeToBeMerged = new Edge(firstOriginalNode, "original_edge", secondOriginalNode);
    edgeToBeMerged = edgeToBeMerged.add(
        new LeafAttribute<>("merged_attribute", new StringAttributeValue("anyValue")));
    graphToBeMerged =
        graphToBeMerged.withAll(firstOriginalNode, secondOriginalNode, edgeToBeMerged);
    var actualGraph = originalGraph.traversable();
    actualGraph.merge(graphToBeMerged, DeduplicateOptions.SAME_EDGE_TYPES_BETWEEN_SAME_NODES);
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itThrowsErrorWhenEdgeCouldBeMergerWithMultipleEdges() {
    var originalGraph = new Graph();
    var firstOriginalNode = new Node("original_first_node");
    var secondOriginalNode = new Node("original_second_node");
    var firstOriginalEdge = new Edge(firstOriginalNode, "original_edge", secondOriginalNode);
    var secondOriginalEdge = new Edge(firstOriginalNode, "original_edge", secondOriginalNode);
    firstOriginalEdge = firstOriginalEdge.add(
        new LeafAttribute<>("original_attribute", new StringAttributeValue("anyValue")));
    secondOriginalEdge = secondOriginalEdge.add(
        new LeafAttribute<>("second_original_attribute", new StringAttributeValue("anyValue")));
    originalGraph = originalGraph.withAll(firstOriginalNode, secondOriginalNode, firstOriginalEdge,
        secondOriginalEdge);

    var graphToBeMerged = new Graph();
    var edgeToBeMerged = new Edge(firstOriginalNode, "original_edge", secondOriginalNode);
    edgeToBeMerged = edgeToBeMerged.add(
        new LeafAttribute<>("merged_attribute", new StringAttributeValue("anyValue")));
    graphToBeMerged =
        graphToBeMerged.withAll(firstOriginalNode, secondOriginalNode, edgeToBeMerged);
    Graph finalOriginalGraph = originalGraph;
    Graph finalGraphToBeMerged = graphToBeMerged;
    Executable throwable = () -> finalOriginalGraph
        .traversable()
        .merge(finalGraphToBeMerged, DeduplicateOptions.SAME_EDGE_TYPES_BETWEEN_SAME_NODES);
    Assertions.assertThrows(GraphEdgesCannotBeMerged.class, throwable);
  }
}
