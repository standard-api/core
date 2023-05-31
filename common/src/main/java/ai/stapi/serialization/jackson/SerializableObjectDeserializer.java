package ai.stapi.serialization.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.serialization.SerializableObject;
import ai.stapi.serialization.classNameProvider.GenericClassNameProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;

public class SerializableObjectDeserializer<T extends SerializableObject> extends StdDeserializer<T> {

  private GenericClassNameProvider classNameProvider;
  private ObjectMapper objectMapper;
  private Class<?> setupBeanClass;

  public SerializableObjectDeserializer(
      GenericClassNameProvider classNameProvider,
      ObjectMapper objectMapper,
      Class<?> setupBeanClass
  ) {
    super(setupBeanClass);
    this.setupBeanClass = setupBeanClass;
    this.classNameProvider = classNameProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    JsonNode jsonNode = node.get(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE
    );
    if (jsonNode == null) {
      throw new RuntimeException(
          "Cannot deserialize SerializableObject, because it is missing serializationType." +
              "\nClass to be deserialized: " + this.setupBeanClass.getSimpleName() +
              "\nProvided data: " + node
      );
    }
    var serializationType = jsonNode.asText();
    var classType = classNameProvider.getClassType(serializationType);
    var serializedObject = node.toString();
    return (T) this.objectMapper.readValue(serializedObject, classType);
  }
}