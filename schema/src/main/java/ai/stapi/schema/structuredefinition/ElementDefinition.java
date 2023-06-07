package ai.stapi.schema.structuredefinition;

import ai.stapi.serialization.SerializableObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class ElementDefinition implements SerializableObject {

  public static final String SERIALIZATION_TYPE = "ElementDefinition";
  
  private String path;

  @Nullable
  private Integer min;

  @Nullable
  private String max;

  @Nullable
  private String shortDescription;

  @Nullable
  private String definition;

  @Nullable
  private String comment;

  @Nullable
  private String contentReference;

  private List<ElementDefinitionType> type;

  protected ElementDefinition() {
  }

  public ElementDefinition(
      String path,
      List<ElementDefinitionType> type,
      @Nullable Integer min,
      @Nullable String max,
      @Nullable String shortDescription,
      @Nullable String definition,
      @Nullable String comment
  ) {
    this.path = path;
    this.type = type;
    this.min = min;
    this.max = max;
    this.shortDescription = shortDescription;
    this.definition = definition;
    this.comment = comment;
  }

  public ElementDefinition(
      String path,
      List<ElementDefinitionType> type,
      @Nullable Integer min,
      @Nullable String max,
      @Nullable String shortDescription,
      @Nullable String definition,
      @Nullable String comment,
      @Nullable String contentReference
  ) {
    this.path = path;
    this.type = type;
    this.min = min;
    this.max = max;
    this.shortDescription = shortDescription;
    this.definition = definition;
    this.comment = comment;
    this.contentReference = contentReference;
  }

  public String getPath() {
    return path;
  }

  @Nullable
  public Integer getMin() {
    return min;
  }

  @Nullable
  public String getMax() {
    return max;
  }

  @Nullable
  @JsonProperty("short")
  public String getShortDescription() {
    return shortDescription;
  }

  @Nullable
  public String getDefinition() {
    return definition;
  }

  @Nullable
  public String getComment() {
    return comment;
  }

  public List<ElementDefinitionType> getType() {
    return this.type;
  }

  @Nullable
  public String getContentReference() {
    return contentReference;
  }

  @Override
  public String toString() {
    return "ElementDefinition{" +
        "path='" + path + '\'' +
        ", min=" + min +
        ", max='" + max + '\'' +
        ", shortDescription='" + shortDescription + '\'' +
        ", definition='" + definition + '\'' +
        ", comment='" + comment + '\'' +
        ", type=" + type +
        '}';
  }

  @Override
  public String getSerializationType() {
    return SERIALIZATION_TYPE;
  }
}