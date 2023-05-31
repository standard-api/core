package ai.stapi.graph.renderer.model.nodeRenderer.exceptions;

import ai.stapi.graph.renderer.model.exceptions.GraphRendererException;
import ai.stapi.graph.renderer.model.nodeRenderer.RendererOptions;

public class OptionsAreNotSupportedByAnyRendererException extends GraphRendererException {

  public OptionsAreNotSupportedByAnyRendererException(RendererOptions options) {
    super(
        createMessage(options)
    );
  }

  private static String createMessage(RendererOptions options) {
    return String.format(
        "Renderer options %s are not supported by any renderer.",
        options.getClass()
    );
  }
}
