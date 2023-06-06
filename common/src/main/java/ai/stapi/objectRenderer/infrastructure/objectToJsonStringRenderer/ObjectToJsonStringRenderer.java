package ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer;

import ai.stapi.serialization.jackson.TimestampConfigurer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions.RenderFeature;
import ai.stapi.objectRenderer.model.ObjectRenderer;
import ai.stapi.objectRenderer.model.RenderOutput;
import ai.stapi.objectRenderer.model.RendererOptions;
import ai.stapi.serialization.AbstractSerializableObject;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

public class ObjectToJsonStringRenderer implements ObjectRenderer {

  @Override
  public RenderOutput render(Object obj) {
    if (obj == null) {
      return new ObjectToJsonStringRenderOutput("null");
    }
    String result = "";
    try {
      result = this.getMapper(new ObjectToJSonStringOptions()).writerWithDefaultPrettyPrinter()
          .writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return new ObjectToJsonStringRenderOutput(result);
  }

  @Override
  public RenderOutput render(
      Object object,
      RendererOptions options
  ) {
    if (object == null) {
      return new ObjectToJsonStringRenderOutput("null");
    }
    var specificOptions = (ObjectToJSonStringOptions) options;
    if (specificOptions.getFeatures()
        .contains(ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS)) {
      object = this.convertObjectToSortedMap(object, specificOptions);
    }

    try {
      var result = this.getMapper(specificOptions)
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(object);

      if (specificOptions.getFeatures()
          .contains(ObjectToJSonStringOptions.RenderFeature.HIDE_IDS)) {
        result = this.hideId(result);
      }

      if (specificOptions.getFeatures().contains(RenderFeature.HIDE_KEY_HASHCODE)) {
        result = this.hideKeyHashcode(result);
      }

      if (specificOptions.getFeatures()
          .contains(ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT)) {
        result = this.hideCreatedAt(result);
      }
      if (specificOptions.getFeatures()
          .contains(ObjectToJSonStringOptions.RenderFeature.HIDE_DISPATCHED_AT)) {
        result = this.hideDispatchedAt(result);
      }
      return new ObjectToJsonStringRenderOutput(result);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private ObjectMapper getMapper(ObjectToJSonStringOptions options) {
    var mapper = new ObjectMapper();
    TimestampConfigurer.configureTimestampModule(mapper);
    var shouldRenderGetters = options.getFeatures()
        .contains(ObjectToJSonStringOptions.RenderFeature.RENDER_GETTERS);

    mapper.setVisibility(
        mapper.getSerializationConfig()
            .getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(shouldRenderGetters ? JsonAutoDetect.Visibility.DEFAULT
                : JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
            .withIsGetterVisibility(shouldRenderGetters ? JsonAutoDetect.Visibility.DEFAULT
                : JsonAutoDetect.Visibility.NONE)
    ).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    return mapper;
  }

  private Map<String, Object> convertToMap(Object object, ObjectToJSonStringOptions options) {
    return this.getMapper(options).convertValue(object, new TypeReference<>() {});
  }

  private Object convertObjectToSortedMap(Object object, ObjectToJSonStringOptions options) {
    if (object == null) {
      return null;
    }
    if (isPrimitiveType(object)) {
      return object;
    }
    if (object instanceof Collection<?> collection && !(object instanceof Map<?, ?>)) {
      return this.getMappedList(collection, options);
    }

    var result = convertToMap(object, options);
    return this.createSortedMap(result, options);
  }

  private boolean isPrimitiveType(Object object) {
    return object instanceof String
        || object instanceof Number
        || object instanceof Boolean;
  }

  private boolean isList(Object object) {
    return object instanceof Collection<?>;
  }

  private boolean isMap(Object object) {
    return object instanceof Map;
  }

  private Map<String, Object> createSortedMap(
      Map<String, Object> object,
      ObjectToJSonStringOptions options
  ) {
    var outputMap = new LinkedHashMap<String, Object>();
    this.addSpecificFieldIfPresent(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE,
        outputMap,
        object,
        options
    );
    this.addSpecificFieldIfPresent("id", outputMap, object, options);
    this.addFieldContainingStringIfPresent("Id", outputMap, object, options);

    //add primitiveType fields
    object.entrySet().stream()
        .filter(entry -> !outputMap.containsKey(entry.getKey()))
        .filter(entry -> isPrimitiveType(entry.getValue()))
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .forEach(entry -> outputMap.put(entry.getKey(), entry.getValue()));

    //add other type fields
    object.entrySet().stream()
        .filter(entry -> !outputMap.containsKey(entry.getKey()))
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .forEach(entry -> {
          if (entry.getValue() instanceof Map map) {
            outputMap.put(entry.getKey(), this.createSortedMap(map, options));
          } else if (entry.getValue() instanceof Collection<?> list) {
            outputMap.put(entry.getKey(), this.getMappedList(list, options));
          } else {
            outputMap.put(entry.getKey(), entry.getValue());
          }
        });
    return outputMap;
  }

  private Collection<?> getMappedList(Collection<?> collection, ObjectToJSonStringOptions options) {
    var sortedList = new LinkedList<>();
    collection.forEach(item -> {
      if (!isList(item) && !isPrimitiveType(item)) {
        var map = this.convertToMap(item, options);
        sortedList.add(this.createSortedMap(map, options));
      } else {
        sortedList.add(item);
      }
    });
    return sortedList;
  }

  private void addSpecificFieldIfPresent(
      String fieldName,
      Map<String, Object> outputMap,
      Map<String, Object> objectMap,
      ObjectToJSonStringOptions options
  ) {
    objectMap.entrySet().stream()
        .filter(entry -> entry.getKey().equals(fieldName))
        .findAny()
        .ifPresent(
            entry -> outputMap.put(
                entry.getKey(),
                this.convertObjectToSortedMap(entry.getValue(), options)
            )
        );
  }

  private void addFieldContainingStringIfPresent(
      String fieldName,
      Map<String, Object> outputMap,
      Map<String, Object> objectMap,
      ObjectToJSonStringOptions options
  ) {
    objectMap.entrySet().stream()
        .filter(entry -> entry.getKey().contains(fieldName))
        .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
        .forEach(
            entry -> outputMap.put(
                entry.getKey(),
                this.convertObjectToSortedMap(entry.getValue(), options)
            )
        );
  }

  private String hideId(String render) {
    return render.replaceAll("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})",
        "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx");
  }

  private String hideKeyHashcode(String render) {
    return render.replaceAll(
        "(\"_key\"\\s*:\\s*\"[A-Za-z0-9-]+?_)([A-Za-z0-9]{6})",
        "$1xxxxxx"
    );
  }

  private String hideCreatedAt(String render) {
    String regex = "\"createdAt\"\\s*:\\s*\"[^\"]+\"";
    String replacement = "\"createdAt\" : hidden_date";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(render);

    return matcher.replaceAll(replacement);
  }

  private String hideDispatchedAt(String render) {
    return render.replaceAll(
        "(?<=\"dispatchedAt\" : )(\"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+\")",
        "hidden_date");
  }

  @Override
  public boolean supports(RendererOptions options) {
    return options instanceof ObjectToJSonStringOptions;
  }

}