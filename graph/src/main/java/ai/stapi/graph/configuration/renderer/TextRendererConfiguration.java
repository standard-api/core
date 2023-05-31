package ai.stapi.graph.configuration.renderer;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge.IdLessTextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.edge.TextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.textRenderer.node.TextNodeRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter(IdLessTextRendererConfiguration.class)
public class TextRendererConfiguration {
  
  @Bean
  public TextNodeRenderer textNodeRenderer(
      IdLessTextNodeRenderer idLessTextNodeRenderer
  ) {
    return new TextNodeRenderer(idLessTextNodeRenderer);
  }
  
  @Bean
  public TextEdgeRenderer textEdgeRenderer(
      IdLessTextEdgeRenderer idLessTextEdgeRenderer
  ) {
    return new TextEdgeRenderer(idLessTextEdgeRenderer);
  }
}
