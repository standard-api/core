package ai.stapi.objectRenderer.exceptions;

import ai.stapi.objectRenderer.model.RendererOptions;

public class OptionsAreNotSupportedByAnyRendererException extends RuntimeException {

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
