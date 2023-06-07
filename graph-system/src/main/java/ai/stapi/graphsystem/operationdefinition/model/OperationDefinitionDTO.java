package ai.stapi.graphsystem.operationdefinition.model;

import ai.stapi.graphsystem.operationdefinition.exceptions.CannotMergeOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.exceptions.InvalidOperationDefinition;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class OperationDefinitionDTO {

  private String id;
  private String name;
  private String status;
  private String kind;
  private List<String> description;
  private String code;
  private List<String> resource;
  private boolean system;
  private boolean type;
  private boolean instance;
  private List<ParameterDTO> parameter;

  private OperationDefinitionDTO() {
  }

  public OperationDefinitionDTO(
      String id,
      String name,
      String status,
      String kind,
      String description,
      String code,
      List<String> resource,
      boolean system,
      boolean type,
      boolean instance,
      List<ParameterDTO> parameter
  ) {
    this(
        id,
        name,
        status,
        kind,
        List.of(description),
        code,
        resource,
        system,
        type,
        instance,
        parameter
    );
  }

  private OperationDefinitionDTO(
      String id,
      String name,
      String status,
      String kind,
      List<String> description,
      String code,
      List<String> resource,
      boolean system,
      boolean type,
      boolean instance,
      List<ParameterDTO> parameter
  ) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.kind = kind;
    this.description = description;
    this.code = code;
    this.resource = resource;
    this.system = system;
    this.type = type;
    this.instance = instance;
    this.parameter = parameter;
  }

  public static OperationDefinitionDTO bareBone(
      String id,
      List<ParameterDTO> parameter
  ) {
    return new OperationDefinitionDTO(
        id,
        null,
        null,
        null,
        "",
        null,
        List.of(),
        false,
        false,
        false,
        parameter
    );
  }

  public void addParameters(List<ParameterDTO> parameters) {
    this.parameter.addAll(parameters);
  }

  public OperationDefinitionDTO merge(OperationDefinitionDTO other) {
    if (!Objects.equals(this.getId(), other.getId())) {
      throw CannotMergeOperationDefinition.becauseSomeFieldIsNotSame(
          "id",
          this.getId(),
          other.getId()
      );
    }
    if (!Objects.equals(this.getCode(), other.getCode())) {
      throw CannotMergeOperationDefinition.becauseSomeFieldIsNotSame(
          "code",
          this.getCode(),
          other.getCode()
      );
    }
    if (!Objects.equals(this.getName(), other.getName())) {
      throw CannotMergeOperationDefinition.becauseSomeFieldIsNotSame(
          "name",
          this.getName(),
          other.getName()
      );
    }
    if (!Objects.equals(this.getStatus(), other.getStatus())) {
      throw CannotMergeOperationDefinition.becauseSomeFieldIsNotSame(
          "status",
          this.getStatus(),
          other.getStatus()
      );
    }
    if (!Objects.equals(this.getKind(), other.getKind())) {
      throw CannotMergeOperationDefinition.becauseSomeFieldIsNotSame(
          "kind",
          this.getKind(),
          other.getKind()
      );
    }
    var mergedResources = new HashSet<>(this.getResource());
    mergedResources.addAll(other.getResource());

    var mergedParameters = new ArrayList<>(this.getParameter());
    mergedParameters.addAll(other.getParameter());

    var deduplicatedParams = mergedParameters.stream()
        .collect(Collectors.groupingBy(ParameterDTO::getName))
        .values()
        .stream()
        .map(group -> group.stream().collect(Collectors.groupingBy(ParameterDTO::getType)))
        .map(group -> group.values().stream().map(
                typeGroup -> typeGroup.stream().reduce(ParameterDTO::merge).get()
            ).toList()
        ).flatMap(group -> {
          if (group.size() == 1) {
            return group.stream();
          }
          return group.stream().map(parameterDTO -> new ParameterDTO(
              String.format(
                  "%s%s",
                  parameterDTO.getName(),
                  StringUtils.capitalize(parameterDTO.getType())
              ),
              parameterDTO.getUse(),
              parameterDTO.getMin(),
              parameterDTO.getMax(),
              parameterDTO.getType(),
              parameterDTO.getReferencedFrom(),
              parameterDTO.getTargetProfileReference()
          ));
        }).toList();

    var mergedDescriptions = new ArrayList<>(this.description);
    mergedDescriptions.addAll(other.description);
    return new OperationDefinitionDTO(
        this.getId(),
        this.getName(),
        this.getStatus(),
        this.getKind(),
        mergedDescriptions,
        this.getCode(),
        mergedResources.stream().toList(),
        this.isSystem() || other.isSystem(),
        this.isType() || other.isType(),
        this.isInstance() || other.isInstance(),
        deduplicatedParams
    );
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public String getKind() {
    return kind;
  }

  public String getDescription() {
    if (this.description.isEmpty()) {
      return "";
    }
    if (this.description.size() == 1) {
      return this.description.get(0);
    }
    return String.format(
        "This automatic adding Operation was merged, because two ComplexType unionType members "
            + "had fieldName with same name, but different type. Original descriptions: %n%s",
        String.join("\n", this.description)
    );
  }

  private void setDescription(String description) {
    this.description = List.of(description);
  }

  public String getCode() {
    return code;
  }

  public List<String> getResource() {
    return resource;
  }

  public boolean isSystem() {
    return system;
  }

  public boolean isType() {
    return type;
  }

  public boolean isInstance() {
    return instance;
  }

  public List<ParameterDTO> getParameter() {
    return parameter;
  }

  @JsonIgnore
  public ParameterDTO getParameter(String parameterName) {
    return this.getParameter().stream()
        .filter(paramater -> paramater.getName().equals(parameterName))
        .findAny()
        .orElseThrow(
            () -> new InvalidOperationDefinition(
                "Parameter '%s' not found.".formatted(parameterName)
            )
        );
  }

  @Override
  public String toString() {
    return "OperationDefinitionDTO{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", status='" + status + '\'' +
        ", kind='" + kind + '\'' +
        ", description='" + description + '\'' +
        ", code='" + code + '\'' +
        ", resource=" + resource +
        ", system=" + system +
        ", type=" + type +
        ", instance=" + instance +
        ", parameter=" + parameter +
        '}';
  }

  public static class ParameterDTO {

    private String name;
    private String use;
    private Integer min;
    private String max;
    private String type;
    private List<ReferencedFrom> referencedFrom;
    private List<StructureDefinitionId> targetProfileReference;

    private ParameterDTO() {
    }

    private ParameterDTO(
        String name,
        String use,
        Integer min,
        String max,
        String type,
        List<ReferencedFrom> referencedFrom,
        List<StructureDefinitionId> targetProfileReference
    ) {
      this.name = name;
      this.use = use;
      this.min = min;
      this.max = max;
      this.type = type;
      this.referencedFrom = referencedFrom;
      this.targetProfileReference = targetProfileReference;
    }

    public ParameterDTO(
        String name,
        String use,
        Integer min,
        String max,
        String type,
        ReferencedFrom referencedFrom,
        List<StructureDefinitionId> targetProfileReference
    ) {
      this.name = name;
      this.use = use;
      this.min = min;
      this.max = max;
      this.type = type;
      this.referencedFrom = new ArrayList<>(List.of(referencedFrom));
      this.targetProfileReference = targetProfileReference;
    }

    public ParameterDTO merge(ParameterDTO other) {
      if (!Objects.equals(this.getName(), other.getName())) {
        throw CannotMergeOperationDefinition.becauseSomeParameterHasSomeFieldWhichIsNotSame(
            this.getName(),
            "name",
            this.getName(),
            other.getName()
        );
      }
      if (!Objects.equals(this.getUse(), other.getUse())) {
        throw CannotMergeOperationDefinition.becauseSomeParameterHasSomeFieldWhichIsNotSame(
            this.getName(),
            "use",
            this.getUse(),
            other.getUse()
        );
      }
      if (!Objects.equals(this.getMin(), other.getMin())) {
        throw CannotMergeOperationDefinition.becauseSomeParameterHasSomeFieldWhichIsNotSame(
            this.getName(),
            "min",
            this.getMin(),
            other.getMin()
        );
      }
      if (!Objects.equals(this.getMax(), other.getMax())) {
        throw CannotMergeOperationDefinition.becauseSomeParameterHasSomeFieldWhichIsNotSame(
            this.getName(),
            "max",
            this.getMax(),
            other.getMax()
        );
      }
      if (!Objects.equals(this.getType(), other.getType())) {
        throw CannotMergeOperationDefinition.becauseSomeParameterHasSomeFieldWhichIsNotSame(
            this.getName(),
            "type",
            this.getType(),
            other.getType()
        );
      }
      var mergedReferencedFrom = new ArrayList<>(this.getReferencedFrom());
      mergedReferencedFrom.addAll(other.getReferencedFrom());

      var mergedTargetProfileReferences = new ArrayList<>(this.getTargetProfileReference());
      mergedTargetProfileReferences.addAll(other.getTargetProfileReference());

      return new ParameterDTO(
          this.getName(),
          this.getUse(),
          this.getMin(),
          this.getMax(),
          this.getType(),
          mergedReferencedFrom.stream()
              .collect(Collectors.groupingBy(ReferencedFrom::getSource))
              .values()
              .stream()
              .map(group -> group.get(0))
              .toList(),
          mergedTargetProfileReferences.stream()
              .collect(Collectors.groupingBy(StructureDefinitionId::getId))
              .values()
              .stream()
              .map(group -> group.get(0))
              .toList()
      );
    }

    public String getName() {
      return name;
    }

    public String getUse() {
      return use;
    }

    public Integer getMin() {
      return min;
    }

    public String getMax() {
      return max;
    }

    public String getType() {
      return type;
    }

    public List<ReferencedFrom> getReferencedFrom() {
      return referencedFrom;
    }

    @JsonIgnore
    public ReferencedFrom getSingleReferencedFrom() {
      if (this.referencedFrom.size() != 1) {
        throw new InvalidOperationDefinition(
            String.format(
                "Every parameter should always have exactly one referencedFrom value.%n%s",
                String.format("Operation name: '%s'", this.name)
            )
        );
      }
      return referencedFrom.get(0);
    }

    public List<StructureDefinitionId> getTargetProfileReference() {
      return targetProfileReference;
    }

    @JsonIgnore
    public boolean isList() {
      try {
        var number = Integer.parseInt(this.max);
        return number > 1;
      } catch (RuntimeException ignore) {
      }
      return this.max.equals("*");
    }

    @Override
    public String toString() {
      return "Parameter{" +
          "name='" + name + '\'' +
          ", use='" + use + '\'' +
          ", min=" + min +
          ", max='" + max + '\'' +
          ", type=" + type +
          '}';
    }

    public static class ReferencedFrom {

      // Location in resource (into what the parameter gets mapped to) eg.: Patient.name
      private String source;

      private ReferencedFrom() {
      }

      public ReferencedFrom(String source) {
        this.source = source;
      }

      public String getSource() {
        return source;
      }
    }
  }
}
