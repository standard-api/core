package ai.stapi.schema.structuredefinition;

import ai.stapi.identity.UniqueIdentifier;

public class StructureDefinitionId extends UniqueIdentifier {

  private StructureDefinitionId() {
  }

  public StructureDefinitionId(String id) {
    super(id);
  }
}