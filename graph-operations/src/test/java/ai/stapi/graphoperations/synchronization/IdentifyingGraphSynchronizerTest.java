package ai.stapi.graphoperations.synchronization;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.Graph;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.exceptions.EdgeNotFound;
import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.repositorypruner.RepositoryPruner;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificator;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.testImplementation.TestNodeIdentificatorsProvider;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({
    IdentifyingGraphSynchronizerTestStructureLoader.SCOPE,
    TestSystemModelDefinitionsLoader.SCOPE
})
class IdentifyingGraphSynchronizerTest extends SchemaIntegrationTestCase {

    @Autowired
    protected RepositoryPruner repositoryPruner;
    
    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private IdentifyingGraphSynchronizer identifyingGraphSynchronizer;
    
    @Autowired
    private TestNodeIdentificatorsProvider testNodeIdentificatorsProvider;

    @BeforeEach
    public void setUp() {
        this.repositoryPruner.prune();
    }

    @Test
    void itShouldSynchronizeNodeToEmptyGraph() throws GraphException {
        var expectedNode = new Node("test_node_type");

        var graph = new Graph(expectedNode);

        this.identifyingGraphSynchronizer.synchronize(graph);

        var actualNode = this.nodeRepository.loadNode(
            expectedNode.getId(),
            expectedNode.getType()
        );

        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldSynchronizeNodeById() throws GraphException {
        var existingNode = new Node("Test_node_type");
        this.nodeRepository.save(existingNode);

        var expectedNode = new Node(
            existingNode.getId(),
            "Test_node_type"
        );
        expectedNode = expectedNode.add(
            new LeafAttribute<>("string_type", new StringAttributeValue("test value"
            ))
        );

        this.identifyingGraphSynchronizer.synchronize(new Graph(expectedNode));

        var actualNode = this.nodeRepository.loadNode(
            expectedNode.getId(),
            expectedNode.getType()
        );

        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldNotSynchronizeNodeByIdAndDifferentType() throws GraphException {
        var existingNode = new Node("OriginalType");
        this.nodeRepository.save(existingNode);

        var expectedNode = new Node(
            existingNode.getId(),
            "DifferentType"
        );
        expectedNode = expectedNode.add(
            new LeafAttribute<>(
                "string_type", 
                new StringAttributeValue("test value")
            )
        );

        this.identifyingGraphSynchronizer.synchronize(new Graph(expectedNode));

        var actualNode = this.nodeRepository.loadNode(
            expectedNode.getId(),
            expectedNode.getType()
        );
        
        Assertions.assertTrue(this.nodeRepository.nodeExists(existingNode.getId(), existingNode.getType()));
        this.thenNodeApproved(actualNode);
    }


    @Test
    void itShouldNotFixEdgeWhenTryingToSynchronizeNodeWithSameAttributeButDifferentType() throws GraphException {
        var existingNode = new Node("SameType");
        var otherNode = new Node("OtherType");
        otherNode = otherNode.add(
            new LeafAttribute<>(
                "identifying_attribute",
                new StringAttributeValue("matching_value")
            )
        ).add(
            new LeafAttribute<>(
                "original_attribute",
                new StringAttributeValue("original_value")
            )
        );
        var anotherNode = new Node("AnotherType");
        var existingEdge = new Edge(existingNode, "originalEdge", otherNode);
        this.nodeRepository.save(existingNode);
        this.nodeRepository.save(otherNode);
        this.nodeRepository.save(anotherNode);
        this.edgeRepository.save(existingEdge);
        
        var mergingNode1 = new Node(
            existingNode.getId(),
            "SameType"
        );
        mergingNode1 = mergingNode1.add(
            new LeafAttribute<>(
                "string_type",
                new StringAttributeValue("test value")
            )
        );

        this.testNodeIdentificatorsProvider.add(
            otherNode.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var mergingNode2 = new Node(anotherNode.getId(), "OtherType");
        mergingNode2 = mergingNode2.add(
            new LeafAttribute<>(
                "identifying_attribute",
                new StringAttributeValue("matching_value")
            )
        ).add(
            new LeafAttribute<>(
                "new_attribute",
                new StringAttributeValue("irrelevant_value")
            )
        );
        var newEdge = new Edge(mergingNode1, "originalEdge", mergingNode2);
        var someEdge = new Edge(mergingNode2, "originalEdge", anotherNode);
        var graph = new Graph(mergingNode1, mergingNode2, anotherNode, newEdge, someEdge);

        this.identifyingGraphSynchronizer.synchronize(graph);
        Assertions.assertTrue(this.nodeRepository.nodeExists(existingNode.getId(), existingNode.getType()));
        Assertions.assertTrue(this.nodeRepository.nodeExists(otherNode.getId(), otherNode.getType()));
        Assertions.assertTrue(this.nodeRepository.nodeExists(anotherNode.getId(), anotherNode.getType()));
        Assertions.assertTrue(this.edgeRepository.edgeExists(existingEdge.getId(), existingEdge.getType()));
        Assertions.assertFalse(this.nodeRepository.nodeExists(mergingNode2.getId(), mergingNode2.getType()));
    }

    @Test
    void itShouldSynchronizeNodeByIdAndMergeAttributes() throws GraphException {
        var existingNode = new Node("Test_node_type");
        var existingAttribute = new LeafAttribute<>("string_type", new StringAttributeValue("test value"
        ));
        existingNode = existingNode.add(
            existingAttribute
        );
        this.nodeRepository.save(existingNode);

        var synchronizedNode = new Node(
            existingNode.getId(),
            "Test_node_type"
        );
        var synchronizedAttribute =
            new LeafAttribute<>("string_type2", new StringAttributeValue("test value2"
            ));
        synchronizedNode = synchronizedNode.add(
            synchronizedAttribute
        );

        this.identifyingGraphSynchronizer.synchronize(new Graph(synchronizedNode));

        var actualNode = this.nodeRepository.loadNode(
            synchronizedNode.getId(),
            synchronizedNode.getType()
        );
        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldSynchronizeNodeByIdAndMergeAttributesWithOverrideExisting(
    ) throws GraphException {
        var existingNode = new Node("Test_node_type");
        existingNode = existingNode.add(
            new LeafAttribute<>(
                "string_type",
                new StringAttributeValue("test value"))
        );
        this.nodeRepository.save(existingNode);

        var updatingNode = new Node(
            existingNode.getId(),
            "Test_node_type"
        );
        updatingNode = updatingNode.add(
            new LeafAttribute<>(
                "string_type",
                new StringAttributeValue("test value_overwritten"))
        );
        updatingNode = updatingNode.add(
            new LeafAttribute<>(
                "string_type2",
                new StringAttributeValue("test value2"))
        );

        this.identifyingGraphSynchronizer.synchronize(new Graph(updatingNode));

        var actualNode = this.nodeRepository.loadNode(
            existingNode.getId(),
            existingNode.getType()
        );

        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldSynchronizeNodeByAttribute() throws GraphException {
        var updatingNodeType = "Test_node_type";
        var existingNode = new Node(updatingNodeType);
        existingNode = existingNode.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("identified value"
            ))
        );
        this.nodeRepository.save(existingNode);

        var updatingNode = new Node(updatingNodeType);
        
        this.testNodeIdentificatorsProvider.add(
            updatingNodeType, 
            new NodeIdentificator("identifying_attribute")
        );
        updatingNode = updatingNode.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("identified value"
            ))
        );
        updatingNode = updatingNode.add(
            new LeafAttribute<>("some_other_attribute",
                new StringAttributeValue("new synchronized value"
                ))
        );
        var graph = new Graph(updatingNode);
        this.identifyingGraphSynchronizer.synchronize(graph);
        var actualNode = this.nodeRepository.loadNode(
            existingNode.getId(),
            existingNode.getType()
        );
        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldNotSynchronizeNodeByDifferentAttribute() throws GraphException {
        var notMergedNodeType = "Test_node_type";
        var existingNode = new Node(notMergedNodeType);
        existingNode = existingNode.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("identified value"
            ))
        );
        this.nodeRepository.save(existingNode);

        var notMergedNode = new Node(notMergedNodeType);
        this.testNodeIdentificatorsProvider.add(
            notMergedNodeType,
            new NodeIdentificator("identifying_attribute")
        );
        notMergedNode = notMergedNode.add(
            new LeafAttribute<>("not_identifying_attribute", new StringAttributeValue("test value"
            ))
        );

        this.identifyingGraphSynchronizer.synchronize(new Graph(notMergedNode));

        var actualNode = this.nodeRepository.loadNode(
            notMergedNode.getId(),
            notMergedNode.getType()
        );
        this.thenNodeApproved(actualNode);
    }

