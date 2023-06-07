package ai.stapi.graphsystem.aggregatedefinition.infrastructure;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.infrastructure.DatabaseOperationDefinitionProvider;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

public class DatabaseAggregateDefinitionProvider implements AggregateDefinitionProvider {

  private final GraphLoader graphLoader;
  private final DatabaseOperationDefinitionProvider databaseOperationDefinitionProvider;
  private List<AggregateDefinitionDTO> cachedAggregates;


  public DatabaseAggregateDefinitionProvider(
      GraphLoader graphLoader,
      DatabaseOperationDefinitionProvider databaseOperationDefinitionProvider
  ) {
    this.graphLoader = graphLoader;
    this.databaseOperationDefinitionProvider = databaseOperationDefinitionProvider;
    this.cachedAggregates = new ArrayList<>();
  }

  @Override
  public List<AggregateDefinitionDTO> provideAll() {
    this.cacheOperations();
    return this.cachedAggregates;
  }

  @Override
  public AggregateDefinitionDTO provide(
      String operationId
  ) throws CannotProvideOperationDefinition {
    this.cacheOperations();
    return this.cachedAggregates.stream()
        .filter(operation -> operation.getId().equals(operationId))
        .findFirst()
        .orElseThrow(() -> new CannotProvideOperationDefinition(operationId));
  }

  private void cacheOperations() {
    if (this.cachedAggregates.isEmpty()) {
      this.cachedAggregates = this.graphLoader.find(
          this.getAggregateGraphDescription(),
          AggregateDefinitionDTO.class
      ).getData();
    }
  }

  private NodeQueryGraphDescription getAggregateGraphDescription() {
    return new NodeQueryGraphDescription(
        new NodeDescriptionParameters("AggregateDefinition"),
        SearchQueryParameters.builder()
            .setPaginationOption(new OffsetPaginationOption(0, 50000))
            .build(),
        new UuidIdentityDescription(),
        new AttributeQueryDescription("name"),
        new AttributeQueryDescription("description"),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters("command"),
            SearchQueryParameters.builder()
                .setPaginationOption(new OffsetPaginationOption(0, 300))
                .build(),
            this.getCommandHandlerGraphDescription()
        ),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("structure"),
            new NodeDescription(
                new NodeDescriptionParameters("StructureDefinition"),
                new UuidIdentityDescription()
            )
        )
    );
  }

  @NotNull
  private NodeDescription getCommandHandlerGraphDescription() {
    return new NodeDescription(
        new NodeDescriptionParameters("CommandHandlerDefinition"),
        new AttributeQueryDescription("creationalPolicy"),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("operation"),
            this.databaseOperationDefinitionProvider.getOperationGraphDescription()
        ),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters("eventFactory"),
            SearchQueryParameters.builder()
                .setPaginationOption(new OffsetPaginationOption(0, 300))
                .build(),
            this.getEventFactoryGraphDescription()
        )
    );
  }

  @NotNull
  private NodeDescription getEventFactoryGraphDescription() {
    return new NodeDescription(
        "CommandHandlerDefinitionEventFactory",
        new UuidIdentityDescription(),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("event"),
            this.getEventMessageGraphDescription()
        ),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("modification"),
            new NodeDescription(
                "CommandHandlerDefinitionEventFactoryModification",
                new AttributeQueryDescription("kind"),
                new AttributeQueryDescription("modificationPath"),
                new AttributeQueryDescription("inputValueParameterName"),
                new AttributeQueryDescription("destinationIndexParameterName")
            )
        )
    );
  }

  @NotNull
  private NodeDescription getEventMessageGraphDescription() {
    return new NodeDescription(
        new NodeDescriptionParameters("EventMessageDefinition"),
        new AttributeQueryDescription("name"),
        new AttributeQueryDescription("description"),
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters("type"),
            new NodeDescription(
                new NodeDescriptionParameters("StructureDefinition")
            ),
            new UuidIdentityDescription()
        )
    );
  }
}
