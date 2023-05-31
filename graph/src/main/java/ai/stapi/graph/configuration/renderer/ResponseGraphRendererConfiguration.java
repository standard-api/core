package ai.stapi.graph.configuration.renderer;

import ai.stapi.graph.renderer.infrastructure.responseRenderer.ResponseGraphRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes.ResponseAttributeVersionsRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes.ResponseAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.edge.ResponseEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.node.ResponseNodeRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ResponseGraphRendererConfiguration {
  
  @Bean
  public ResponseAttributeVersionsRenderer responseAttributeVersionsRenderer() {
    return new ResponseAttributeVersionsRenderer();
  }
  
  @Bean
  public ResponseAttributesRenderer responseAttributesRenderer(
      ResponseAttributeVersionsRenderer responseAttributeVersionsRenderer
  ) {
    return new ResponseAttributesRenderer(responseAttributeVersionsRenderer);
  }
  
  @Bean
  public ResponseNodeRenderer responseNodeRenderer(
      ResponseAttributesRenderer responseAttributesRenderer
  ) {
    return new ResponseNodeRenderer(responseAttributesRenderer);
  }
  
  @Bean
  public ResponseEdgeRenderer responseEdgeRenderer(
      ResponseAttributesRenderer responseAttributesRenderer
  ) {
    return new ResponseEdgeRenderer(responseAttributesRenderer);
  }
  
  @Bean
  public ResponseGraphRenderer responseGraphRenderer(
      ResponseNodeRenderer responseNodeRenderer,
      ResponseEdgeRenderer responseEdgeRenderer
  ) {
    return new ResponseGraphRenderer(responseNodeRenderer, responseEdgeRenderer);
  }
}
