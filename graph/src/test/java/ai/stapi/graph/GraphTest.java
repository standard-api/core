package ai.stapi.graph;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.EdgeWithSameIdAndTypeAlreadyExists;
import ai.stapi.graph.exceptions.MoreThanOneNodeOfTypeFoundException;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeOfTypeNotFoundException;
import ai.stapi.graph.exceptions.NodeWithSameIdAndTypeAlreadyExists;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.inMemoryGraph.DeduplicateOptions;
import ai.stapi.graph.inMemoryGraph.exceptions.CannotCreateGraphWithOtherThanGraphElements;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class GraphTest extends UnitTestCase {

  @Test
  void itCanCreateWithNodes() {
    var actualGraph = new Graph(
        new Node("arbitraty_type_A"),
        new Node("arbitraty_type_B")
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
  void itThrowsExceptionWhenAddingNodeOfSameIdAndType() {
    var initialGraph = new Graph();
    var sameId = UniqueIdentifier.fromString("sameId");
    var node = new Node(sameId,"SameType");
    var sameIdNode = new Node(sameId, "SameType");
    var testedGraph = initialGraph.with(node);

    Executable when = () -> testedGraph.with(sameIdNode);

    //Then
    Assertions.assertThrows(NodeWithSameIdAndTypeAlreadyExists.class, when);
  }

  @Test
  void itCanAddNodeOfSameIdAndDifferentType() {
    var initialGraph = new Graph();
    var sameId = UniqueIdentifier.fromString("sameId");
    var originalFirstNodeType = "original_first_node";
    var node = new Node(sameId, originalFirstNodeType);
    var secondNodeType = "second_node";
    var sameIdNode = new Node(sameId, secondNodeType);
    var testedGraph = initialGraph.with(node).with(sameIdNode);
    
    Assertions.assertTrue(testedGraph.nodeExists(sameId, originalFirstNodeType));
    Assertions.assertTrue(testedGraph.nodeExists(sameId, secondNodeType));
  }

  @Test
  void itCanAddEdgeOfSameIdAndDifferentType() {
    var nodeA = new Node("IrrelevantNodeType");
    var nodeB = new Node("IrrelevantNodeType");    
    var nodeC = new Node("IrrelevantNodeType");
    var nodeD = new Node("IrrelevantNodeType");
    var sameId = UniqueIdentifier.fromString("SameId");
    var sameIdEdge = new Edge(sameId, nodeA, "originalType", nodeB);
    var initialGraph = new Graph(
        nodeA, 
        nodeB,
        nodeC,
        nodeD,
        sameIdEdge
    );

    var testedEdge = new Edge(sameId, nodeC, "differentType", nodeD);

    //When
    var testedGraph = initialGraph.with(testedEdge);

    //Then
    Assertions.assertTrue(testedGraph.edgeExists(sameId, sameIdEdge.getType()));
    Assertions.assertTrue(testedGraph.edgeExists(sameId, testedEdge.getType()));
  }

  @Test
  void itThrowsExceptionWhenAddingEdgeOfSameIdAndType() {
    var nodeA = new Node("IrrelevantNodeType");
    var nodeB = new Node("IrrelevantNodeType");
    var nodeC = new Node("IrrelevantNodeType");
    var nodeD = new Node("IrrelevantNodeType");
    var sameId = UniqueIdentifier.fromString("SameId");
    var sameType = "sameType";
    var sameIdEdge = new Edge(sameId, nodeA, sameType, nodeB);
    var initialGraph = new Graph(
        nodeA,
        nodeB,
        nodeC,
        nodeD,
        sameIdEdge
    );

    var testedEdge = new Edge(sameId, nodeC, sameType, nodeD);

    //When
    Executable when = () -> initialGraph.with(testedEdge);

    //Then
    Assertions.assertThrows(EdgeWithSameIdAndTypeAlreadyExists.class, when);
  }

  @Test
  void itThrowsExceptionWhenGettingNonExistentNode() {
    var nodeA = new Node("MatchingNodeType");
    var nodeB = new Node("NotMatchingNodeType");
    var testedGraph = new Graph(
        nodeA, nodeB
    );

    Executable when = () -> testedGraph.getNode(new UniqueIdentifier("nonexistentId"), nodeA.getType());

    //Then
    Assertions.assertThrows(NodeNotFound.class, when);
  }

  @Test
  void itCanGetNode() {
    var nodeA = new Node("MatchingNodeType");
    var nodeB = new Node("NotMatchingNodeType");
    var testedGraph = new Graph(
        nodeA, nodeB
    );

    var returnedNode = testedGraph.getNode(nodeA.getId(), nodeA.getType());

    //Then
    Assertions.assertEquals(nodeA.getId(), returnedNode.getId());
  }

  @Test
  void itThrowsExceptionWhenGettingNonExistentNodeByIdAndType() {
    var nodeA = new Node("MatchingNodeType");
    var nodeB = new Node("IrrelevantNodeType");
    var testedGraph = new Graph(
        nodeA, nodeB
    );

    Executable when = () -> testedGraph.getNode(
        new UniqueIdentifier("nonexistentId"), "MatchingNodeType");
    //Then
    Assertions.assertThrows(NodeNotFound.class, when);

    Executable when2 = () -> testedGraph.getNode(nodeA.getId(), "NotMatchingNodeType");
    //Then
    Assertions.assertThrows(NodeNotFound.class, when2);
  }

  @Test
  void itThrowsExceptionWhenGettingNonExistentEdge() {
    var nodeA = new Node("MatchingNodeType");
    var nodeB = new Node("NotMatchingNodeType");
    var edgeA = new Edge(nodeA, "edgeType", nodeB);
    var edgeB = new Edge(nodeA, "irrelevantEdgeType", nodeB);
    var testedGraph = new Graph(
        nodeA, nodeB, edgeA, edgeB
    );

    Executable when = () -> testedGraph.getEdge(new UniqueIdentifier("nonexistentId"), edgeA.getType());

    //Then
    Assertions.assertThrows(EdgeNotFound.class, when);
  }

  @Test
  void itCanGetEdge() {
    var nodeA = new Node("MatchingNodeType");
    var nodeB = new Node("NotMatchingNodeType");
    var edgeA = new Edge(nodeA, "edgeType", nodeB);
    var edgeB = new Edge(nodeA, "irrelevantEdgeType", nodeB);
    var testedGraph = new Graph(
        nodeA, nodeB, edgeA, edgeB
    );

    var returnedEdge = testedGraph.getEdge(edgeA.getId(), edgeA.getType());

    //Then
    Assertions.assertEquals(edgeA.getId(), returnedEdge.getId());
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
    var nodeA1 = new Node(
        "type_A",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var nodeA2 = new Node(
        "type_A",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
        new LeafAttribute<>("new", new StringAttributeValue("new value"))
    );

    var nodeB = new Node(
        "type_B",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var graph = new Graph(
        nodeA1,
        nodeA2,
        nodeB
    );

    Assertions.assertEquals(2, graph.getAllNodes("type_A").size());

  }

  @Test
  void itThrowsExceptionWhenLoadingExactlyOneNodeOfTypeButMoreThanOneIsPresent() {
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
    basicTestGraph();
  }

  @Test
  void itCanGetNodeTypeInfos() {
    var graph = basicTestGraph();

    this.thenObjectApproved(graph.getNodeTypeInfos());
  }

  @Test
  void itCanGetEdgeTypeInfos() {
    var graph = basicTestGraph();

    this.thenObjectApproved(graph.getEdgeTypeInfos());
  }

  @Test
  void itCanGetNodeInfosBy() {
    var graph = basicTestGraph();

    this.thenObjectApproved(graph.getNodeInfosBy("nodeA"));
  }

  private static Graph basicTestGraph() {
    var nodeA1 = new Node("nodeA");
    var nodeA2 = new Node("nodeA");
    var nodeB = new Node("nodeB");
    var nodeC = new Node("nodeC");
    var nodeD1 = new Node("nodeD");
    var nodeD2 = new Node("nodeD");
    return new Graph(
        nodeA1,
        nodeA2,
        nodeB,
        nodeC,
        nodeD1,
        nodeD2,
        new Edge(
            nodeA1,
            "edgeA",
            nodeB
        ),
        new Edge(
            nodeC,
            "edgeB",
            nodeD1
        ),
        new Edge(
            nodeA1,
            "edgeB",
            nodeD2
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
  void itCanMergeTwoGraphsByIds() {
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
  void itCanReplaceNode() {
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
  void itCanReplaceEdge() {
    var nodeA = new Node("ArbitraryNodeType");
    var nodeB = new Node("ArbitraryNodeType");

    var alreadySavedEdge = new Edge(
        nodeA,
        "test_edge",
        nodeB
    );
    var replacingEdge = new Edge(
        alreadySavedEdge.getId(),
        nodeA,
        "test_edge",
        nodeB
    );

    nodeA = nodeA.add(
        new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value"))
    );
    nodeB = nodeB.add(
        new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value"))
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>(
            "Edge_attribute_original_type",
            new StringAttributeValue("Edge_attribute_value_original")
        )
    );
    replacingEdge = replacingEdge.add(
        new LeafAttribute<>(
            "Edge_attribute_new_type",
            new StringAttributeValue("Edge_attribute_value_value_new")
        )
    );

    var graph = new Graph(nodeA, nodeB, alreadySavedEdge);

    var actualGraph = graph.replace(replacingEdge);
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanReplaceEdgeWithSameIdButDifferentNodes() {
    var nodeA = new Node("ArbitraryNodeType1");
    var nodeB = new Node("ArbitraryNodeType2");
    var nodeC = new Node("ArbitraryNodeType3");

    var alreadySavedEdge = new Edge(
        nodeA,
        "test_edge",
        nodeB
    );
    var replacingEdge = new Edge(
        alreadySavedEdge.getId(),
        nodeA,
        "test_edge",
        nodeC
    );

    nodeA = nodeA.add(
        new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value"))
    );
    nodeB = nodeB.add(
        new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value"))
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>(
            "Edge_attribute_original_type",
            new StringAttributeValue("Edge_attribute_value_original")
        )
    );
    replacingEdge = replacingEdge.add(
        new LeafAttribute<>(
            "Edge_attribute_new_type",
            new StringAttributeValue("Edge_attribute_value_value_new")
        )
    );

    var graph = new Graph(nodeA, nodeB, nodeC, alreadySavedEdge);

    var actualGraph = graph.replace(replacingEdge);
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveNode() {
    var testNodeToRemove = new Node("test_node");
    testNodeToRemove = testNodeToRemove.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(testNodeToRemove.getId(), testNodeToRemove.getType());

    var givenGraph = new Graph(testNodeToRemove);

    //When
    var actualGraph = givenGraph.removeNode(nodeForRemoval);

    //Then
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveNodeIdempotently() {
    var testNodeToRemove = new Node("test_node");
    testNodeToRemove = testNodeToRemove.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(testNodeToRemove.getId(), testNodeToRemove.getType());

    var givenGraph = new Graph(testNodeToRemove);

    var actualGraph = givenGraph.removeNode(nodeForRemoval);

    //When
    actualGraph = actualGraph.removeNode(nodeForRemoval);

    //Then
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveEdge() {
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

    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveEdgeIdempotently() {
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
    var intermitentGraph = givenGraph.removeGraphElement(edgeForRemoval);
    var actualGraph = intermitentGraph.removeGraphElement(edgeForRemoval);

    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveEdgeAndNode() {
    var nodeA = new Node("ArbitratyNodeType1");
    var nodeB = new Node("ArbitratyNodeType2");
    var nodeC = new Node("ArbitratyNodeType3");
    var nodeD = new Node("ArbitratyNodeType4");

    var edgeA = new Edge(
        nodeA,
        "arbitratyEdgeType1",
        nodeB
    );
    var edgeB = new Edge(
        nodeB,
        "arbitratyEdgeType2",
        nodeC
    );
    var edgeC = new Edge(
        nodeC,
        "arbitratyEdgeType3",
        nodeD
    );
    var givenGraph = new Graph(
        nodeA,
        nodeB,
        nodeC,
        nodeD,
        edgeA,
        edgeB,
        edgeC
    );

    var nodeForRemoval = new NodeForRemoval(nodeA.getId(), nodeA.getType());
    var edgeForRemoval = new EdgeForRemoval(edgeC.getId(), edgeC.getType());

    //When
    var actualGraph = givenGraph.removeGraphElements(nodeForRemoval, edgeForRemoval);

    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanRemoveNode_AndNotAffectOthers() {
    var testNodeToRemove = new Node("TestNode");
    var anotherNodeOfSameType = new Node("TestNode").add(
        new LeafAttribute<>("name", new StringAttributeValue("irrelevant"))
    );
    var anotherNodeOfAnotherType = new Node("AnotherType");
    testNodeToRemove = testNodeToRemove.add(
        new LeafAttribute<>("name", new StringAttributeValue("irrelevant")));

    var nodeForRemoval = new NodeForRemoval(testNodeToRemove.getId(), testNodeToRemove.getType());

    var givenGraph = new Graph(
        testNodeToRemove,
        anotherNodeOfSameType,
        anotherNodeOfAnotherType,
        new Edge(testNodeToRemove, "should_remove", anotherNodeOfSameType),
        new Edge(anotherNodeOfSameType, "should_remove", testNodeToRemove),
        new Edge(testNodeToRemove, "should_remove", anotherNodeOfAnotherType),
        new Edge(anotherNodeOfAnotherType, "should_remove", testNodeToRemove),
        new Edge(anotherNodeOfSameType, "should_keep", anotherNodeOfAnotherType)
    );
    //When
    var actualGraph = givenGraph.removeNode(nodeForRemoval);

    //Then
    this.thenGraphApproved(actualGraph);
  }

  @Test
  void itCanMergeEdgesOfSameTypeBetweenSameNodes() {
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
  void itThrowsErrorWhenEdgeCouldBeMergedWithMultipleEdges() {
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
