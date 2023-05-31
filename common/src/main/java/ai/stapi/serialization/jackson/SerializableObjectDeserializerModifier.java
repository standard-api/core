package ai.stapi.serialization.jackson;

import ai.stapi.serialization.SerializableObject;
import ai.stapi.serialization.classNameProvider.GenericClassNameProvider;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import java.lang.reflect.Modifier;

public class SerializableObjectDeserializerModifier extends BeanDeserializerModifier {

    private final GenericClassNameProvider nameProvider;
    private final ObjectMapper objectMapper;

    public SerializableObjectDeserializerModifier(
        GenericClassNameProvider nameProvider,
        ObjectMapper objectMapper
    ) {
      this.nameProvider = nameProvider;
      this.objectMapper = objectMapper;
    }

    @Override
    public JsonDeserializer<?> modifyDeserializer(
        DeserializationConfig config,
        BeanDescription beanDesc,
        JsonDeserializer<?> deserializer
    ) {
      JsonDeserializer<?> defaultDeserializer =
          super.modifyDeserializer(config, beanDesc, deserializer);
      if (
          (beanDesc.getBeanClass().isInterface() || Modifier.isAbstract(
              beanDesc.getBeanClass().getModifiers())
          ) && SerializableObject.class.isAssignableFrom(beanDesc.getBeanClass())
      ) {
        return new SerializableObjectDeserializer<>(
            this.nameProvider,
            this.objectMapper,
            beanDesc.getBeanClass()
        );
      }
      return defaultDeserializer;
    }
  }