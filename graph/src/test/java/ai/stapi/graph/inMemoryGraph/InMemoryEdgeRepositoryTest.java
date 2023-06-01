package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.Graph;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.EdgeWithSameIdAlreadyExists;
import ai.stapi.graph.exceptions.OneOrBothNodesOnEdgeDoesNotExist;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class InMemoryEdgeRepositoryTest extends UnitTestCase {

  private InMemoryGraphRepository inMemoryGraphRepository;

  @BeforeEach
  void setUp() {
    this.inMemoryGraphRepository = new InMemoryGraphRepository();
  }

  private NodeRepository getNodeRepository() {
    return inMemoryGraphRepository;
  }

  private EdgeRepository getEdgeRepository() {
    return inMemoryGraphRepository;
  }

  @Test
  void itShouldNotLoadEdge_WhenEmpty() {
    Executable runnable =
        () -> getEdgeRepository().loadEdge(UniversallyUniqueIdentifier.randomUUID(), "irrelevant");

    Assertions.assertThrows(EdgeNotFound.class, runnable);
  }

  @Test
  void itShouldNotSaveEdge_WhenNodeFromDoesNotExist() {
    var existingNodeTo = new InputNode("Test_node_type");
    getNodeRepository().save(existingNodeTo);
    var expectedEdge = new InputEdge(
        existingNodeTo,
        "test_edge_type",
        new InputNode("Test_node_type")
    );

    Executable runnable = () -> getEdgeRepository().save(expectedEdge);
    Assertions.assertThrows(OneOrBothNodesOnEdgeDoesNotExist.class, runnable);
  }

  @Test
  void itShouldNotSaveEdge_WhenNodeToDoesNotExist() {
    var existingNodeFrom = new InputNode("Test_node_type");
    getNodeRepository().save(existingNodeFrom);
    var expectedEdge = new InputEdge(
        new InputNode("Test_node_type"),
        "test_edge_type",
        existingNodeFrom
    );

    Executable runnable = () -> getEdgeRepository().save(expectedEdge);
    Assertions.assertThrows(OneOrBothNodesOnEdgeDoesNotExist.class, runnable);
  }

  @Test
  void itShouldSaveEdgeWithoutAttributes() throws OneOrBothNodesOnEdgeDoesNotExist {
    var nodeFrom = new InputNode("Test_node_type");
    var nodeTo = new InputNode("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var expectedEdge = new InputEdge(
        nodeFrom,
        "test_edge_type",
        nodeTo
    );

    getEdgeRepository().save(expectedEdge);

    var actualEdge = getEdgeRepository().loadEdge(
        expectedEdge.getId(),
        expectedEdge.getType()
    );

    Assertions.assertEquals(
        expectedEdge.getId(),
        actualEdge.getId()
    );
    Assertions.assertEquals(expectedEdge.getType(), actualEdge.getType());
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenEdgeNotExists() throws EdgeNotFound {
    var actuallyExists = getEdgeRepository().edgeExists(
        UniversallyUniqueIdentifier.randomUUID(),
        "not_existing_type"
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenEdgeExistsWithSameIdButDifferentType()
      throws EdgeNotFound {
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        "some_type",
        nodeTo
    );
    getEdgeRepository().save(alreadySavedEdge);
    var actuallyExists = getEdgeRepository().edgeExists(
        alreadySavedEdge.getId(),
        "different_type"
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenEdgeExistsWithSameTypeButDifferentId()
      throws EdgeNotFound {
    var sameType = "same_type";
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        sameType,
        nodeTo
    );
    getEdgeRepository().save(alreadySavedEdge);
    var actuallyExists = getEdgeRepository().edgeExists(
        UniversallyUniqueIdentifier.randomUUID(),
        sameType
    );
    Assertions.assertFalse(actuallyExists);
  }

  @Test
  void itShouldTellWhetherExists_ByIdAndType_WhenEdgeExists() throws EdgeNotFound {
    var sameType = "same_type";
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        sameType,
        nodeTo
    );
    getEdgeRepository().save(alreadySavedEdge);
    var actuallyExists = getEdgeRepository().edgeExists(
        alreadySavedEdge.getId(),
        sameType
    );
    Assertions.assertTrue(actuallyExists);
  }

  @Test
  void itShouldNotSaveEdgeWhenEdgeWithSameIdAlreadySaved() {
    var nodeFrom = new InputNode("Irrelevant_type");
    var nodeTo = new InputNode("Irrelevant_type");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedNode = new InputEdge(
        nodeFrom,
        "irrelevant_type",
        nodeTo
    );
    getEdgeRepository().save(alreadySavedNode);

    var newEdge = new InputEdge(
        alreadySavedNode.getId(),
        nodeFrom,
        "irrelevant_type",
        nodeTo
    );
    Executable executable = () -> getEdgeRepository().save(newEdge);

    Assertions.assertThrows(EdgeWithSameIdAlreadyExists.class, executable);
  }

  @Test
  void itShouldSaveEdgeWithVariousAttributes() throws OneOrBothNodesOnEdgeDoesNotExist {
    var nodeFrom = new InputNode("Test_node_type");
    var nodeTo = new InputNode("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var expectedEdge = new InputEdge(
        nodeFrom, "test_edge_type",
        nodeTo
    );
    expectedEdge = expectedEdge.addToEdge(
        new LeafAttribute<>("test_attribute_name", new StringAttributeValue("testValueA"
        ))
    );
    expectedEdge = expectedEdge.addToEdge(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true
        ))
    );
    expectedEdge = expectedEdge.addToEdge(
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d
        ))
    );
    expectedEdge = expectedEdge.addToEdge(
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    expectedEdge = expectedEdge.addToEdge(
        new LeafAttribute<>("test_timestamp",
            new InstantAttributeValue(Timestamp.valueOf(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
                    .format(Date.from(Instant.now()))
            )))
    );
    getEdgeRepository().save(expectedEdge);

    var actualEdge = getEdgeRepository().loadEdge(
        expectedEdge.getId(),
        expectedEdge.getType()
    );

    this.thenEdgesAreSame(expectedEdge, actualEdge);
  }

  @Test
  void itShouldSaveEdgeTypeToEdgeTypesCollectionNTimes() throws EdgeNotFound {
    var node1 = new InputNode("Irrelevant");
    var node2 = new InputNode("Irrelevant");

    var edgeTypeA1 = new InputEdge(
        node1,
        "type_A",
        node2
    );
    var edgeTypeA2 = new InputEdge(
        node2,
        "type_A",
        node1
    );
    var edgeTypeB1 = new InputEdge(
        node1,
        "type_B",
        node2
    );

    getNodeRepository().save(node1);
    getNodeRepository().save(node2);
    getEdgeRepository().save(edgeTypeA1);
    getEdgeRepository().save(edgeTypeA2);
    getEdgeRepository().save(edgeTypeB1);

    var edgeTypeInfoList = getEdgeRepository().getEdgeTypeInfos();

    var typeAInfo = edgeTypeInfoList.stream().filter(
        edgeTypeInfo -> edgeTypeInfo.getType().equals("type_A")
    ).findFirst().get();

    var typeBInfo = edgeTypeInfoList.stream().filter(
        edgeTypeInfo -> edgeTypeInfo.getType().equals("type_B")
    ).findFirst().get();

    Assertions.assertEquals(
        2,
        edgeTypeInfoList.size()
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
  void itShouldFindEdgesByNode() throws OneOrBothNodesOnEdgeDoesNotExist {
    var examinationNode = new InputNode("Examination");
    var atlasNode = new InputNode("Lab_atlas");
    var rangeNode = new InputNode("Examination_range");
    getNodeRepository().save(examinationNode);
    getNodeRepository().save(atlasNode);
    getNodeRepository().save(rangeNode);

    var foundEdge1 = new InputEdge(
        examinationNode,
        "has",
        rangeNode
    );
    var foundEdge2 = new InputEdge(
        atlasNode,
        "contains",
        examinationNode
    );
    var notFoundEdge3 = new InputEdge(
        rangeNode,
        "comes_from",
        atlasNode
    );

    getEdgeRepository().save(foundEdge1);
    getEdgeRepository().save(foundEdge2);
    getEdgeRepository().save(notFoundEdge3);

    var actualEdges = getEdgeRepository().findInAndOutEdgesForNode(
        examinationNode.getId(),
        examinationNode.getType()
    );

    Assertions.assertEquals(
        2,
        actualEdges.size()
    );

    this.thenEdgesHaveSameIdAndTypeAndNodeIds(
        foundEdge1,
        actualEdges.stream().filter(
            edge -> edge.getId().equals(foundEdge1.getId())
        ).findFirst().get()
    );

    this.thenEdgesHaveSameIdAndTypeAndNodeIds(
        foundEdge2,
        actualEdges.stream().filter(
            edge -> edge.getId().equals(foundEdge2.getId())
        ).findFirst().get()
    );
  }

  @Test
  void itShouldFindEdgeByTypeAndNodes() {
    var nodeFrom = new InputNode("Test_node_type");
    var nodeTo = new InputNode("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var notMatchedEdge = new InputEdge(
        nodeFrom,
        "not_matched_edge",
        nodeTo
    );

    getEdgeRepository().save(notMatchedEdge);

    var matchedEdge = new InputEdge(
        nodeFrom,
        "matched_edge",
        nodeTo
    );

    getEdgeRepository().save(matchedEdge);
    //When
    var actualFoundEdge = getEdgeRepository().findEdgeByTypeAndNodes(
        "matched_edge",
        new NodeIdAndType(nodeFrom.getId(), nodeFrom.getType()),
        new NodeIdAndType(nodeTo.getId(), nodeTo.getType())
    );
    //Then
    Assertions.assertEquals(matchedEdge.getId(), actualFoundEdge.getId());
  }

  @Test
  void itShouldReplaceEdge() {
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        "same",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.addToEdge(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var replacingEdge = new InputEdge(
        alreadySavedEdge.getId(),
        nodeFrom,
        "same",
        nodeTo
    );
    replacingEdge = replacingEdge.addToEdge(
        new LeafAttribute<>("alias", new StringAttributeValue("alias")));
    getEdgeRepository().save(alreadySavedEdge);
    //When
    getEdgeRepository().replace(replacingEdge);
    //Then
    var actualFoundEdge = getEdgeRepository().loadEdge(
        alreadySavedEdge.getId(),
        alreadySavedEdge.getType()
    );
    Assertions.assertTrue(actualFoundEdge.hasAttribute("alias"));
    Assertions.assertFalse(actualFoundEdge.hasAttribute("name"));
  }

  @Test
  void itShouldRemoveEdge() {
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.addToEdge(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    getEdgeRepository().save(alreadySavedEdge);
    //When
    getEdgeRepository().removeEdge(alreadySavedEdge.getId(), alreadySavedEdge.getType());
    //Then
    InputEdge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> getEdgeRepository().loadEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveEdgeForRemoval() {
    var nodeFrom = new InputNode("Node_from");
    var nodeTo = new InputNode("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new InputEdge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.addToEdge(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var edgeForRemoval = new EdgeForRemoval(alreadySavedEdge.getId(), alreadySavedEdge.getType());

    getEdgeRepository().save(alreadySavedEdge);
    //When
    getEdgeRepository().removeEdge(edgeForRemoval);
    //Then
    InputEdge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> getEdgeRepository().loadEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }
}
