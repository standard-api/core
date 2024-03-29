package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.EdgeWithSameIdAndTypeAlreadyExists;
import ai.stapi.graph.exceptions.OneOrBothNodesOnEdgeDoesNotExist;
import ai.stapi.graph.graphElementForRemoval.EdgeForRemoval;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.time.Instant;
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
    var existingNodeTo = new Node("Test_node_type");
    getNodeRepository().save(existingNodeTo);
    var expectedEdge = new Edge(
        existingNodeTo,
        "test_edge_type",
        new Node("Test_node_type")
    );

    Executable runnable = () -> getEdgeRepository().save(expectedEdge);
    Assertions.assertThrows(OneOrBothNodesOnEdgeDoesNotExist.class, runnable);
  }

  @Test
  void itShouldNotSaveEdge_WhenNodeToDoesNotExist() {
    var existingNodeFrom = new Node("Test_node_type");
    getNodeRepository().save(existingNodeFrom);
    var expectedEdge = new Edge(
        new Node("Test_node_type"),
        "test_edge_type",
        existingNodeFrom
    );

    Executable runnable = () -> getEdgeRepository().save(expectedEdge);
    Assertions.assertThrows(OneOrBothNodesOnEdgeDoesNotExist.class, runnable);
  }

  @Test
  void itShouldSaveEdgeWithoutAttributes() throws OneOrBothNodesOnEdgeDoesNotExist {
    var nodeFrom = new Node("Test_node_type");
    var nodeTo = new Node("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var expectedEdge = new Edge(
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
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new Edge(
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
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new Edge(
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
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);
    var alreadySavedEdge = new Edge(
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
    var nodeFrom = new Node("Irrelevant_type");
    var nodeTo = new Node("Irrelevant_type");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedNode = new Edge(
        nodeFrom,
        "irrelevant_type",
        nodeTo
    );
    getEdgeRepository().save(alreadySavedNode);

    var newEdge = new Edge(
        alreadySavedNode.getId(),
        nodeFrom,
        "irrelevant_type",
        nodeTo
    );
    Executable executable = () -> getEdgeRepository().save(newEdge);

    Assertions.assertThrows(EdgeWithSameIdAndTypeAlreadyExists.class, executable);
  }

  @Test
  void itShouldSaveEdgeWithVariousAttributes() throws OneOrBothNodesOnEdgeDoesNotExist {
    var nodeFrom = new Node("Test_node_type");
    var nodeTo = new Node("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var expectedEdge = new Edge(
        nodeFrom, "test_edge_type",
        nodeTo
    );
    expectedEdge = expectedEdge.add(
        new LeafAttribute<>("test_attribute_name", new StringAttributeValue("testValueA"
        ))
    );
    expectedEdge = expectedEdge.add(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true
        ))
    );
    expectedEdge = expectedEdge.add(
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d
        ))
    );
    expectedEdge = expectedEdge.add(
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15))
    );
    expectedEdge = expectedEdge.add(
        new LeafAttribute<>(
            "test_timestamp",
            new InstantAttributeValue(Instant.now())
        )
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
    var node1 = new Node("Irrelevant");
    var node2 = new Node("Irrelevant");

    var edgeTypeA1 = new Edge(
        node1,
        "type_A",
        node2
    );
    var edgeTypeA2 = new Edge(
        node2,
        "type_A",
        node1
    );
    var edgeTypeB1 = new Edge(
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
    var examinationNode = new Node("Examination");
    var atlasNode = new Node("Lab_atlas");
    var rangeNode = new Node("Examination_range");
    getNodeRepository().save(examinationNode);
    getNodeRepository().save(atlasNode);
    getNodeRepository().save(rangeNode);

    var foundEdge1 = new Edge(
        examinationNode,
        "has",
        rangeNode
    );
    var foundEdge2 = new Edge(
        atlasNode,
        "contains",
        examinationNode
    );
    var notFoundEdge3 = new Edge(
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
    var nodeFrom = new Node("Test_node_type");
    var nodeTo = new Node("Test_node_type2");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var notMatchedEdge = new Edge(
        nodeFrom,
        "not_matched_edge",
        nodeTo
    );

    getEdgeRepository().save(notMatchedEdge);

    var matchedEdge = new Edge(
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
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new Edge(
        nodeFrom,
        "same",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var replacingEdge = new Edge(
        alreadySavedEdge.getId(),
        nodeFrom,
        "same",
        nodeTo
    );
    replacingEdge = replacingEdge.add(
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
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new Edge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));

    getEdgeRepository().save(alreadySavedEdge);
    //When
    getEdgeRepository().removeEdge(alreadySavedEdge.getId(), alreadySavedEdge.getType());
    //Then
    Edge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> getEdgeRepository().loadEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }

  @Test
  void itShouldRemoveEdgeForRemoval() {
    var nodeFrom = new Node("Node_from");
    var nodeTo = new Node("Node_to");
    getNodeRepository().save(nodeFrom);
    getNodeRepository().save(nodeTo);

    var alreadySavedEdge = new Edge(
        nodeFrom,
        "test_edge",
        nodeTo
    );
    alreadySavedEdge = alreadySavedEdge.add(
        new LeafAttribute<>("name", new StringAttributeValue("name")));
    var edgeForRemoval = new EdgeForRemoval(alreadySavedEdge.getId(), alreadySavedEdge.getType());

    getEdgeRepository().save(alreadySavedEdge);
    //When
    getEdgeRepository().removeEdge(edgeForRemoval);
    //Then
    Edge finalAlreadySavedEdge = alreadySavedEdge;
    Executable executable = () -> getEdgeRepository().loadEdge(
        finalAlreadySavedEdge.getId(),
        finalAlreadySavedEdge.getType()
    );
    Assertions.assertThrows(EdgeNotFound.class, executable);
  }
}
