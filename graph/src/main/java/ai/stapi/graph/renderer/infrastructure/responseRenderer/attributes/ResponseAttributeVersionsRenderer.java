package ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseAttributeVersion;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import java.util.ArrayList;


public class ResponseAttributeVersionsRenderer {

  public <T extends Attribute<?>> ArrayList<ResponseAttributeVersion<T>> render(
      VersionedAttribute<T> attributeVersions, RendererOptions options) {
    var responseAttributeVersions = new ArrayList<ResponseAttributeVersion<T>>();
    attributeVersions.iterateVersionsFromOldest().forEachRemaining(
        attributeVersion -> {
          var responseAttributeVersion = new ResponseAttributeVersion(attributeVersion);
          responseAttributeVersions.add(responseAttributeVersion);
        }
    );

    return responseAttributeVersions;

  }
}
