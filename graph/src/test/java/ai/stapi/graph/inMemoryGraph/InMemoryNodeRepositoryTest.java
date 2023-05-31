package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeWithSameIdAlreadyExists;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.MoreThanOneNodeOfTypeFoundException;
import ai.stapi.graph.exceptions.NodeOfTypeNotFoundException;
import ai.stapi.graph.inMemoryGraph.exceptions.CannotCreateGraphWithOtherThanInputElements;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class InMemoryNodeRepositoryTest extends UnitTestCase {

  private InMemoryGraphRepository graph;

  @BeforeEach
  void setUp() {
    graph = new InMemoryGraphRepository();
  }

  protected NodeRepository getNodeRepository() {
    return this.graph;
  }

  protected EdgeRepository getEdgeRepository() {
    return this.graph;
  }

  @Test
  void itCanCreateWithNodes() {
    var actualGraph = new Graph(
        new InputNode("A"),
        new InputNode("B")
    );
  }

  @Test
  void itCannotCreateWithTraversableNodes() {
    Executable executable = () -> new Graph(
        new TraversableNode("A")
    );

    Assertions.assertThrows(
        CannotCreateGraphWithOtherThanInputElements.class,
        executable
    );
  }

  @Test
  void itCanMergeOverwriteWithOtherGraph_WithNodes() {
    var nodeInGraph1 = new InputNode(
        "merged_node_type",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var nodeInGraph2 = new InputNode(
        nodeInGraph1.getId(),
        "merged_node_type",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("updated value")),
        new LeafAttribute<>("new", new StringAttributeValue("new value"))
    );

    var graphG1 = new Graph(
        nodeInGraph1,
        new InputNode("already saved node")
    );

    var graphG2 = new Graph(
        nodeInGraph2,
        new InputNode("node from other graph")
    );

    graphG1 = graphG1.merge(graphG2);
    this.thenGraphApproved(graphG1);
  }

  @Test
  void itCanLoadNodesByNodeType() {
    var nodeA = new InputNode(
        "type_A",
        new LeafAttribute<>("original", new StringAttributeValue("original value")),
        new LeafAttribute<>("updated", new StringAttributeValue("old value"))
    );

    var nodeB = new InputNode(
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
    var nodeA = new InputNode("type_A");
    var nodeB = new InputNode("type_A");

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
    var nodeA = new InputNode("type_A");
    var nodeB = new InputNode("type_A");

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
    var nodeA = new InputNode("type_A");
    var nodeB = new InputNode("type_B");

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
    var node = new InputNode("type_A");
    var graph = new Graph(node);
    Assertions.assertTrue(graph.containsNodeOfType("type_A"));
    Assertions.assertFalse(graph.containsNodeOfType("type_B"));
  }

  @Test
  void itShouldNotLoadNode_ByIdAndType_WhenEmpty() throws NodeNotFound {
    Executable runnable =
        () -> getNodeRepository().loadNode(UniversallyUniqueIdentifier.randomUUID(), "irrelevant");

    Assertions.assertThrows(NodeNotFound.class, runnable);
  }

  @Test
  void itShouldNotLoadNode_ById_WhenEmpty() throws NodeNotFound {
    Executable runnable =
        () -> getNodeRepository().loadNode(UniversallyUniqueIdentifier.randomUUID());

    Assertions.assertThrows(NodeNotFound.class, runnable);
  }

  @Test
  void itShouldNotLoadNode_ByIdAndType_WhenNodeTypeDiffers() throws NodeNotFound {
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var alreadySavedNode = new InputNode(sameId, "Already_saved_type");
    getNodeRepository().save(alreadySavedNode);
    Executable runnable = () -> getNodeRepository().loadNode(sameId, "not_matching_type");

    Assertions.assertThrows(NodeNotFound.class, runnable);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenNodeNotExists() throws NodeNotFound {
    var actuallyExists = getNodeRepository().nodeExists(
        UniversallyUniqueIdentifier.randomUUID(),
        "not_existing_type"
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenNodeExistsWithSameIdButDifferentType()
      throws NodeNotFound {
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var alreadySavedNode = new InputNode(sameId, "Some_type");
    getNodeRepository().save(alreadySavedNode);
    var actuallyExists = getNodeRepository().nodeExists(
        sameId,
        "different_type"
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenNodeExistsWithSameTypeButDifferentId()
      throws NodeNotFound {
    var sameType = "Same_type";
    var alreadySavedNode = new InputNode(UniversallyUniqueIdentifier.randomUUID(), sameType);
    getNodeRepository().save(alreadySavedNode);
    var actuallyExists = getNodeRepository().nodeExists(
        UniversallyUniqueIdentifier.randomUUID(),
        sameType
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenNodeExists() throws NodeNotFound {
    var sameType = "Same_type";
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var alreadySavedNode = new InputNode(sameId, sameType);
    getNodeRepository().save(alreadySavedNode);
    var actuallyExists = getNodeRepository().nodeExists(
        sameId,
        sameType
    );
    Assertions.assertTrue(actuallyExists);
  }


  @Test
  void itShouldSaveAndLoadNode_ByIdAndType() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    Assertions.assertEquals(expectedNode.getId(), actualNode.getId());
    Assertions.assertEquals(expectedNode.getType(), actualNode.getType());
  }

  @Test
  void itShouldSaveAndLoadNode_ById() throws NodeNotFound {
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var expectedNode = new InputNode(sameId, "Already_saved_type");
    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(sameId);

    Assertions.assertEquals(expectedNode.getId(), actualNode.getId());
    Assertions.assertEquals(expectedNode.getType(), actualNode.getType());
  }

  @Test
  void itShouldNotSaveNodeWhenNodeWithSameIdAlreadySaved() {
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var alreadySavedNode = new InputNode(sameId, "Irrelevant_type");
    getNodeRepository().save(alreadySavedNode);

    var newNode = new InputNode(sameId, "Irrelevant_type");
    Executable executable = () -> getNodeRepository().save(newNode);

    Assertions.assertThrows(NodeWithSameIdAlreadyExists.class, executable);
  }

  @Test
  void itShouldSaveNodeWithVariousAttributes() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    expectedNode = expectedNode.addToNode(
        new LeafAttribute<>("test_attribute_name", new StringAttributeValue("testValueA"))
    );
    expectedNode = expectedNode.addToNode(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true))
    );
    expectedNode = expectedNode.addToNode(
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d))
    );
    expectedNode = expectedNode.addToNode(
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    expectedNode = expectedNode.addToNode(
        new LeafAttribute<>(
            "test_timestamp",
            new InstantAttributeValue(Timestamp.from(Instant.now())))
    );
    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    this.thenNodesAreSame(expectedNode, actualNode);
  }

  @Test
  void itShouldSaveNodeWithManyAttributes() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");

    var expectedVersion1 = new LeafAttribute<>("test_name", new StringAttributeValue("version1"));
    var expectedVersion2 = new LeafAttribute<>("test_name", new StringAttributeValue("versionZ"));
    var expectedVersion3 = new LeafAttribute<>("test_name", new StringAttributeValue("versionX"));
    var expectedVersion4 = new LeafAttribute<>("test_name", new StringAttributeValue("versionU"));
    var expectedVersion5 =
        new LeafAttribute<>("test_name", new StringAttributeValue("versionLast"));

    expectedNode = expectedNode.addToNode(expectedVersion1);
    expectedNode = expectedNode.addToNode(expectedVersion2);
    expectedNode = expectedNode.addToNode(expectedVersion3);
    expectedNode = expectedNode.addToNode(expectedVersion4);
    expectedNode = expectedNode.addToNode(expectedVersion5);

    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    this.thenNodesAreSame(expectedNode, actualNode);
  }


  @Test
  void itShouldSaveNodeTypeToNodeTypesCollection() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    getNodeRepository().save(expectedNode);

    var nodeTypeInfoList = this.getNodeRepository().getNodeTypeInfos();

    Assertions.assertEquals(
        1,
        nodeTypeInfoList.size()
    );
  }

  @Test
  void itShouldSaveNodeTypeToNodeTypesCollectionNTimes() throws NodeNotFound {
    var nodeTypeA1 = new InputNode("Type_A");
    var nodeTypeA2 = new InputNode("Type_A");
    var nodeTypeB1 = new InputNode("Type_B");

    getNodeRepository().save(nodeTypeA1);
    getNodeRepository().save(nodeTypeA2);
    getNodeRepository().save(nodeTypeB1);

    var nodeTypeInfoList = this.getNodeRepository().getNodeTypeInfos();

    var typeAInfo = nodeTypeInfoList.stream().filter(
        nodeTypeInfo -> nodeTypeInfo.getType().equals("Type_A")
    ).findFirst().get();

    var typeBInfo = nodeTypeInfoList.stream().filter(
        nodeTypeInfo -> nodeTypeInfo.getType().equals("Type_B")
    ).findFirst().get();

    Assertions.assertEquals(
        2,
        nodeTypeInfoList.size()
    );
    Assertions.assertEquals(
        2,
        typeAInfo.getCount()
    );
    Assertions.assertEquals(
        1,
        typeBInfo.getCount()
    );
  }

  @Test
  void itShouldProperlyReturnNodeInfo() throws NodeNotFound {
    var nodeTypeA1 = new InputNode("Type_A");
    nodeTypeA1 = nodeTypeA1.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("oldName")));
    nodeTypeA1 = nodeTypeA1.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("newName")));
    var nodeTypeA2 = new InputNode("Type_A");
    var nodeTypeB1 = new InputNode("Type_B");

    getNodeRepository().save(nodeTypeA1);
    getNodeRepository().save(nodeTypeA2);
    getNodeRepository().save(nodeTypeB1);

    var nodeInfoList = this.getNodeRepository().getNodeInfosBy("Type_A");

    Assertions.assertEquals(
        2,
        nodeInfoList.size()
    );
    Assertions.assertEquals(
        "Type_A",
        nodeInfoList.get(0).getName()
    );
    Assertions.assertEquals(
        "newName",
        nodeInfoList.get(1).getName()
    );
  }

  @Test
  void itShouldLoadNodeWithEdges() throws NodeNotFound {
    var expectedNode = new InputNode("Expected_node_type");
    var node2 = new InputNode("Test_node1_type");
    var node3 = new InputNode("Test_node2_type");
    var edge = new InputEdge(
        expectedNode,
        "has",
        node2
    );
    var edge2 = new InputEdge(
        expectedNode,
        "has",
        node3
    );

    getNodeRepository().save(expectedNode);
    getNodeRepository().save(node2);
    getNodeRepository().save(node3);
    getEdgeRepository().save(edge);
    getEdgeRepository().save(edge2);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    Assertions.assertEquals(
        2,
        actualNode.getEdges().size()
    );
  }

  @Test
  void itShouldReplaceNode() {
    var alreadySavedNode = new InputNode("Same");
    alreadySavedNode = alreadySavedNode.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var replacingNode = new InputNode(alreadySavedNode.getId(), "Same");
    replacingNode = replacingNode.addToNode(
        new LeafAttribute<>("alias", new StringAttributeValue("alias")));
    getNodeRepository().save(alreadySavedNode);
    //When
    getNodeRepository().replace(replacingNode);
    //Then
    var actualFoundNode = getNodeRepository().loadNode(alreadySavedNode.getId());
    Assertions.assertTrue(actualFoundNode.hasAttribute("alias"));
    Assertions.assertFalse(actualFoundNode.hasAttribute("name"));
  }

  @Test
  void itShouldRemoveNode() {
    var alreadySavedNode = new InputNode("Test_node");
    alreadySavedNode = alreadySavedNode.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    getNodeRepository().save(alreadySavedNode);
    //When
    getNodeRepository().removeNode(alreadySavedNode.getId(), "Test_node");
    //Then
    InputNode finalAlreadySavedNode = alreadySavedNode;
    Executable executable =
        () -> getNodeRepository().loadNode(finalAlreadySavedNode.getId(), "Test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveNodeForRemoval() {
    var alreadySavedNode = new InputNode("Test_node");
    alreadySavedNode = alreadySavedNode.addToNode(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(alreadySavedNode.getId(), alreadySavedNode.getType());

    getNodeRepository().save(alreadySavedNode);
    //When
    getNodeRepository().removeNode(nodeForRemoval);
    //Then
    InputNode finalAlreadySavedNode = alreadySavedNode;
    Executable executable =
        () -> getNodeRepository().loadNode(finalAlreadySavedNode.getId(), "Test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveEdgesContainedInRemovedNode() {
    var alreadySavedNode = new InputNode("Test_node");
    var someOtherNode = new InputNode("Other_node");
    var alreadySaveEdge = new InputEdge(
        alreadySavedNode,
        "test_edge",
        someOtherNode
    );

    getNodeRepository().save(alreadySavedNode);
    getNodeRepository().save(someOtherNode);
    getEdgeRepository().save(alreadySaveEdge);
    //When
    getNodeRepository().removeNode(alreadySavedNode.getId(), "Test_node");
    //Then
    Executable executable =
        () -> getEdgeRepository().loadEdge(alreadySaveEdge.getId(), "test_edge");
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }

  @Test
  void itShouldSaveNodeWithListAttributeWithMoreVersions() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    expectedNode = expectedNode.addToNode(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB")
        )
    );
//        expectedNode = expectedNode.addToNode(
//            new SetAttribute<>(
//                "test_set_attribute_name",
//                new StringAttributeValue("testValueA"),
//                new StringAttributeValue("testValueB")
//            )
//        );
    expectedNode = expectedNode.addToNode(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueA")
        )
    );
//        expectedNode = expectedNode.addToNode(
//            new SetAttribute<>(
//                "test_set_attribute_name",
//                new StringAttributeValue("testValueB"),
//                new StringAttributeValue("testValueA")
//            )
//        );
    expectedNode = expectedNode.addToNode(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueC")
        )
    );
