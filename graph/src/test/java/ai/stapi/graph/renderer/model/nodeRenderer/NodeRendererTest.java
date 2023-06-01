package ai.stapi.graph.renderer.model.nodeRenderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.ApiRendererOptions;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.NodeResponse;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderOutput;
import ai.stapi.graph.renderer.infrastructure.textRenderer.TextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.textRenderer.node.TextNodeRenderOutput;
import ai.stapi.graph.test.integration.IntegrationTestCase;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NodeRendererTest extends IntegrationTestCase {

  @Autowired
  protected GenericNodeRenderer genericNodeRenderer;

  @Test
  void itShouldReturnTextRenderedNode() {
    var testNode = getTestNode();
    var nodeRender = this.genericNodeRenderer.render(
        testNode,
        new TextRendererOptions()
    );
    assertEquals(
        TextNodeRenderOutput.class,
        nodeRender.getClass()
    );
    var textNodeRender = (TextNodeRenderOutput) nodeRender;
    Approvals.verify(textNodeRender.toPrintableString());
  }


  @Test
  void itShouldReturnIdLessTextRenderedNode() {
    var testNode = getTestNode();
    var nodeRender = this.genericNodeRenderer.render(
        testNode,
        new IdLessTextRendererOptions()
    );
    assertEquals(
        IdLessTextNodeRenderOutput.class,
        nodeRender.getClass()
    );
    var textNodeRender = (IdLessTextNodeRenderOutput) nodeRender;
    Approvals.verify(textNodeRender.toPrintableString());
  }

  @Test
  void itShouldReturnHtmlRenderedNode() throws JsonProcessingException {
    var testNode = getTestNode();
    var nodeRender = this.genericNodeRenderer.render(
        testNode,
        new ApiRendererOptions()
    );
    assertEquals(
        NodeResponse.class,
        nodeRender.getClass()
    );
    var apiNodeRender = (NodeResponse) nodeRender;

    var ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    var json = ow.writeValueAsString(apiNodeRender);
    var jsonWithHiddenTime =
        json.replaceAll("(\"createdAt\" : [0-9]+)", "\"createdAt\" : xxxxxxxx");

    Approvals.verify(jsonWithHiddenTime);
  }

  @NotNull
  private TraversableNode getTestNode() {
    var testNode = new Node(
        UniversallyUniqueIdentifier.fromString("b800d6e3-fd98-436f-9a5c-b4568fa7e2f0"),
        "test_type"
    );
    var anotherTestNode = new Node(
        UniversallyUniqueIdentifier.fromString("a648a53c-32dd-4649-8c33-f942fef14646"),
        "another_test_type"
    );
    var testEdge = new Edge(
        UniversallyUniqueIdentifier.fromString("654dfc5f-58fb-4d9f-8d1d-eaba842f4854"),
        testNode,
        "is_testing",
        anotherTestNode
    );
    var anotherTestEdge = new Edge(
        UniversallyUniqueIdentifier.fromString("160dc265-631c-4f05-854e-87db0fec0699"),
        testNode,
        "is_also_testing",
        anotherTestNode
    );
    var testAttribute = new LeafAttribute<>("name", new StringAttributeValue("primary name"));
    var newTestAttribute =
        new LeafAttribute<>("attribute_type", new StringAttributeValue("newTestValue"));
    var anotherTestAttribute =
        new LeafAttribute<>("another_attribute_type", new StringAttributeValue("testValue"));

    testNode = testNode.add(testAttribute);
    testNode = testNode.add(newTestAttribute);
    testNode = testNode.add(anotherTestAttribute);

    var graph = new Graph(
        testNode,
        anotherTestNode,
        testEdge,
        anotherTestEdge
    );

    return graph.traversable().loadNode(testNode.getId());
  }

}
