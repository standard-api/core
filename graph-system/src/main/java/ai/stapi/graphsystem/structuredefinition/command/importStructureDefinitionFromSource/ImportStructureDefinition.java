package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class ImportStructureDefinition extends AbstractCommand<StructureDefinitionId> {

  public static final String SERIALIZATION_TYPE = "ImportStructureDefinition";

  private Map<String, Object> structureDefinitionSource;

  protected ImportStructureDefinition() {
  }

  public ImportStructureDefinition(
      StructureDefinitionId targetIdentifier,
      Map<String, Object> structureDefinitionSource
  ) {
    super(targetIdentifier, ImportStructureDefinition.SERIALIZATION_TYPE);
    this.structureDefinitionSource = structureDefinitionSource;
  }

  public ImportStructureDefinition(
      StructureDefinitionId targetIdentifier,
      RawStructureDefinitionData structureDefinitionSource
  ) {
    super(targetIdentifier, ImportStructureDefinition.SERIALIZATION_TYPE);
    this.structureDefinitionSource = new ObjectMapper().convertValue(
        structureDefinitionSource,
        new TypeReference<>() {
        }
    );
  }

  public Map<String, Object> getStructureDefinitionSource() {
    return structureDefinitionSource;
  }
}
