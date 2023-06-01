package ai.stapi.graph.renderer.model.edgeRenderer;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.Graph;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.test.base.UnitTestCase;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

class EdgeRendererTest extends UnitTestCase {

  @Test
  void itShouldRenderEdge() {
    var nodeFrom = new InputNode("statement");
    var nodeTo = new InputNode("research");
    var edge = new InputEdge(UniversallyUniqueIdentifier.fromString(
        "b800d6e3-fd98-436f-9a5c-b4568fa7e2f0"),
        nodeFrom,
        "is_based_on",
        nodeTo
    );
    edge = edge.add(
        new LeafAttribute<>("attribute_name", new StringAttributeValue("test_value")));

    var inMemoryGraph = new Graph(
        nodeFrom,
        nodeTo,
        edge
    );

    var loadedEdge = inMemoryGraph.traversable().loadEdge(
        edge.getId(),
        edge.getType()
    );

    var actualEdgeString = textEdgeRenderer.render(loadedEdge).toPrintableString();
    Approvals.verify(actualEdgeString);
  }
}
