package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;

public class FixedImportStructureDefinition extends AbstractCommand<StructureDefinitionId> {

  public static final String SERIALIZATION_TYPE = "c53c63da-d465-4299-82f5-5e8c92b0cb2c";
  private StructureDefinitionData structureDefinitionData;

  protected FixedImportStructureDefinition() {
  }

  public FixedImportStructureDefinition(
      StructureDefinitionId targetIdentifier,
      StructureDefinitionData structureDefinitionData
  ) {
    super(targetIdentifier, FixedImportStructureDefinition.SERIALIZATION_TYPE);
    this.structureDefinitionData = structureDefinitionData;
  }

  public StructureDefinitionData getStructureDefinitionSource() {
    return structureDefinitionData;
  }
}
