package ai.stapi.schema.structuredefinition;

import ai.stapi.serialization.SerializableObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;

public class RawStructureDefinitionData implements SerializableObject {

  public static final String SERIALIZATION_TYPE = "RawStructureDefinitionData";
  private String id;
  private String url;
  private String status;

  private String description;
  private String kind;
  private Boolean isAbstract;
  private String type;

  private String baseDefinition;
  private Differential differential;

  protected RawStructureDefinitionData() {
  }

  public RawStructureDefinitionData(
      String id,
      String url,
      String status,
      String description,
      String kind,
      Boolean isAbstract,
      String type,
      String baseDefinition,
      Differential differential
  ) {
    this.id = id;
    this.url = url;
    this.status = status;
    this.description = description;
    this.kind = kind;
    this.isAbstract = isAbstract;
    this.type = type;
    this.baseDefinition = baseDefinition;
    this.differential = differential;
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public String getStatus() {
    return status;
  }

  public String getDescription() {
    return description;
  }

  public String getKind() {
    return kind;
  }

  @JsonProperty("abstract")
  public Boolean getIsAbstract() {
    return isAbstract;
  }

  public String getType() {
    return type;
  }

  public String getBaseDefinition() {
    return baseDefinition;
  }

  public Differential getDifferential() {
    return differential;
  }

  @Override
  public String getSerializationType() {
    return SERIALIZATION_TYPE;
  }

  public static class Differential {

    private ArrayList<RawStructureDefinitionElementDefinition> element;
    private String parent;


    protected Differential() {
    }

    public Differential(
        ArrayList<RawStructureDefinitionElementDefinition> element,
        String parent
    ) {
      this.element = element;
      this.parent = parent;
    }

    public ArrayList<RawStructureDefinitionElementDefinition> getElement() {
      return element;
    }

    @Nullable
    public String getParent() {
      return parent;
    }
  }
}
