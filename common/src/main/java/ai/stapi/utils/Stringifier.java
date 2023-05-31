package ai.stapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Stringifier {

  public static String convertToString(Object value) {
    try {
      return new ObjectMapper()
          .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(value);
    } catch (JsonProcessingException ignored) {
    }
    return value.toString();
  }
}
