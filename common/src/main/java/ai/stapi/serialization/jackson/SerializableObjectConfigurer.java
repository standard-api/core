package ai.stapi.serialization.jackson;

import ai.stapi.serialization.classNameProvider.GenericClassNameProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SerializableObjectConfigurer {
  
  private final GenericClassNameProvider genericClassNameProvider;

  public SerializableObjectConfigurer(GenericClassNameProvider genericClassNameProvider) {
    this.genericClassNameProvider = genericClassNameProvider;
  }

  public void configure(ObjectMapper objectMapper) {
    var module = new SimpleModule("SerializableObjectModule");
    module.setDeserializerModifier(
        new SerializableObjectDeserializerModifier(genericClassNameProvider, objectMapper)
    );
    objectMapper.registerModule(module);
  }
}
