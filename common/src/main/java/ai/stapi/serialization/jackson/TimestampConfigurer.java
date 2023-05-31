package ai.stapi.serialization.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.sql.Timestamp;

public class TimestampConfigurer {

  public static void configureTimestampModule(ObjectMapper objectMapper) {
    var timestampModule = new SimpleModule("CustomTimestampModule");
    timestampModule.addSerializer(
        Timestamp.class,
        new JsonSerializer<>() {

          @Override
          public void serialize(
              Timestamp value,
              JsonGenerator gen,
              SerializerProvider serializers
          ) throws IOException {
            gen.writeString(value.toString());
          }
        }
    );
    timestampModule.addDeserializer(
        Timestamp.class,
        new JsonDeserializer<>() {
          @Override
          public Timestamp deserialize(
              JsonParser p,
              DeserializationContext ctxt
          ) throws IOException {
            return Timestamp.valueOf(p.getValueAsString());
          }
        }
    );
    objectMapper.registerModule(timestampModule);
  }
}