//        expectedNode = expectedNode.addToNode(
//            new SetAttribute<>(
//                "test_set_attribute_name",
//                new StringAttributeValue("testValueA"),
//                new StringAttributeValue("testValueB"),
//                new StringAttributeValue("testValueC")
//            )
//        );
    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    this.thenNodesAreSame(expectedNode, actualNode);
  }

  @Test
  void itShouldSaveAndLoadNodeWithUnionTypeAttribute() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    expectedNode = expectedNode.addToNode(
        new ListAttribute(
            "test_union_type_attribute",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueC")
        )
    );
    getNodeRepository().save(expectedNode);

    var secondExpectedNode = new InputNode("Test_node_type");
    secondExpectedNode = secondExpectedNode.addToNode(
        new ListAttribute(
            "test_union_type_attribute",
            new IntegerAttributeValue(0),
            new IntegerAttributeValue(1),
            new IntegerAttributeValue(2)
        )
    );
    getNodeRepository().save(secondExpectedNode);

    var thirdExpectedNode = new InputNode("Test_node_type");
    thirdExpectedNode = thirdExpectedNode.addToNode(
        new ListAttribute(
            "test_union_type_attribute",
            new DateAttributeValue(Timestamp.from(Instant.now())),
            new DateAttributeValue(Timestamp.from(Instant.now())),
            new DateAttributeValue(Timestamp.from(Instant.now()))
        )
    );
    getNodeRepository().save(thirdExpectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    this.thenNodesAreSame(expectedNode, actualNode);

    var secondActualNode = getNodeRepository().loadNode(
        secondExpectedNode.getId(),
        secondExpectedNode.getType()
    );

    this.thenNodesAreSame(secondExpectedNode, secondActualNode);

    var thirdActualNode = getNodeRepository().loadNode(
        thirdExpectedNode.getId(),
        thirdExpectedNode.getType()
    );

    this.thenNodesAreSame(thirdExpectedNode, thirdActualNode);
  }

  @Test
  void itShouldSaveAndLoadNode_WithAttributeWithMetaData() throws NodeNotFound {
    var expectedNode = new InputNode("Test_node_type");
    var expectedLeafMetadataValue = "Example Meta Data Value";
    var expectedListMetaDataValue = "Example Meta Data List Value";
    var exampleLeafAttributeName = "exampleLeafAttributeName";
    var exampleListAttributeName = "exampleListAttributeName";
    var exampleMetaData = "exampleMetaData";
    var expectedAttribute = new LeafAttribute<>(
        exampleLeafAttributeName,
        Map.of(
            exampleMetaData, new MetaData(
                exampleMetaData,
                new StringAttributeValue(expectedLeafMetadataValue)
            )
        ),
        new StringAttributeValue("Example Attribute Value"));
    var expectedListAttribute = new ListAttribute(
        exampleListAttributeName,
        Map.of(
            exampleMetaData, new MetaData(
                exampleMetaData,
                new StringAttributeValue(expectedListMetaDataValue)
            )
        ),
        new StringAttributeValue("Example List Attribute Value")
    );

    getNodeRepository().save(
        expectedNode
            .addToNode(expectedAttribute)
            .addToNode(expectedListAttribute)
    );

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );
    var actualLeafAttribute = actualNode.getAttribute(exampleLeafAttributeName);
    var actualListAttribute = actualNode.getAttribute(exampleListAttributeName);

    Assertions.assertEquals(
        expectedLeafMetadataValue,
        actualLeafAttribute.getMetaData().get(exampleMetaData).getValues().get(0).getValue()
    );
    Assertions.assertEquals(
        expectedListMetaDataValue,
        actualListAttribute.getMetaData().get(exampleMetaData).getValues().get(0).getValue()
    );
  }
}