    @Test
    void itShouldSynchronizeEdgeWithItsNodesToEmptyGraph() throws GraphException {
        var nodeA = new Node("glass");
        var nodeB = new Node("water");

        var expectedEdge = new Edge(
            nodeA,
            "contains",
            nodeB
        );

        this.whenSynchronize(
            nodeA,
            nodeB,
            expectedEdge
        );

        thenEdgeIsLoadedAndApproved(expectedEdge);
    }


    @Test
    void itShouldSynchronizeEdgeWhenItAlreadyExistWithoutDuplication() throws GraphException {
        var nodeAId = UniversallyUniqueIdentifier.fromString("10ee31f2-1017-4c71-b4fb-44b52923e6f8");
        var nodeBId = UniversallyUniqueIdentifier.fromString("f7a63c6b-b51d-478d-913f-7ca2bccfcf61");

        var nodeA = new Node(
            nodeAId,
            "Glass"
        );
        var nodeB = new Node(
            nodeBId,
            "Water"
        );
        var synchronizedEdge = new Edge(
            nodeA,
            "contains",
            nodeB
        );

        this.givenSynchronized(
            nodeA,
            nodeB,
            synchronizedEdge
        );

        var sameEdgeWithAttribute = new Edge(
            nodeA,
            "contains",
            nodeB
        );
        sameEdgeWithAttribute = sameEdgeWithAttribute.add(
            new LeafAttribute<>("new", new StringAttributeValue("value")));

        this.whenSynchronize(
            nodeA,
            nodeB,
            sameEdgeWithAttribute
        );
        this.thenEdgeIsLoadedAndApproved(synchronizedEdge);
        this.thenEdgeDoesNotExists(sameEdgeWithAttribute);
    }

