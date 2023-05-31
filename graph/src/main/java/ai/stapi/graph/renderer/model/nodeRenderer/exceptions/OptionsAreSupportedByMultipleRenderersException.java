package ai.stapi.graph.renderer.model.nodeRenderer.exceptions;

import ai.stapi.graph.renderer.model.exceptions.GraphRendererException;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public class OptionsAreSupportedByMultipleRenderersException extends GraphRendererException {

  public OptionsAreSupportedByMultipleRenderersException(RendererOptions options) {
    super(
        createMessage(options)
    );
  }

  private static String createMessage(RendererOptions options) {
    return String.format(
        "Renderer options %s are supported by more than one renderer.",
        options.getClass()
    );
  }
}
