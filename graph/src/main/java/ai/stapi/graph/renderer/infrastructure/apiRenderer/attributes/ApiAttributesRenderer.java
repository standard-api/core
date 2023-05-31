package ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.AttributeResponse;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;


public class ApiAttributesRenderer {

  private ApiAttributeVersionsRenderer apiAttributeVersionsRenderer;

  @Autowired
  public ApiAttributesRenderer(ApiAttributeVersionsRenderer apiAttributeVersionsRenderer) {
    this.apiAttributeVersionsRenderer = apiAttributeVersionsRenderer;
  }

  public ArrayList<AttributeResponse> render(
      AbstractAttributeContainer attributeContainer,
      RendererOptions options
  ) {
    var attributes = new ArrayList<AttributeResponse>();
    attributeContainer.getVersionedAttributeList().forEach(
        (versionedAttribute) -> {

          var apiAttributeValueRenders = this.apiAttributeVersionsRenderer.render(
              versionedAttribute,
              options
          );
          var apiAttributeRender = new AttributeResponse(
              versionedAttribute.getName(),
              apiAttributeValueRenders
          );
          attributes.add(apiAttributeRender);
        }
    );

    return attributes;

  }
}
