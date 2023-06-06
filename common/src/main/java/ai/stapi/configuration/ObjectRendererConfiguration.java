package ai.stapi.configuration;

import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJsonStringRenderer;
import ai.stapi.objectRenderer.model.GenericObjectRenderer;
import ai.stapi.objectRenderer.model.ObjectRenderer;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ObjectRendererConfiguration {
  
  @Bean
  public GenericObjectRenderer genericObjectRenderer(
      List<ObjectRenderer> objectRenderers
  ) {
    return new GenericObjectRenderer(objectRenderers);
  }
  
  @Bean
  public ObjectToJsonStringRenderer objectToJsonStringRenderer() {
    return new ObjectToJsonStringRenderer();
  }
}
