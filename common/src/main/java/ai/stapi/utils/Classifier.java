package ai.stapi.utils;

import java.util.UUID;

public class Classifier {

  public static boolean isPrimitiveOrString(Object value) {
    return value instanceof Byte || value instanceof Short || value instanceof Integer
        || value instanceof Long ||
        value instanceof Float || value instanceof Double || value instanceof Character
        || value instanceof Boolean ||
        value instanceof String || value instanceof UUID || value instanceof byte[];
  }
}
