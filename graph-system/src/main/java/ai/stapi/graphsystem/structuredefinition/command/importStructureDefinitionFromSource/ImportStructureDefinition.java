package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;

public class ImportStructureDefinition extends AbstractCommand<StructureDefinitionId> {

  public static final String SERIALIZATION_TYPE = "ImportStructureDefinition";

  private RawStructureDefinitionData structureDefinitionSource;

  protected ImportStructureDefinition() {
  }

  public ImportStructureDefinition(
      StructureDefinitionId targetIdentifier,
      RawStructureDefinitionData structureDefinitionSource
  ) {
    super(targetIdentifier, ImportStructureDefinition.SERIALIZATION_TYPE);
    this.structureDefinitionSource = structureDefinitionSource;
  }

  public RawStructureDefinitionData getStructureDefinitionSource() {
    return structureDefinitionSource;
  }
}
