package ai.stapi.graphsystem.operationdefinition.model;

import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class FieldDefinitionWithSource extends FieldDefinition {

  private final String source;

  public FieldDefinitionWithSource(
      String name,
      Integer min,
      String max,
      String description,
      List<FieldType> types,
      String parentDefinitionType,
      String source
  ) {
    super(name, min, max, description, types, parentDefinitionType);
    this.source = source;
  }

  @JsonIgnore
  public String getSource() {
    return source;
  }

  @JsonIgnore
  public String[] getSplitSource() {
    return source.split("\\.");
  }

  @JsonIgnore
  public int getSourceLength() {
    return this.getSplitSource().length;
  }

  @JsonIgnore
  public String getLastSourcePath() {
    return this.getSplitSource()[this.getSourceLength() - 1];
  }
}
