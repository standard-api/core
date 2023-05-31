package ai.stapi.graph.renderer.infrastructure.idLessTextRenderer;

import ai.stapi.graph.Graph;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge.IdLessTextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderer;
import ai.stapi.graph.renderer.model.GraphRenderOutput;
import ai.stapi.graph.renderer.model.GraphRenderer;
import ai.stapi.graph.renderer.model.StringGraphRenderOutput;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.utils.LineFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;


public class IdLessTextGraphRenderer implements GraphRenderer {

  private final IdLessTextNodeRenderer idLessTextNodeRenderer;
  private final IdLessTextEdgeRenderer idLessTextEdgeRenderer;

  @Autowired
  public IdLessTextGraphRenderer(
      IdLessTextNodeRenderer idLessTextNodeRenderer,
      IdLessTextEdgeRenderer idLessTextEdgeRenderer
  ) {
    this.idLessTextNodeRenderer = idLessTextNodeRenderer;
    this.idLessTextEdgeRenderer = idLessTextEdgeRenderer;
  }

  @Override
  public GraphRenderOutput render(InMemoryGraphRepository graph) {
    return this.render(
        graph,
        new IdLessTextRendererOptions()
    );
  }

  public GraphRenderOutput render(Graph graph) {
    return this.render(
        new InMemoryGraphRepository(graph),
        new IdLessTextRendererOptions()
    );
  }

  @Override
  public GraphRenderOutput render(
      InMemoryGraphRepository graph,
      RendererOptions options
  ) {
    var idLessRendererOptions = (IdLessTextRendererOptions) options;

    var nodesTitle = LineFormatter.createLine(
        "nodes:",
        idLessRendererOptions.getIndentLevel()
    );
    var renderedNodes = this.getRenderedNodesString(
        graph,
        idLessRendererOptions
    );
    var edgesTitle = LineFormatter.createLine(
        "edges:",
        idLessRendererOptions.getIndentLevel()
    );
    var renderedEdges = this.getRenderedEdgeString(
        graph,
        idLessRendererOptions
    );

    return new StringGraphRenderOutput(
        nodesTitle + renderedNodes + LineFormatter.createNewLine() + edgesTitle + renderedEdges);
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options instanceof IdLessTextRendererOptions;
  }

  private String getRenderedNodesString(InMemoryGraphRepository graph,
      IdLessTextRendererOptions options) {
    List<TraversableNode> traversableNodes = graph.loadAllNodes();
    return traversableNodes.stream()
        .sorted(Comparator.comparing(
            node -> node.getSortingNameWithNodeTypeFallback() + node.getIdlessHashCodeWithEdges())
        ).map(node -> idLessTextNodeRenderer.render(
                node,
                new IdLessTextRendererOptions(
                    options.getIndentLevel() + 1,
                    options.getNodeRelationAnnotationAttributeName().orElse("")
                )
            ).toPrintableString()
        ).sorted()
        .collect(Collectors.joining(LineFormatter.createNewLine()));
  }

  private String getRenderedEdgeString(InMemoryGraphRepository graph,
      IdLessTextRendererOptions options) {
    return graph.loadAllEdges().stream()
        .map(edge -> idLessTextEdgeRenderer.render(
            edge,
            new IdLessTextRendererOptions(
                options.getIndentLevel() + 1,
                options.getNodeRelationAnnotationAttributeName().orElse("")
            )
        ).toPrintableString())
        .sorted()
        .collect(Collectors.joining(LineFormatter.createNewLine()));
  }
}
