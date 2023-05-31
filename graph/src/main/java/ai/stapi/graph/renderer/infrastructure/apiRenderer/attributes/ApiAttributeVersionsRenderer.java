package ai.stapi.graph.renderer.infrastructure.apiRenderer.attributes;

import ai.stapi.graph.renderer.infrastructure.apiRenderer.reponseGraph.AttributeVersionResponse;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import java.util.ArrayList;


public class ApiAttributeVersionsRenderer {

  public ArrayList<AttributeVersionResponse> render(
      VersionedAttribute<?> attributeVersions,
      RendererOptions options
  ) {
    var apiAttributeValueRenders = new ArrayList<AttributeVersionResponse>();
    attributeVersions.iterateVersionsFromOldest().forEachRemaining(
        attributeVersion -> {
          var apiAttributeValueRender = new AttributeVersionResponse(attributeVersion);
          apiAttributeValueRenders.add(0, apiAttributeValueRender);
        }
    );

    return apiAttributeValueRenders;

  }
}
