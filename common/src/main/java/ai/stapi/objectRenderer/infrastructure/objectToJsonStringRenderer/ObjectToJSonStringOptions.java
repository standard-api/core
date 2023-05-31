package ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer;


import ai.stapi.objectRenderer.model.RendererOptions;
import java.util.Arrays;
import java.util.List;

public class ObjectToJSonStringOptions implements RendererOptions {

  private final List<RenderFeature> features;

  public ObjectToJSonStringOptions(RenderFeature... renderFeatures) {
    this.features = Arrays.stream(renderFeatures).toList();
  }

  public ObjectToJSonStringOptions(List<RenderFeature> renderFeatures) {
    this.features = renderFeatures;
  }

  public List<RenderFeature> getFeatures() {
    return features;
  }

  public enum RenderFeature {
    SORT_FIELDS,
    HIDE_CREATED_AT,
    HIDE_DISPATCHED_AT,
    HIDE_IDS,
    HIDE_KEY_HASHCODE, RENDER_GETTERS
  }
}