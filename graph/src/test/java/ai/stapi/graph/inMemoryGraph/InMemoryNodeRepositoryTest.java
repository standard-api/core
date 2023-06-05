package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.NodeNotFound;
import ai.stapi.graph.exceptions.NodeWithSameIdAlreadyExists;
import ai.stapi.graph.graphElementForRemoval.NodeForRemoval;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.identity.UniversallyUniqueIdentifier;
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
    var alreadySavedNode = new Node(sameId, "Already_saved_type");
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
    var alreadySavedNode = new Node(sameId, "Some_type");
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
    var alreadySavedNode = new Node(UniversallyUniqueIdentifier.randomUUID(), sameType);
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
    var alreadySavedNode = new Node(sameId, sameType);
    getNodeRepository().save(alreadySavedNode);
    var actuallyExists = getNodeRepository().nodeExists(
        sameId,
        sameType
    );
    Assertions.assertTrue(actuallyExists);
  }


  @Test
  void itShouldSaveAndLoadNode_ByIdAndType() throws NodeNotFound {
    var expectedNode = new Node("Test_node_type");
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
    var expectedNode = new Node(sameId, "Already_saved_type");
    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(sameId);

    Assertions.assertEquals(expectedNode.getId(), actualNode.getId());
    Assertions.assertEquals(expectedNode.getType(), actualNode.getType());
  }

  @Test
  void itShouldNotSaveNodeWhenNodeWithSameIdAlreadySaved() {
    var sameId = UniversallyUniqueIdentifier.randomUUID();
    var alreadySavedNode = new Node(sameId, "Irrelevant_type");
    getNodeRepository().save(alreadySavedNode);

    var newNode = new Node(sameId, "Irrelevant_type");
    Executable executable = () -> getNodeRepository().save(newNode);

    Assertions.assertThrows(NodeWithSameIdAlreadyExists.class, executable);
  }

  @Test
  void itShouldSaveNodeWithVariousAttributes() throws NodeNotFound {
    var expectedNode = new Node("Test_node_type");
    expectedNode = expectedNode.add(
        new LeafAttribute<>("test_attribute_name", new StringAttributeValue("testValueA"))
    );
    expectedNode = expectedNode.add(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true))
    );
    expectedNode = expectedNode.add(
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d))
    );
    expectedNode = expectedNode.add(
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    expectedNode = expectedNode.add(
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
    var expectedNode = new Node("Test_node_type");

    var expectedVersion1 = new LeafAttribute<>("test_name", new StringAttributeValue("version1"));
    var expectedVersion2 = new LeafAttribute<>("test_name", new StringAttributeValue("versionZ"));
    var expectedVersion3 = new LeafAttribute<>("test_name", new StringAttributeValue("versionX"));
    var expectedVersion4 = new LeafAttribute<>("test_name", new StringAttributeValue("versionU"));
    var expectedVersion5 =
        new LeafAttribute<>("test_name", new StringAttributeValue("versionLast"));

    expectedNode = expectedNode.add(expectedVersion1);
    expectedNode = expectedNode.add(expectedVersion2);
    expectedNode = expectedNode.add(expectedVersion3);
    expectedNode = expectedNode.add(expectedVersion4);
    expectedNode = expectedNode.add(expectedVersion5);

    getNodeRepository().save(expectedNode);

    var actualNode = getNodeRepository().loadNode(
        expectedNode.getId(),
        expectedNode.getType()
    );

    this.thenNodesAreSame(expectedNode, actualNode);
  }


  @Test
  void itShouldSaveNodeTypeToNodeTypesCollection() throws NodeNotFound {
    var expectedNode = new Node("Test_node_type");
    getNodeRepository().save(expectedNode);

    var nodeTypeInfoList = this.getNodeRepository().getNodeTypeInfos();

    Assertions.assertEquals(
        1,
        nodeTypeInfoList.size()
    );
  }

  @Test
  void itShouldSaveNodeTypeToNodeTypesCollectionNTimes() throws NodeNotFound {
    var nodeTypeA1 = new Node("Type_A");
    var nodeTypeA2 = new Node("Type_A");
    var nodeTypeB1 = new Node("Type_B");

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
    var nodeTypeA1 = new Node("Type_A");
    nodeTypeA1 = nodeTypeA1.add(
        new LeafAttribute<>("name", new StringAttributeValue("oldName")));
    nodeTypeA1 = nodeTypeA1.add(
        new LeafAttribute<>("name", new StringAttributeValue("newName")));
    var nodeTypeA2 = new Node("Type_A");
    var nodeTypeB1 = new Node("Type_B");

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
        "Type_A",
        nodeInfoList.get(0).getType()
    );
    Assertions.assertEquals(
        nodeTypeA2.getId().toString(),
        nodeInfoList.get(0).getId()
    );
    Assertions.assertEquals(
        "newName",
        nodeInfoList.get(1).getName()
    );
  }

  @Test
  void itShouldLoadNodeWithEdges() throws NodeNotFound {
    var expectedNode = new Node("Expected_node_type");
    var node2 = new Node("Test_node1_type");
    var node3 = new Node("Test_node2_type");
    var edge = new Edge(
        expectedNode,
        "has",
        node2
    );
    var edge2 = new Edge(
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
    var alreadySavedNode = new Node("Same");
    alreadySavedNode = alreadySavedNode.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var replacingNode = new Node(alreadySavedNode.getId(), "Same");
    replacingNode = replacingNode.add(
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
    var alreadySavedNode = new Node("Test_node");
    alreadySavedNode = alreadySavedNode.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    getNodeRepository().save(alreadySavedNode);
    //When
    getNodeRepository().removeNode(alreadySavedNode.getId(), "Test_node");
    //Then
    Node finalAlreadySavedNode = alreadySavedNode;
    Executable executable =
        () -> getNodeRepository().loadNode(finalAlreadySavedNode.getId(), "Test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveNodeForRemoval() {
    var alreadySavedNode = new Node("Test_node");
    alreadySavedNode = alreadySavedNode.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    var nodeForRemoval = new NodeForRemoval(alreadySavedNode.getId(), alreadySavedNode.getType());

    getNodeRepository().save(alreadySavedNode);
    //When
    getNodeRepository().removeNode(nodeForRemoval);
    //Then
    Node finalAlreadySavedNode = alreadySavedNode;
    Executable executable =
        () -> getNodeRepository().loadNode(finalAlreadySavedNode.getId(), "Test_node");
    Assertions.assertThrows(NodeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveEdgesContainedInRemovedNode() {
    var alreadySavedNode = new Node("Test_node");
    var someOtherNode = new Node("Other_node");
    var alreadySaveEdge = new Edge(
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
    var expectedNode = new Node("Test_node_type");
    expectedNode = expectedNode.add(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB")
        )
    );
//        expectedNode = expectedNode.add(
//            new SetAttribute<>(
//                "test_set_attribute_name",
//                new StringAttributeValue("testValueA"),
//                new StringAttributeValue("testValueB")
//            )
//        );
    expectedNode = expectedNode.add(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueA")
        )
    );
//        expectedNode = expectedNode.add(
//            new SetAttribute<>(
//                "test_set_attribute_name",
//                new StringAttributeValue("testValueB"),
//                new StringAttributeValue("testValueA")
//            )
//        );
    expectedNode = expectedNode.add(
        new ListAttribute(
            "test_list_attribute_name",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueC")
        )
    );
//        expectedNode = expectedNode.add(
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
    var expectedNode = new Node("Test_node_type");
    expectedNode = expectedNode.add(
        new ListAttribute(
            "test_union_type_attribute",
            new StringAttributeValue("testValueA"),
            new StringAttributeValue("testValueB"),
            new StringAttributeValue("testValueC")
        )
    );
    getNodeRepository().save(expectedNode);

    var secondExpectedNode = new Node("Test_node_type");
    secondExpectedNode = secondExpectedNode.add(
        new ListAttribute(
            "test_union_type_attribute",
            new IntegerAttributeValue(0),
            new IntegerAttributeValue(1),
            new IntegerAttributeValue(2)
        )
    );
    getNodeRepository().save(secondExpectedNode);

    var thirdExpectedNode = new Node("Test_node_type");
    thirdExpectedNode = thirdExpectedNode.add(
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
    var expectedNode = new Node("Test_node_type");
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
            .add(expectedAttribute)
            .add(expectedListAttribute)
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
