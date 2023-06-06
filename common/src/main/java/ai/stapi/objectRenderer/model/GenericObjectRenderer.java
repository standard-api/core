package ai.stapi.objectRenderer.model;

import ai.stapi.objectRenderer.exceptions.OptionsAreNotSupportedByAnyRendererException;
import ai.stapi.objectRenderer.exceptions.OptionsAreSupportedByMultipleRenderersException;
import java.util.List;

public class GenericObjectRenderer {

  private List<ObjectRenderer> existingObjectRenderers;

  public GenericObjectRenderer(List<ObjectRenderer> existingObjectRenderers) {
    this.existingObjectRenderers = existingObjectRenderers;
  }

  public RenderOutput render(Object obj, RendererOptions options) {
    var renderer = this.getObjectRenderer(options);
    return renderer.render(obj, options);
  }

  private ObjectRenderer getObjectRenderer(RendererOptions options) {
    var renderers = existingObjectRenderers.stream().filter(
        renderer -> renderer.supports(options)
    ).toList();

    if (renderers.isEmpty()) {
      throw new OptionsAreNotSupportedByAnyRendererException(options);
    }

    if (renderers.size() > 1) {
      throw new OptionsAreSupportedByMultipleRenderersException(options);
    }

    return renderers.stream()
        .findFirst()
        .orElseThrow();
  }


}
