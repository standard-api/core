package ai.stapi.graph.renderer.infrastructure.responseRenderer.attributes;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph.ResponseAttribute;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;


public class ResponseAttributesRenderer {

  private final ResponseAttributeVersionsRenderer responseAttributeVersionsRenderer;

  @Autowired
  public ResponseAttributesRenderer(
      ResponseAttributeVersionsRenderer responseAttributeVersionsRenderer) {
    this.responseAttributeVersionsRenderer = responseAttributeVersionsRenderer;
  }

  public Map<String, ResponseAttribute<?>> render(AbstractAttributeContainer attributeContainer,
                                                  RendererOptions options) {
    return attributeContainer.getVersionedAttributeList().stream()
        .map(attribute -> this.renderResponseAttribute(options, attribute))
        .collect(Collectors.toMap(ResponseAttribute::getName, Function.identity()));
  }

  @NotNull
  private ResponseAttribute<?> renderResponseAttribute(RendererOptions options,
      VersionedAttribute<?> attribute) {
    return new ResponseAttribute<>(attribute.getName(),
        this.responseAttributeVersionsRenderer.render(attribute, options));
  }

}
