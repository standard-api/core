package ai.stapi.objectRenderer.model;

public interface ObjectRenderer {

  RenderOutput render(Object obj);

  RenderOutput render(Object obj, RendererOptions options);

  boolean supports(RendererOptions options);
}
