package ai.stapi.graphoperations.graphRenderer.infrastructure.IdlessTextRenderer;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.test.base.UnitTestCase;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

public class IdLessTextGraphRendererTest extends UnitTestCase {

  @Test
  public void itShouldRenderGraph() {
    var nodeFrom = new Node("statement");
    var nodeTo = new Node("research");
    var edge = new Edge(
        UniversallyUniqueIdentifier.fromString(
            "b800d6e3-fd98-436f-9a5c-b4568fa7e2f0"),
        nodeFrom,
        "is_based_on",
        nodeTo
    );
    var edge2 = new Edge(
        UniversallyUniqueIdentifier.fromString(
            "c8fd9c2b-bb4e-4536-becd-529356d9ab71"),
        nodeFrom,
        "is_derived_from",
        nodeTo
    );
    var testAttribute = new LeafAttribute<>("attribute_name", new StringAttributeValue("test_value"
    ));
    nodeFrom = nodeFrom.add(testAttribute);
    nodeTo = nodeTo.add(testAttribute);
    edge = edge.add(testAttribute);

    var graph = new Graph(
        nodeFrom,
        nodeTo,
        edge,
        edge2
    );

    var actualGraphString =
        graphRenderer.render(graph).toPrintableString();
    Approvals.verify(actualGraphString);
  }
}
