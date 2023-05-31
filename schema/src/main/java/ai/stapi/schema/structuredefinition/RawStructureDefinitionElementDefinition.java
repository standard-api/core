package ai.stapi.schema.structuredefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class RawStructureDefinitionElementDefinition {

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

  private ArrayList<ElementDefinitionType> type;

  protected RawStructureDefinitionElementDefinition() {
  }

  public RawStructureDefinitionElementDefinition(
      String path,
      @Nullable Integer min,
      @Nullable String max,
      @Nullable String shortDescription,
      @Nullable String definition,
      @Nullable String comment,
      ArrayList<ElementDefinitionType> type
  ) {
    this.path = path;
    this.min = min;
    this.max = max;
    this.shortDescription = shortDescription;
    this.definition = definition;
    this.comment = comment;
    this.type = type;
  }

  public RawStructureDefinitionElementDefinition(
      String path,
      @Nullable Integer min,
      @Nullable String max,
      @Nullable String shortDescription,
      @Nullable String definition,
      @Nullable String comment,
      @Nullable String contentReference,
      ArrayList<ElementDefinitionType> type
  ) {
    this.path = path;
    this.min = min;
    this.max = max;
    this.shortDescription = shortDescription;
    this.definition = definition;
    this.comment = comment;
    this.type = type;
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

  @Nullable
  public String getContentReference() {
    return contentReference;
  }

  public ArrayList<ElementDefinitionType> getType() {
    return type;
  }

  public static class ElementDefinitionType {

    private String code;
    private List<String> targetProfile = new ArrayList<>();

    protected ElementDefinitionType() {
    }

    public ElementDefinitionType(
        String code,
        List<String> targetProfile
    ) {
      this.code = code;
      this.targetProfile = targetProfile;
    }

    public String getCode() {
      return code;
    }

    public List<String> getTargetProfile() {
      return targetProfile;
    }
  }
}