package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graphsystem.genericGraphEventFactory.specific.SpecificGraphEventFactory;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.List;

public class StructureDefinitionImportedGraphEventFactory implements SpecificGraphEventFactory {

  @Override
  public AggregateGraphUpdatedEvent<? extends UniqueIdentifier> createEvent(
      UniqueIdentifier identity,
      Graph graph,
      List<GraphElementForRemoval> elementsForRemoval
  ) {
    return new StructureDefinitionImported((StructureDefinitionId) identity, graph);
  }

  @Override
  public boolean supports(
      Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>> event) {
    return event.equals(StructureDefinitionImported.class);
  }
}
