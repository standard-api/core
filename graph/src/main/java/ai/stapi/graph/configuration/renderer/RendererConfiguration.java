package ai.stapi.graph.configuration.renderer;

import ai.stapi.graph.renderer.model.GenericGraphRenderer;
import ai.stapi.graph.renderer.model.GraphRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.GenericNodeRenderer;
import ai.stapi.graph.renderer.model.nodeRenderer.NodeRenderer;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RendererConfiguration {
  
  @Bean
  public GenericGraphRenderer genericGraphRenderer(
      List<GraphRenderer> graphRenderers
  ) {
    return new GenericGraphRenderer(graphRenderers);
  }
  
  @Bean
  public GenericNodeRenderer genericNodeRenderer(
      List<NodeRenderer> nodeRenderers
  ) {
    return new GenericNodeRenderer(nodeRenderers);
  }
}
