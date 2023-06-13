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
  private UniqueIdentifier baseDefinitionReference;
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
      UniqueIdentifier baseDefinitionReference,
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
    this.baseDefinitionReference = baseDefinitionReference;
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
      UniqueIdentifier baseDefinitionReference,
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
    this.baseDefinitionReference = baseDefinitionReference;
    this.differential = new Differential(differential, this.id);
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

  public UniqueIdentifier getBaseDefinitionReference() {
    return baseDefinitionReference;
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
        ", baseDefinition=" + baseDefinitionReference +
        ", differential=" + differential +
        '}';
  }

  public static class Differential implements SerializableObject {

    public static final String SERIALIZATION_TYPE = "StructureDefinitionDifferential";
    private List<ElementDefinition> element;
    private String parent;

    private Differential() {
    }

    public Differential(List<ElementDefinition> element, String parent) {
      this.element = element;
      this.parent = parent;
    }

    public List<ElementDefinition> getElement() {
      return element;
    }

    public String getParent() {
      return parent;
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
