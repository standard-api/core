package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.exceptions.GraphLoaderException;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class InMemoryGraphLoaderTest extends SchemaIntegrationTestCase {
  
  @Autowired
  private InMemoryGenericSearchOptionResolver searchOptionResolver;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private StructureSchemaFinder structureSchemaFinder;

  @Test
  void FindMethod_ShouldReturnEmptyListWhenGraphIsEmpty_WhenSearchingNode() {
    var graphLoader = this.initializeGraphLoader(this.getEmptyGraph());
    var nodeDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("any_node")
    );
    var result = graphLoader.findAsTraversable(nodeDescription);
    Assertions.assertEquals(result.size(), 0);
  }

  @Test
  void FindMethod_ShouldReturnEmptyListWhenGraphIsEmpty_WhenSearchingOutgoingEdge() {
    var graphLoader = this.initializeGraphLoader(this.getEmptyGraph());
    var edgeDescription = new OutgoingEdgeQueryDescription(
        new EdgeDescriptionParameters("any_edge")
    );
    var result = graphLoader.findAsTraversable(edgeDescription);
    Assertions.assertEquals(result.size(), 0);
  }

  @Test
  void FindMethod_ShouldReturnEmptyListWhenGraphIsEmpty_WhenSearchingIngoingEdge() {
    var graphLoader = this.initializeGraphLoader(this.getEmptyGraph());
    var edgeDescription = new IngoingEdgeQueryDescription(
        new EdgeDescriptionParameters("any_edge")
    );
    var result = graphLoader.findAsTraversable(edgeDescription);
    Assertions.assertEquals(result.size(), 0);
  }

  @Test
  void FindMethod_shouldReturnNodesSpecifiedByFirstNodeQueryDescription() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node1")
    );
    var result = graphLoader.findAsTraversable(nodeQueryDescription);
    this.thenGraphElementsApproved(result);
  }

  @Test
  void FindMethod_shouldReturnNodesWithAttributesAsSpecified() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node1"),
        new SearchQueryParameters(),
        new AttributeQueryDescription("example_attribute")
    );
    var result = graphLoader.findAsTraversable(nodeQueryDescription);
    this.thenGraphElementsApproved(result);
  }

  @Test
  void FindMethod_shouldReturnNodesEvenThoughSpecifiedAttributeNotPresent() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node1"),
        new SearchQueryParameters(),
        new AttributeQueryDescription("not_existing_attribute")
    );
    var result = graphLoader.findAsTraversable(nodeQueryDescription);
    this.thenGraphElementsApproved(result);
  }

  @Test
  void FindMethod_shouldReturnNodesWithSpecifiedAttributesEvenThoughSomeAreMissing() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node1"),
        new SearchQueryParameters(),
        new AttributeQueryDescription("example_attribute"),
        new AttributeQueryDescription("other_attribute")
    );
    var result = graphLoader.findAsTraversable(nodeQueryDescription);
    this.thenGraphElementsApproved(result);
  }

  @Test
  void FindMethod_shouldReturnNodesWithSubgraphAsSpecified() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node2"),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("edge2"),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters("node3"),
                new AttributeQueryDescription("example_attribute")
            )
        )
    );
    var result = graphLoader.find(
        nodeQueryDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH
    ).getGraphLoaderFindAsGraphOutput();

    this.thenGraphApproved(result.getGraph());

    Assertions.assertEquals(result.getFoundGraphElementIds().size(), 1);
    Assertions.assertEquals(
        result.getFoundGraphElementIds().get(0),
        UniversallyUniqueIdentifier.fromString("2378d6d6-4424-45f7-8f2e-b21bf5e8de66")
    );
  }

  @Test
  void GetMethod_shouldThrowErrorWhenNodeDoesNotExistWithSpecifiedUUID() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node2")
    );

    Executable executable = () -> graphLoader.getAsTraversable(
        UniversallyUniqueIdentifier.fromString("0762fc65-7c9f-4578-944a-8d89f003d03d"),
        nodeQueryDescription
    );
    this.thenExceptionMessageApproved(GraphLoaderException.class, executable);
  }

  @Test
  void GetMethod_shouldThrowErrorWhenEdgeDoesNotExistWithSpecifiedUUID() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new OutgoingEdgeQueryDescription(
        new EdgeDescriptionParameters("nonexisitng_edge")
    );

    Executable executable = () -> graphLoader.getAsTraversable(
        UniversallyUniqueIdentifier.fromString("0762fc65-7c9f-4578-944a-8d89f003d03d"),
        nodeQueryDescription
    );
    Assertions.assertThrows(GraphLoaderException.class, executable);
  }

  @Test
  void GetMethod_shouldReturnSubgraphAsSpecified() {
    var graphLoader = this.initializeGraphLoader(this.getSimpleGraph());
    var nodeQueryDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("node1"),
        new AttributeQueryDescription("example_attribute"),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("edge1"),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters("node2"),
                new AttributeQueryDescription("example_attribute"),
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters("edge2"),
                    new NodeQueryGraphDescription(
                        new NodeDescriptionParameters("node3"),
                        new AttributeQueryDescription("example_attribute")
                    )
                )
            )
        )
    );
    var result = graphLoader.get(
        UniversallyUniqueIdentifier.fromString("fca128a0-637a-4d6f-bd86-d2e42fe9e38d"),
        nodeQueryDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH
    ).getGraph();
    this.thenGraphApproved(result);
  }

  private Graph getSimpleGraph() {
    var graph = new Graph();
    var node1 = new Node(
        UniversallyUniqueIdentifier.fromString("fca128a0-637a-4d6f-bd86-d2e42fe9e38d"), "node1");
    node1 = node1.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("node1_value")));
    node1 = node1.add(
        new LeafAttribute<>("other_attribute", new StringAttributeValue("node1_other_value")));
    var node11 = new Node(
        UniversallyUniqueIdentifier.fromString("376a6385-60bb-4cbb-97f2-6b8f5349fb8e"), "node1");
    node11 = node11.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("node11_value")));
    var node12 = new Node(
        UniversallyUniqueIdentifier.fromString("b0c2711f-aa50-4227-8c83-353749c872de"), "node1");
    node12 = node12.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("node12_value")));
    var node2 = new Node(
        UniversallyUniqueIdentifier.fromString("2378d6d6-4424-45f7-8f2e-b21bf5e8de66"), "node2");
    node2 = node2.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("node2_value")));
    var node3 = new Node(
        UniversallyUniqueIdentifier.fromString("25ed70b7-c53f-4d5a-b279-2595ad2c1f4c"), "node3");
    node3 = node3.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("node3_value")));
    var edge1 = new Edge(
        UniversallyUniqueIdentifier.fromString("b18de82b-7c69-46e8-b18b-f45622df3459"), node1,
        "edge1", node2);
    edge1 = edge1.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("edge1_value")));
    var edge2 = new Edge(
        UniversallyUniqueIdentifier.fromString("d3cba05a-7918-4ebc-8bd1-137dee05a0c5"), node2,
        "edge2", node3);
    edge2 = edge2.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("edge2_value")));
    var edge3 = new Edge(
        UniversallyUniqueIdentifier.fromString("42e380ce-9794-49f0-9380-c2ff275b9bfa"), node3,
        "edge3", node1);
    edge3 = edge3.add(
        new LeafAttribute<>("example_attribute", new StringAttributeValue("edge3_value")));
    return graph.withAll(node1, node11, node12, node2, node3, edge1, edge2, edge3);
  }

  private Graph getEmptyGraph() {
    return new Graph();
  }

  private InMemoryGraphLoader initializeGraphLoader(Graph graph) {
    return new InMemoryGraphLoader(
        graph, 
        this.searchOptionResolver, 
        this.structureSchemaFinder, 
        this.objectMapper
    );
  }
}
