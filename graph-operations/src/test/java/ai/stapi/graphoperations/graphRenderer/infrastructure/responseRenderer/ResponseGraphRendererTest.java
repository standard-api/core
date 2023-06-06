package ai.stapi.graphoperations.graphRenderer.infrastructure.responseRenderer;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.ResponseGraphRenderer;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ResponseGraphRendererTest extends IntegrationTestCase {

  @Autowired
  private ResponseGraphRenderer responseGraphRenderer;

  @Test
  void itShouldRenderEmptyGraph() {
    var actual = this.responseGraphRenderer.render(new InMemoryGraphRepository());
    this.thenObjectApproved(actual, getOptions());
  }

  @Test
  void itShouldRenderGraphWithNode() {
    var graph = new InMemoryGraphRepository(
        new Node(UniversallyUniqueIdentifier.randomUUID(), "example_node_type")
    );
    var actual = this.responseGraphRenderer.render(graph);
    this.thenObjectApproved(actual, getOptions());
  }

  @Test
  void itShouldRenderGraphWithNodeWithAttribute() {
    var graph = new InMemoryGraphRepository(
        new Node(
            UniversallyUniqueIdentifier.randomUUID(),
            "example_node_type",
            new LeafAttribute<>("exampleAttributeName",
                new StringAttributeValue("exampleAttributeValue"))
        )
    );
    var actual = this.responseGraphRenderer.render(graph);
    this.thenObjectApproved(actual, getOptions());
  }

  @Test
  void itShouldRenderGraphWithEdge() {
    var nodeFrom = new Node(
        UniversallyUniqueIdentifier.fromString("c023fdc9-4045-4642-9f02-f301833ac17c"),
        "example_node_from_type");
    var nodeTo = new Node(
        UniversallyUniqueIdentifier.fromString("0fe04b45-8275-4d30-ae2f-2fc24cef530a"),
        "example_node_to_type");
    var graph = new InMemoryGraphRepository(
        nodeFrom,
        nodeTo,
        new Edge(
            nodeFrom,
            "example_edge_type",
            nodeTo,
            new LeafAttribute<>("exampleAttributeName",
                new StringAttributeValue("exampleAttributeValue"))
        )
    );
    var actual = this.responseGraphRenderer.render(graph);
    this.thenObjectApproved(actual, getOptions());
  }

  private ObjectToJSonStringOptions getOptions() {
    return new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT,
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS
    );
  }
}