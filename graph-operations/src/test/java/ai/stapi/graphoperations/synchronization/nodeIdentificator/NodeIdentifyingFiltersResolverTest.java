package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.testImplementation.TestNodeIdentificatorsProvider;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NodeIdentifyingFiltersResolverTest extends IntegrationTestCase {
    
    @Autowired
    private NodeIdentifyingFiltersResolver nodeIdentifyingFiltersResolver;
    
    @Autowired
    private TestNodeIdentificatorsProvider testNodeIdentificatorsProvider;
    
    @BeforeEach
    public void beforeEach() {
        this.testNodeIdentificatorsProvider.clear();
    }

    @Test
    void itShouldNotBeAbleToUseIdentificatorWhenAttributeIsMissingOnNode() {
        var node = new Node("test_node_type");
        this.testNodeIdentificatorsProvider.add(
            node.getType(),
            new NodeIdentificator("missing_attribute")
        );
        var graph = new Graph(node);
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph.traversable()
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseIdentificatorWhenAttributeIsPresent() {
        var node = new Node("test_node_type");
        var identifyingAttributeName = "identifying_attribute";
        node = node.add(
            new LeafAttribute<>(
                identifyingAttributeName, 
                new StringAttributeValue("matching")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            node.getType(),
            new NodeIdentificator(identifyingAttributeName)
        );
        var graph = new Graph(node);
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph.traversable()
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldNotBeAbleToUseMultipleIdentificatorWhenBothAttributesAreMissingOnNode() {
        var node = new Node("test_node_type");
        this.testNodeIdentificatorsProvider.add(
            node.getType(),
            new NodeIdentificator("missingAttribute", "anotherMissingAttribute")
        );
        var graph = new Graph(node).traversable();
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldNotBeAbleToUseMultipleIdentificatorWhenOneOfTwoAttributesIsPresent() {
        var node = new Node("test_node_type");
        var identifyingAttributeName = "identifying_attribute";
        node = node.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("matching")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            node.getType(),
            new NodeIdentificator("missingAttribute", identifyingAttributeName)
        );
        var graph = new Graph(node).traversable();
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseMultipleIdentificatorWhenAttributeIsPresent() {
        var node = new Node("test_node_type");
        var identifyingAttributeName = "identifying_attribute";
        var anotherIdentifyingAttributeName = "another_identifying_attribute";
        node = node.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("matching")
            )
        );
        node = node.add(
            new LeafAttribute<>(
                anotherIdentifyingAttributeName,
                new StringAttributeValue("value")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            node.getType(),
            new NodeIdentificator(anotherIdentifyingAttributeName, identifyingAttributeName)
        );
        var graph = new Graph(node).traversable();
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseOnlyValidIdentificatorsWhenNodeHasMultipleIdentificators() {
        var node = new Node("test_node_type");
        var identifyingAttributeName = "identifying_attribute";
        var anotherIdentifyingAttributeName = "another_identifying_attribute";
        var singleIdentificatorAttributeName = "single_identifying_attribute";
        node = node.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("irrelevant")
            )
        );
        node = node.add(
            new LeafAttribute<>(
                singleIdentificatorAttributeName,
                new StringAttributeValue("singleAttributeIdentificatorIrrelevantValue")
            )
        );
        this.testNodeIdentificatorsProvider.addAll(
            node.getType(),
            new NodeIdentificator(anotherIdentifyingAttributeName, identifyingAttributeName),
            new NodeIdentificator(singleIdentificatorAttributeName)
        );
        var graph = new Graph(node).traversable();
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            node,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldNotBeAbleToUseIdentificatorThroughRelationWhenItsNotUsable() {
        var identifiedNode = new Node("IdentifiedNode");
        var identifyingNode = new Node("IdentifyingNode");
        var edge = new Edge(identifiedNode, "edge", identifyingNode);
        identifyingNode = identifyingNode.add(
            new LeafAttribute<>(
                "another_attribute",
                new StringAttributeValue("irrelevant")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            identifiedNode.getType(),
            new NodeIdentificator(
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters(edge.getType()),
                    new NodeDescription(
                        new NodeDescriptionParameters(identifyingNode.getType()),
                        new AttributeQueryDescription("missing_attribute")
                    )
                )
            )
        );
        var graph = new Graph(
            identifiedNode,
            identifyingNode,
            edge
        ).traversable();

        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            identifiedNode,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseIdentificatorThroughRelation() {
        var identifiedNode = new Node("IdentifiedNode");
        var identifyingNode = new Node("IdentifyingNode");
        var edge = new Edge(identifiedNode, "edge", identifyingNode);
        var identifyingAttributeName = "identifying_attribute";
        identifyingNode = identifyingNode.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("irrelevant")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            identifiedNode.getType(),
            new NodeIdentificator(
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters(edge.getType()),
                    new NodeDescription(
                        new NodeDescriptionParameters(identifyingNode.getType()),
                        new AttributeQueryDescription(identifyingAttributeName)
                    )
                )
            )
        );
        var graph = new Graph(
            identifiedNode,
            identifyingNode,
            edge
        ).traversable();
        
        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            identifiedNode,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseIdentificatorThroughRelationWhenThereAreMoreRelations() {
        var identifiedNode = new Node("IdentifiedNode");
        var identifyingNode = new Node("IdentifyingNode");
        var anotherIdentifyingNode = new Node("IdentifyingNode");
        var edge = new Edge(identifiedNode, "edge", identifyingNode);
        var anotherEdge = new Edge(identifiedNode, "edge", anotherIdentifyingNode);
        var identifyingAttributeName = "identifying_attribute";
        identifyingNode = identifyingNode.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("any")
            )
        );
        anotherIdentifyingNode = anotherIdentifyingNode.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("any")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            identifiedNode.getType(),
            new NodeIdentificator(
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters(edge.getType()),
                    new NodeDescription(
                        new NodeDescriptionParameters(identifyingNode.getType()),
                        new AttributeQueryDescription(identifyingAttributeName)
                    )
                )
            )
        );
        var graph = new Graph(
            identifiedNode,
            identifyingNode,
            anotherIdentifyingNode,
            edge,
            anotherEdge
        ).traversable();

        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            identifiedNode,
            graph
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldBeAbleToUseIdentificatorThroughRelationWhenThereAreMoreRelationsButNotAllAreUsable() {
        var identifiedNode = new Node("IdentifiedNode");
        var identifyingNode = new Node("IdentifyingNode");
        var unusableIdentifyingNode = new Node("IdentifyingNode");
        var edge = new Edge(identifiedNode, "edge", identifyingNode);
        var anotherEdge = new Edge(identifiedNode, "edge", unusableIdentifyingNode);
        var identifyingAttributeName = "identifying_attribute";
        identifyingNode = identifyingNode.add(
            new LeafAttribute<>(
                identifyingAttributeName,
                new StringAttributeValue("irrelevantFirstIdentifyingNodeValue")
            )
        );
        this.testNodeIdentificatorsProvider.add(
            identifiedNode.getType(),
            new NodeIdentificator(
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters(edge.getType()),
                    new NodeDescription(
                        new NodeDescriptionParameters(identifyingNode.getType()),
                        new AttributeQueryDescription(identifyingAttributeName)
                    )
                )
            )
        );
        var graph = new Graph(
            identifiedNode,
            identifyingNode,
            unusableIdentifyingNode,
            edge,
            anotherEdge
        ).traversable();

        var actual = this.nodeIdentifyingFiltersResolver.resolve(
            identifiedNode,
            graph
        );
        this.thenObjectApproved(actual);
    }
}
