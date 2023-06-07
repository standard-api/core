package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graph.Graph;
import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;

public class StructureDefinitionImported
    extends AggregateGraphUpdatedEvent<StructureDefinitionId> {

  public StructureDefinitionImported(
      StructureDefinitionId structureDefinitionId,
      Graph synchronizedGraph
  ) {
    super(structureDefinitionId, synchronizedGraph);
  }
}