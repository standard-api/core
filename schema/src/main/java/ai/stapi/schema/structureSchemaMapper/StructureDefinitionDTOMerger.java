package ai.stapi.schema.structureSchemaMapper;

import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.ArrayList;
import java.util.List;

public class StructureDefinitionDTOMerger {

  private StructureDefinitionDTOMerger() {
  }

  public static StructureDefinitionData merge(
      StructureDefinitionData slave,
      StructureDefinitionData master
  ) {
    if (!slave.getId().equals(master.getId())) {
      throw new RuntimeException(
          "Cannot merge two different structure definitions." +
              "\nSlave id: " + slave.getId() +
              "Master id: " + master.getId()
      );
    }
    var masterDifferential = master.getDifferential().getElement();
    var slaveDifferential = slave.getDifferential().getElement();

    var mergedDifferential = new ArrayList<>(masterDifferential);
    slaveDifferential.stream()
        .filter(
            slaveElement -> !StructureDefinitionDTOMerger.isContainedInMaster(masterDifferential,
                slaveElement))
        .forEach(mergedDifferential::add);

    return new StructureDefinitionData(
        master.getId() != null ? master.getId() : slave.getId(),
        master.getUrl() != null ? master.getUrl() : slave.getUrl(),
        master.getStatus() != null ? master.getStatus() : slave.getStatus(),
        master.getDescription() != null ? master.getDescription() : slave.getDescription(),
        master.getKind() != null ? master.getKind() : slave.getKind(),
        master.getIsAbstract() != null ? master.getIsAbstract() : slave.getIsAbstract(),
        master.getType() != null ? master.getType() : slave.getType(),
        master.getBaseDefinition() != null ? master.getBaseDefinition() : slave.getBaseDefinition(),
        master.getBaseDefinitionReference() != null ? master.getBaseDefinitionReference()
            : slave.getBaseDefinitionReference(),
        mergedDifferential
    );
  }

  private static boolean isContainedInMaster(List<ElementDefinition> masterDifferential,
      ElementDefinition slaveElement) {
    return masterDifferential.stream().anyMatch(
        masterElement -> masterElement.getPath().equals(slaveElement.getPath())
    );
  }
}