package ai.stapi.graph.renderer.model.exceptions;

import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public class RenderOptionsNotRecognizedException extends RuntimeException {

  public RenderOptionsNotRecognizedException(Class<? extends RendererOptions> supports,
      Class<? extends RendererOptions> provided) {
    super(
        String.format(
            "Renderer supports only %s Provided: %s",
            supports,
            provided
        )
    );
  }
}
