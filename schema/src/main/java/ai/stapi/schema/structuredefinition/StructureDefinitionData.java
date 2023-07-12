package ai.stapi.schema.structuredefinition;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.serialization.SerializableObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StructureDefinitionData implements SerializableObject {

  public static final String SERIALIZATION_TYPE = "StructureDefinition";

  private String id;
  private String url;
  private String status;
  private String description;
  private String kind;
  private Boolean isAbstract;
  private String type;
  private String baseDefinition;
  private UniqueIdentifier baseDefinitionRef;
  private Differential differential;

  protected StructureDefinitionData() {
  }

  public StructureDefinitionData(
      String id,
      String url,
      String status,
      String description,
      String kind,
      Boolean isAbstract,
      String type,
      String baseDefinition,
      UniqueIdentifier baseDefinitionRef,
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
    this.baseDefinitionRef = baseDefinitionRef;
    this.differential = differential;
  }

  public StructureDefinitionData(
      String id,
      String url,
      String status,
      String description,
      String kind,
      Boolean isAbstract,
      String type,
      String baseDefinition,
      UniqueIdentifier baseDefinitionRef,
      List<ElementDefinition> differential
  ) {
    this.id = id;
    this.url = url;
    this.status = status;
    this.description = description;
    this.kind = kind;
    this.isAbstract = isAbstract;
    this.type = type;
    this.baseDefinition = baseDefinition;
    this.baseDefinitionRef = baseDefinitionRef;
    this.differential = new Differential(differential);
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

  public Differential getDifferential() {
    return differential;
  }

  public UniqueIdentifier getBaseDefinitionRef() {
    return baseDefinitionRef;
  }

  public String getBaseDefinition() {
    return baseDefinition;
  }

  @Override
  @JsonIgnore
  public String getSerializationType() {
    return StructureDefinitionData.SERIALIZATION_TYPE;
  }

  @Override
  public String toString() {
    return "StructureDefinitionDTO{" +
        "id='" + id + '\'' +
        ", url='" + url + '\'' +
        ", status='" + status + '\'' +
        ", description='" + description + '\'' +
        ", kind='" + kind + '\'' +
        ", isAbstract=" + isAbstract +
        ", type='" + type + '\'' +
        ", baseDefinition=" + baseDefinitionRef +
        ", differential=" + differential +
        '}';
  }

  public static class Differential implements SerializableObject {

    public static final String SERIALIZATION_TYPE = "StructureDefinitionDifferential";
    private List<ElementDefinition> element;

    private Differential() {
    }

    public Differential(List<ElementDefinition> element) {
      this.element = element;
    }

    public List<ElementDefinition> getElement() {
      return element;
    }

    @Override
    public String toString() {
      return "Differential{" +
          "element=" + element +
          '}';
    }

    @Override
    public String getSerializationType() {
      return SERIALIZATION_TYPE;
    }
  }
}