    @Test
    void itShouldSynchronizeEdgesWhenSynchronizingNodeByAttribute() throws GraphException {
        var existingNodeId =
            UniversallyUniqueIdentifier.fromString("10ee31f2-1017-4c71-b4fb-44b52923e6f8");

        var existingNode = new Node(
            existingNodeId,
            "node_from_type"
        );
        existingNode = existingNode.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("666"))
        );
        this.givenSynchronized(
            existingNode
        );

        var nodeToBeMerged = new Node("node_from_type");
        nodeToBeMerged = nodeToBeMerged.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("666"
            ))
        );
        this.testNodeIdentificatorsProvider.add(
            nodeToBeMerged.getType(),
            new NodeIdentificator("identifying_attribute")
        );
        var nodeTo = new Node(
            UniversallyUniqueIdentifier.fromString("aceab77c-92c5-4f59-95bc-0210af2edc76"),
            "node_to_type"
        );
        var synchronizedEdge = new Edge(
            UniversallyUniqueIdentifier.fromString("9d4f37cd-6b20-4a9c-b65c-39406c36bf1c"),
            nodeToBeMerged,
            "contains",
            nodeTo
        );
        this.whenSynchronize(
            nodeToBeMerged,
            nodeTo,
            synchronizedEdge
        );
        var expectedEdge = new Edge(
            synchronizedEdge.getId(),
            existingNode,
            "contains",
            nodeTo
        );
        var actualEdge = this.edgeRepository.loadEdge(
            synchronizedEdge.getId(),
            "contains"
        );
        this.thenNodeWithGivenNodeIdDoesNotExist(nodeToBeMerged);
        this.thenEdgesHaveSameIdAndTypeAndNodeIds(
            expectedEdge,
            actualEdge
        );
    }

    @Test
    void itShouldNotMergeDuplicateNodesByIdentificators_WhenTypesDiffer() {
        var nodeA = new Node("typeA");
        this.testNodeIdentificatorsProvider.add(
            nodeA.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var nodeB = new Node("typeB");
        this.testNodeIdentificatorsProvider.add(
            nodeB.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        nodeA = nodeA.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("same_value")));
        nodeA =
            nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
        nodeB = nodeB.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("same_value")));
        nodeB =
            nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));

        var graph = new Graph(
            nodeA,
            nodeB
        );

        var actualGraph = whenMergeDuplicates(graph);

        this.thenGraphApproved(
            actualGraph
        );
    }

    @Test
    void itShouldMergeDuplicateNodesByIdentificators() {
        var nodeA = new Node("type");

        var nodeB = new Node("type");

        var nodeC = new Node("type");
        this.testNodeIdentificatorsProvider.add(
            nodeC.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        nodeA = nodeA.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched"))
        );
        nodeA = nodeA.add(
            new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value"))
        );
        nodeB = nodeB.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched"))
        );
        nodeB = nodeB.add(
            new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value"))
        );
        nodeC = nodeC.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("not_matched"))
        );

        var graph = new Graph(
            nodeA,
            nodeC,
            nodeB
        );

        var actualGraph = whenMergeDuplicates(graph);

        thenGraphApproved(actualGraph);
    }

    @Test
    void itShouldMergeDuplicateNodesByIdentificators_AndItWillFixEdges() {
        var nodeA = new Node("type");
        this.testNodeIdentificatorsProvider.add(
            nodeA.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var nodeB = new Node("type");
        this.testNodeIdentificatorsProvider.add(
            nodeB.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var nodeC = new Node("type_C");
        var edge = new Edge(
            nodeB,
            "connects",
            nodeC
        );

        nodeA = nodeA.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched")));
        nodeA =
            nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
        nodeB = nodeB.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched")));
        nodeB =
            nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));

        var graph = new Graph(
            nodeA,
            nodeB,
            nodeC,
            edge
        );

        var actualGraph = whenMergeDuplicates(graph);

        this.thenGraphApproved(actualGraph);
    }

    @Test
    void itShouldMergeDuplicateNodesByIdentificators_AndItWillMergeEdges() {
        var nodeA = new Node("type");
        this.testNodeIdentificatorsProvider.add(
            nodeA.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var nodeB = new Node("type");
        this.testNodeIdentificatorsProvider.add(
            nodeB.getType(),
            new NodeIdentificator("identifying_attribute")
        );

        var nodeC = new Node("type_C");
        var e1 = new Edge(
            nodeA,
            "connects",
            nodeC
        );
        e1 = e1.add(
            new LeafAttribute<>("A_edge_attribute", new StringAttributeValue("A_edge_value")));
        var e2 = new Edge(
            nodeB,
            "connects",
            nodeC
        );
        e2 = e2.add(
            new LeafAttribute<>("B_edge_attribute", new StringAttributeValue("B_edge_value")));

        nodeA = nodeA.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched")));
        nodeA =
            nodeA.add(new LeafAttribute<>("A_attribute", new StringAttributeValue("A_value")));
        nodeB = nodeB.add(
            new LeafAttribute<>("identifying_attribute", new StringAttributeValue("matched")));
        nodeB =
            nodeB.add(new LeafAttribute<>("B_attribute", new StringAttributeValue("B_value")));

        var graph = new Graph(
            nodeA,
            nodeB,
            nodeC,
            e1,
            e2
        );

        var actualGraph = whenMergeDuplicates(graph);

        this.thenGraphApproved(actualGraph);
    }
    
    @NotNull
    private Graph whenMergeDuplicates(Graph graph) {
        var actualGraph = graph.traversable();
        return this.identifyingGraphSynchronizer
            .mergeDuplicateNodesByIdentificators(actualGraph)
            .getGraph();
    }

    private void givenSynchronized(AttributeContainer... attributeContainers) {
        this.synchronize(attributeContainers);
    }

    private void whenSynchronize(AttributeContainer... attributeContainers) {
        this.synchronize(attributeContainers);
    }

    private void thenEdgeIsLoadedAndApproved(Edge expectedEdge) {
        var actualEdge = this.edgeRepository.loadEdge(
            expectedEdge.getId(),
            expectedEdge.getType()
        );

        this.thenEdgeApproved(actualEdge);
    }

    private void thenEdgeDoesNotExists(Edge edge) {
        var id = edge.getId();
        var type = edge.getType();
        assertThrows(
            EdgeNotFound.class,
            () -> this.edgeRepository.loadEdge(id, type)
        );
    }

    private void synchronize(AttributeContainer[] attributeContainers) {
        var graph = new Graph(attributeContainers);
        this.identifyingGraphSynchronizer.synchronize(graph);
    }

    protected void thenNodeWithGivenNodeIdDoesNotExist(Node nodeToBeMerged) {
        var exists = this.nodeRepository.nodeExists(
            nodeToBeMerged.getId(),
            nodeToBeMerged.getType()
        );
        Assertions.assertFalse(
            exists,
            String.format(
                "Node with id %s and type \"%s\" was expected to not exist, but it actuallu exists.",
                nodeToBeMerged.getId(),
                nodeToBeMerged.getType()
            )
        );
    }
}
