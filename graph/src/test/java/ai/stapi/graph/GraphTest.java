package ai.stapi.graph;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.inMemoryGraph.DeduplicateOptions;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.test.base.UnitTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class GraphTest extends UnitTestCase {

  @Test
  void itShouldMergeTwoGraphsByIds() {
    var nodeFromInGraphG1 = new InputNode("merged_node_type");
    var nodeToInGraphG1 = new InputNode("node_type_to");
    var edgeE1InGraphG1 = new InputEdge(
        nodeFromInGraphG1,
        "edge_type",
        nodeToInGraphG1
    );

    var nodeFromInGraphG2 = new InputNode(
        nodeFromInGraphG1.getId(),
        nodeFromInGraphG1.getType()
    );
    var nodeToInGraphG2 = new InputNode(
        nodeToInGraphG1.getId(),
        nodeToInGraphG1.getType()
    );
    var edgeE1InGraphG2 = new InputEdge(
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
  
//    
//    @Test
//    void itShouldNotAllowToReplaceNodeOfDifferentType()
//    {
//        var nodeA = new InputNode("type");
//        nodeA = nodeA.addIdentificator(new AttributeNodeIdentificator(
//            nodeA,
//            "identifying_attribute"
//        ));
//
//        var nodeB = new InputNode("different_type");
//        nodeB = nodeB.addIdentificator(new AttributeNodeIdentificator(
//            nodeB,
//            "identifying_attribute"
//        ));
//
//        nodeA = (InputNode) nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
//        nodeB = (InputNode) nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));
//
//        var graph = new Graph(
//            nodeA
//        );
//
//        InputNode finalNodeA = nodeA;
//        InputNode finalNodeB = nodeB;
//        Executable executable = () -> graph.replaceNodeAndKeepEdges(
//            finalNodeA.getId(),
//            finalNodeB
//        );
//        Assertions.assertThrows(UnableToReplaceNode.class, executable);
//    }
//    
//    @Test
//    void itShouldReplaceNodes()
//    {
//        var nodeA = new InputNode("type");
//        nodeA = nodeA.addIdentificator(new AttributeNodeIdentificator(
//            nodeA,
//            "identifying_attribute"
//        ));
//
//        var nodeB = new InputNode("type");
//        nodeB = nodeB.addIdentificator(new AttributeNodeIdentificator(
//            nodeB,
//            "identifying_attribute"
//        ));
//
//        var nodeC = new InputNode("type");
//        nodeC = nodeC.addIdentificator(new AttributeNodeIdentificator(
//            nodeC,
//            "identifying_attribute"
//        ));
//
//        nodeA = (InputNode) nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
//        nodeB = (InputNode) nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));
//        nodeC = (InputNode) nodeC.add(new LeafAttribute<>("irrelevant_attribute", new StringAttributeValue("irrelevant_value")));
//
//        var graph = new Graph(
//            nodeA,
//            nodeC
//        );
//
//        var actualGraph = graph.replaceNodeAndKeepEdges(nodeA.getId(), nodeB);
//
//        this.thenGraphApproved(
//            actualGraph
//        );
//    }

  @Test
  void itShouldRemoveEdge_AsGraphElementForRemoval() {
    var nodeFrom = new InputNode("node_from");
    var nodeTo = new InputNode("node_to");

    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.addToEdge(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var givenGraph = new Graph(nodeFrom, nodeTo, alreadySavedEdge);
    var edgeForRemoval = new EdgeForRemoval(alreadySavedEdge.getId(), alreadySavedEdge.getType());

    //When
    var actualGraph = givenGraph.removeGraphElement(edgeForRemoval);

    //Then
    InputEdge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> actualGraph.getEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveNode_AsGraphElementForRemoval() {
    var alreadySavedNode = new InputNode("test_node");
    alreadySavedNode = alreadySavedNode.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(alreadySavedNode.getId(), alreadySavedNode.getType());

    var givenGraph = new Graph(alreadySavedNode);
    //When
    var actualGraph = givenGraph.removeNode(nodeForRemoval);
    //Then
    InputNode finalAlreadySavedNode = alreadySavedNode;
    Executable executable = () -> actualGraph.getNode(finalAlreadySavedNode.getId(), "test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldMergeEdgesOfSameTypeBetweenSameNodes() {
    var originalGraph = new Graph();
    var firstOriginalNode = new InputNode("original_first_node");
    var secondOriginalNode = new InputNode("original_second_node");
    var firstOriginalEdge = new InputEdge(firstOriginalNode, "original_edge", secondOriginalNode);
    firstOriginalEdge = firstOriginalEdge.addToEdge(
        new LeafAttribute<>("original_attribute", new StringAttributeValue("anyValue")));
    originalGraph = originalGraph.withAll(firstOriginalNode, secondOriginalNode, firstOriginalEdge);

    var graphToBeMerged = new Graph();
    var edgeToBeMerged = new InputEdge(firstOriginalNode, "original_edge", secondOriginalNode);
    edgeToBeMerged = edgeToBeMerged.addToEdge(
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
    var firstOriginalNode = new InputNode("original_first_node");
    var secondOriginalNode = new InputNode("original_second_node");
    var firstOriginalEdge = new InputEdge(firstOriginalNode, "original_edge", secondOriginalNode);
    var secondOriginalEdge = new InputEdge(firstOriginalNode, "original_edge", secondOriginalNode);
    firstOriginalEdge = firstOriginalEdge.addToEdge(
        new LeafAttribute<>("original_attribute", new StringAttributeValue("anyValue")));
    secondOriginalEdge = secondOriginalEdge.addToEdge(
        new LeafAttribute<>("second_original_attribute", new StringAttributeValue("anyValue")));
    originalGraph = originalGraph.withAll(firstOriginalNode, secondOriginalNode, firstOriginalEdge,
        secondOriginalEdge);

    var graphToBeMerged = new Graph();
    var edgeToBeMerged = new InputEdge(firstOriginalNode, "original_edge", secondOriginalNode);
    edgeToBeMerged = edgeToBeMerged.addToEdge(
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
