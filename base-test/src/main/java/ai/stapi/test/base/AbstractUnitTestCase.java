package ai.stapi.test.base;

import ai.stapi.graph.Graph;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextGraphRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextRendererOptions;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.attribute.TextAttributeContainerRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge.IdLessTextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.edge.TextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.node.TextNodeRenderer;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJsonStringRenderer;
import ai.stapi.test.FixtureFileLoadableTestTrait;
import ai.stapi.utils.LineFormatter;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUnitTestCase implements FixtureFileLoadableTestTrait {

  private final TextAttributeContainerRenderer textAttributeContainerRenderer =
      new TextAttributeContainerRenderer();

  protected IdLessTextNodeRenderer idLessTextNodeRenderer = new IdLessTextNodeRenderer(
      textAttributeContainerRenderer
  );
  protected TextNodeRenderer textNodeRenderer = new TextNodeRenderer(
      idLessTextNodeRenderer
  );
  protected IdLessTextEdgeRenderer idLessTextEdgeRenderer = new IdLessTextEdgeRenderer(
      textAttributeContainerRenderer
  );
  protected IdLessTextGraphRenderer idLessTextGraphRenderer =
      new IdLessTextGraphRenderer(
          idLessTextNodeRenderer, idLessTextEdgeRenderer
      );
  protected TextEdgeRenderer textEdgeRenderer = new TextEdgeRenderer(
      idLessTextEdgeRenderer
  );

  protected IdLessTextGraphRenderer graphRenderer =
      new IdLessTextGraphRenderer(
          idLessTextNodeRenderer,
          idLessTextEdgeRenderer
      );

  protected ObjectToJsonStringRenderer objectToJsonStringRenderer =
      new ObjectToJsonStringRenderer();

  protected void thenGraphApproved(Graph actualGraph) {
    this.thenGraphApproved(new InMemoryGraphRepository(actualGraph));
  }

  protected void thenGraphApproved(InMemoryGraphRepository actualGraph) {
    var textRenderedGraph = idLessTextGraphRenderer.render(
        actualGraph,
        this.getRenderOptions()
    ).toPrintableString();
    Approvals.verify(textRenderedGraph, "txt");
  }

  protected void thenGraphApproved(Graph actualGraph, Options options) {
    this.thenGraphApproved(
        new InMemoryGraphRepository(actualGraph),
        options
    );
  }

  protected void thenGraphApproved(InMemoryGraphRepository actualGraph,
      Options options) {
    var textRenderedGraph = idLessTextGraphRenderer.render(
        actualGraph,
        this.getRenderOptions()
    ).toPrintableString();
    Approvals.verify(textRenderedGraph, "txt", options);
  }

  protected void thenNodeApproved(TraversableNode node) {
    Assertions.assertNotNull(node);
    var renderedNode = idLessTextNodeRenderer.render(node).toPrintableString();
    Approvals.verify(renderedNode);
  }

  protected void thenNodesApproved(List<TraversableNode> nodes) {
    Assertions.assertNotNull(nodes);
    if (nodes.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    var renderedNodes = nodes.stream()
        .map(node -> idLessTextNodeRenderer.render(node).toPrintableString())
        .sorted();
    Approvals.verify(LineFormatter.createLines(renderedNodes));
  }

  protected void thenUnsortedNodesApproved(List<TraversableNode> nodes) {
    Assertions.assertNotNull(nodes);
    if (nodes.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    var renderedNodes = nodes.stream()
        .map(node -> idLessTextNodeRenderer.render(node).toPrintableString());
    Approvals.verify(LineFormatter.createLines(renderedNodes));
  }

  protected void thenGraphElementApproved(TraversableGraphElement graphElement) {
    if (graphElement instanceof TraversableNode node) {
      this.thenNodeApproved(node);
      return;
    }
    if (graphElement instanceof TraversableEdge edge) {
      this.thenEdgeApproved(edge);
      return;
    }
    Assertions.fail("This should never happen.");
  }

  protected void thenGraphElementsApproved(List<TraversableGraphElement> graphElements) {
    Assertions.assertNotNull(graphElements);
    if (graphElements.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    if (graphElements.get(0) instanceof TraversableNode) {
      var nodes = graphElements.stream().map(node -> (TraversableNode) node).toList();
      this.thenNodesApproved(nodes);
    }
    if (graphElements.get(0) instanceof TraversableEdge) {
      var edges = graphElements.stream().map(edge -> (TraversableEdge) edge).toList();
      this.thenEdgesApproved(edges);
    }
  }

  protected void thenUnsortedGraphElementsApproved(List<TraversableGraphElement> graphElements) {
    Assertions.assertNotNull(graphElements);
    if (graphElements.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    if (graphElements.get(0) instanceof TraversableNode) {
      var nodes = graphElements.stream().map(node -> (TraversableNode) node).toList();
      this.thenUnsortedNodesApproved(nodes);
    }
    if (graphElements.get(0) instanceof TraversableEdge) {
      var edges = graphElements.stream().map(edge -> (TraversableEdge) edge).toList();
      this.thenUnsortedEdgesApproved(edges);
    }
  }

  protected void thenEdgeApproved(TraversableEdge edge) {
    var renderEdge =
        idLessTextEdgeRenderer.render(edge, this.getRenderOptions()).toPrintableString();
    Approvals.verify(renderEdge);
  }

  protected void thenEdgesApproved(List<TraversableEdge> edges) {
    Assertions.assertNotNull(edges);
    if (edges.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    var renderedEdges = edges.stream()
        .map(edge -> idLessTextEdgeRenderer.render(edge).toPrintableString())
        .sorted();
    Approvals.verify(LineFormatter.createLines(renderedEdges));
  }

  protected void thenUnsortedEdgesApproved(List<TraversableEdge> edges) {
    Assertions.assertNotNull(edges);
    if (edges.size() == 0) {
      Approvals.verify(new ArrayList<>());
      return;
    }
    var renderedEdges = edges.stream()
        .map(edge -> idLessTextEdgeRenderer.render(edge).toPrintableString());
    Approvals.verify(LineFormatter.createLines(renderedEdges));
  }
  
  protected void thenIdLessGraphsAreEqual(
      Graph expected,
      Graph actual
  ) {
    this.thenIdLessGraphsAreEqual(
        new InMemoryGraphRepository(expected),
        new InMemoryGraphRepository(actual)
    );
  }

  protected void thenIdLessGraphsAreEqual(
      InMemoryGraphRepository expected,
      InMemoryGraphRepository actual
  ) {
    var renderedExpected =
        idLessTextGraphRenderer.render(expected, this.getRenderOptions())
            .toPrintableString();
    var renderedActual = idLessTextGraphRenderer.render(actual, this.getRenderOptions())
        .toPrintableString();
    Assertions.assertEquals(
        renderedExpected,
        renderedActual
    );
  }

  protected void thenNodesAreSame(InputNode expectedNode, TraversableNode actualNode) {
    var traversableExpectedNode = new Graph(expectedNode)
        .traversable()
        .loadNode(expectedNode.getId());

    var renderedExpected = textNodeRenderer.render(traversableExpectedNode).toPrintableString();
    var renderedActual = textNodeRenderer.render(actualNode).toPrintableString();
    Assertions.assertEquals(
        renderedExpected,
        renderedActual
    );
  }

  protected void thenEdgesAreSame(InputEdge expectedEdge, TraversableEdge actualEdge) {
    var traversableExpectedEdge = new Graph(
        new InputNode(expectedEdge.getNodeFromId(), expectedEdge.getNodeFromType()),
        new InputNode(expectedEdge.getNodeToId(), expectedEdge.getNodeToType()),
        expectedEdge
    ).traversable().loadEdge(expectedEdge.getId(), expectedEdge.getType());

    var renderedExpected = textEdgeRenderer.render(traversableExpectedEdge).toPrintableString();
    var renderedActual = textEdgeRenderer.render(actualEdge).toPrintableString();
    Assertions.assertEquals(
        renderedExpected,
        renderedActual
    );
  }

  protected void thenGraphsAreSame(
      Graph expectedGraph,
      Graph actualGraph
  ) {
    this.thenGraphsAreSame(
        new InMemoryGraphRepository(expectedGraph),
        new InMemoryGraphRepository(actualGraph)
    );
  }

  protected void thenGraphsAreSame(
      InMemoryGraphRepository expectedGraph,
      InMemoryGraphRepository actualGraph
  ) {
    var renderedExpected = idLessTextGraphRenderer.render(
        expectedGraph,
        this.getRenderOptions()
    ).toPrintableString();
    var renderedActual = idLessTextGraphRenderer.render(
        actualGraph,
        this.getRenderOptions()
    ).toPrintableString();

    Assertions.assertEquals(
        renderedExpected,
        renderedActual
    );
  }

  protected void thenEdgesHaveSameIdAndTypeAndNodeIds(InputEdge expectedEdge,
      TraversableEdge actualEdge) {
    Assertions.assertEquals(
        expectedEdge.getId(),
        actualEdge.getId(),
        "Edges have different EdgeIds."
    );
    Assertions.assertEquals(
        expectedEdge.getType(),
        actualEdge.getType(),
        "Edges have different type."
    );
    Assertions.assertEquals(
        expectedEdge.getNodeFromId(),
        actualEdge.getNodeFrom().getId(),
        "Edges have different NodeFrom Id."
    );
    Assertions.assertEquals(
        expectedEdge.getNodeToId(),
        actualEdge.getNodeTo().getId(),
        "Edges have different NodeTo Id."
    );
  }

  protected void thenObjectApproved(Object obj) {
    var options = new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS
    );
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(
      Object obj,
      ObjectToJSonStringOptions.RenderFeature... features
  ) {
    var options = new ObjectToJSonStringOptions(features);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(
      Object obj,
      List<ObjectToJSonStringOptions.RenderFeature> features
  ) {
    var options = new ObjectToJSonStringOptions(features);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApprovedWithoutSorting(Object obj) {
    var options = new ObjectToJSonStringOptions(ObjectToJSonStringOptions.RenderFeature.HIDE_IDS);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApprovedWithShownIds(Object obj) {
    var options =
        new ObjectToJSonStringOptions(ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(Object obj, ObjectToJSonStringOptions options) {
    var objRender = this.objectToJsonStringRenderer.render(obj, options);
    Approvals.verify(objRender.toPrintableString().replace("\\n", System.lineSeparator()));
  }

  protected void thenStringApproved(String actual) {
    Approvals.verify(actual.replace("\\n", System.lineSeparator()));
  }

  protected void thenObjectsEquals(Object expected, Object actual,
      ObjectToJSonStringOptions options) {
    var expectedRender = this.objectToJsonStringRenderer.render(expected, options);
    var actualRender = this.objectToJsonStringRenderer.render(actual, options);
    Assertions.assertEquals(expectedRender.toPrintableString(), actualRender.toPrintableString(),
        "Objects do not match!");
  }

  protected void thenObjectsEquals(Object expected, Object actual) {
    this.thenObjectsEquals(
        expected,
        actual,
        new ObjectToJSonStringOptions(
            ObjectToJSonStringOptions.RenderFeature.HIDE_IDS,
            ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS
        )
    );
  }

  protected <T extends Throwable> T thenExceptionMessageApprovedWithHiddenUuids(
      Class<T> exception,
      Executable throwable
  ) {
    var error = Assertions.assertThrows(exception, throwable);
    var toApprove = error.getMessage().replaceAll("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})",
        "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx");
    Approvals.verify(toApprove);
    return error;
  }

  protected <T extends Throwable> T thenExceptionMessageApproved(
      Class<T> exception,
      Executable throwable
  ) {
    var error = Assertions.assertThrows(exception, throwable);
    Approvals.verify(error.getMessage());
    return error;
  }

  protected IdLessTextRendererOptions getRenderOptions() {
    return new IdLessTextRendererOptions();
  }
}
