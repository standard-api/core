package ai.stapi.objectRenderer.exceptions;


import ai.stapi.objectRenderer.model.RendererOptions;

public class OptionsAreSupportedByMultipleRenderersException extends RuntimeException {

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
