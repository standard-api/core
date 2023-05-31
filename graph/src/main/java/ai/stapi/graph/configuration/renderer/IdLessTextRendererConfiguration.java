package ai.stapi.graph.configuration.renderer;

import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.IdLessTextGraphRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.attribute.TextAttributeContainerRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.edge.IdLessTextEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.idLessTextRenderer.node.IdLessTextNodeRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class IdLessTextRendererConfiguration {
  
  @Bean
  public TextAttributeContainerRenderer textAttributeContainerRenderer() {
    return new TextAttributeContainerRenderer();
  }
  
  @Bean
  public IdLessTextNodeRenderer idLessTextNodeRenderer(
      TextAttributeContainerRenderer textAttributeContainerRenderer
  ) {
    return new IdLessTextNodeRenderer(textAttributeContainerRenderer);
  }
  
  @Bean
  public IdLessTextEdgeRenderer idLessTextEdgeRenderer(
      TextAttributeContainerRenderer textAttributeContainerRenderer
  ) {
    return new IdLessTextEdgeRenderer(textAttributeContainerRenderer);
  }
  
  @Bean
  public IdLessTextGraphRenderer idLessTextGraphRenderer(
      IdLessTextNodeRenderer idLessTextNodeRenderer,
      IdLessTextEdgeRenderer idLessTextEdgeRenderer
  ) {
    return new IdLessTextGraphRenderer(idLessTextNodeRenderer, idLessTextEdgeRenderer);
  }
}
