package ai.stapi.graph.configuration.renderer;

import ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes.ApiAttributeVersionsRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes.ApiAttributesRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.edge.ApiEdgeRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.node.ApiCompactNodeRenderer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.node.ApiNodePrimaryNameFactory;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.node.ApiNodeRenderer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ApiRendererConfiguration {
  
  @Bean
  public ApiAttributeVersionsRenderer apiAttributeVersionsRenderer() {
    return new ApiAttributeVersionsRenderer();
  }
  
  @Bean
  public ApiAttributesRenderer apiAttributesRenderer(
      ApiAttributeVersionsRenderer apiAttributeVersionsRenderer
  ) {
    return new ApiAttributesRenderer(apiAttributeVersionsRenderer);
  }
  
  @Bean
  public ApiNodePrimaryNameFactory apiNodePrimaryNameFactory() {
    return new ApiNodePrimaryNameFactory();
  }

  @Bean
  public ApiCompactNodeRenderer apiCompactNodeRenderer(
      ApiAttributesRenderer apiAttributesRenderer,
      ApiNodePrimaryNameFactory apiNodePrimaryNameFactory
  ) {
    return new ApiCompactNodeRenderer(apiAttributesRenderer, apiNodePrimaryNameFactory);
  }

  @Bean
  public ApiEdgeRenderer apiEdgeRenderer(
      ApiCompactNodeRenderer apiCompactNodeRenderer,
      ApiAttributesRenderer apiAttributesRenderer
  ) {
    return new ApiEdgeRenderer(apiCompactNodeRenderer, apiAttributesRenderer);
  }
  
  @Bean
  public ApiNodeRenderer apiNodeRenderer(
      ApiAttributesRenderer apiAttributesRenderer,
      ApiEdgeRenderer apiEdgeRenderer,
      ApiNodePrimaryNameFactory apiNodePrimaryNameFactory
  ) {
    return new ApiNodeRenderer(apiAttributesRenderer, apiEdgeRenderer, apiNodePrimaryNameFactory);
  }
}
