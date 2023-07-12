package ai.stapi.graphsystem.operationdefinition.infrastructure;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperationDefinitionProvider implements OperationDefinitionProvider {

  private final GraphLoader graphLoader;
  private List<OperationDefinitionDTO> cachedOperations;


  public DatabaseOperationDefinitionProvider(GraphLoader graphLoader) {
    this.graphLoader = graphLoader;
    this.cachedOperations = new ArrayList<>();
  }

  @Override
  public List<OperationDefinitionDTO> provideAll() {
    this.cacheOperations();
    return this.cachedOperations;
  }

  @Override
  public OperationDefinitionDTO provide(
      String operationId
  ) throws CannotProvideOperationDefinition {
    this.cacheOperations();
    return this.cachedOperations.stream()
        .filter(operation -> operation.getId().equals(operationId))
        .findFirst()
        .orElseThrow(() -> new CannotProvideOperationDefinition(operationId));
  }

  @Override
  public boolean contains(String operationId) {
    this.cacheOperations();
    return this.cachedOperations.stream().anyMatch(
        operation -> operation.getId().equals(operationId)
    );
  }

  private void cacheOperations() {
    if (this.cachedOperations.isEmpty()) {
      this.cachedOperations = this.graphLoader.find(
          this.getOperationGraphDescription(),
          OperationDefinitionDTO.class
      ).getData();
    }
  }

  public NodeQueryGraphDescription getOperationGraphDescription() {
    return new NodeQueryGraphDescription(
        new NodeDescriptionParameters("OperationDefinition"),
        SearchQueryParameters.builder()
            .setPaginationOption(new OffsetPaginationOption(0, 50000))
            .build(),
        new UuidIdentityDescription(),
        new AttributeQueryDescription("name"),
        new AttributeQueryDescription("status"),
        new AttributeQueryDescription("kind"),
        new AttributeQueryDescription("description"),
        new AttributeQueryDescription("code"),
        new AttributeQueryDescription("resource"),
        new AttributeQueryDescription("system"),
        new AttributeQueryDescription("type"),
        new AttributeQueryDescription("instance"),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters("parameter"),
            SearchQueryParameters.builder()
                .setPaginationOption(new OffsetPaginationOption(0, 300))
                .build(),
            new NodeDescription(
                new NodeDescriptionParameters("OperationDefinitionParameter"),
                new AttributeQueryDescription("name"),
                new AttributeQueryDescription("use"),
                new AttributeQueryDescription("min"),
                new AttributeQueryDescription("max"),
                new AttributeQueryDescription("type"),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters("referencedFrom"),
                    SearchQueryParameters.builder()
                        .setPaginationOption(new OffsetPaginationOption(0, 300))
                        .build(),
                    new NodeDescription(
                        new NodeDescriptionParameters("OperationDefinitionParameterReferencedFrom"),
                        new AttributeQueryDescription("source")
                    )
                ),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters("targetProfile"),
                    SearchQueryParameters.builder()
                        .setPaginationOption(new OffsetPaginationOption(0, 300))
                        .build(),
                    new NodeDescription(
                        new NodeDescriptionParameters("StructureDefinition"),
                        new UuidIdentityDescription()
                    )
                )
            )
        )
    );
  }
}
