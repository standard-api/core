package ai.stapi.graphoperations.graphLoader.showcasegraph;

import ai.stapi.graphoperations.fixtures.ShowcaseGraphFixturesProvider;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graph.repositorypruner.RepositoryPruner;
import ai.stapi.graphoperations.synchronization.GraphSynchronizer;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.CollectionComparisonOperator;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoaderReturnType;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AllMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AnyMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.ContainsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EndsWithFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NoneMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AscendingSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.DescendingSortOption;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope(TestSystemModelDefinitionsLoader.SCOPE)
class ShowcaseGraphInMemoryLoaderTest extends SchemaIntegrationTestCase {

  @Autowired
  private InMemoryGraphLoader inMemoryGraphLoader;

  @BeforeAll
  @Order(2)
  public static void beforeAll(
      @Autowired GraphSynchronizer graphSynchronizer,
      @Autowired RepositoryPruner repositoryPruner,
      @Autowired ShowcaseGraphFixturesProvider showcaseGraphFixturesProvider
  ) {
    repositoryPruner.prune();
    graphSynchronizer.synchronize(showcaseGraphFixturesProvider.getFixtureGraph());
  }

  @Test
  void itShouldFilterListByAllFilterOption() {
    var allEquals = new AllMatchFilterOption(
        new EqualsFilterOption<>(
            "line",
            "28. rijna 50"
        )
    );
    var searchParam = SearchQueryParameters.from(allEquals);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters("Address"),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterListByAnyFilterOption() {
    var anyEndsWith = new AnyMatchFilterOption(
        new EndsWithFilterOption(
            "line",
            "1"
        )
    );
    var searchParam = SearchQueryParameters.from(anyEndsWith);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters("Address"),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterListByNoneFilterOption() {
    var noneContains = new NoneMatchFilterOption(
        new ContainsFilterOption(
            "line",
            "8"
        )
    );
    var searchParam = SearchQueryParameters.from(noneContains);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters("Address"),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenGraphElementsApproved(actual);
  }

  @Test
  void itShouldJoinComplexSortJoinedNodesAndMapIntoCompactObject() {
    var graphDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters("StructureDefinition"),
        SearchQueryParameters.from(),
        new UuidIdentityDescription(),
        new AttributeQueryDescription("kind"),
        new AttributeQueryDescription("abstract"),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters("differential"),
            new SearchQueryParameters(),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters("StructureDefinitionDifferential"),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters("element"),
                    SearchQueryParameters.builder()
                        .setPaginationOption(new OffsetPaginationOption(0, 100))
                        .addSortOption(
                            new AscendingSortOption(
                                new NodeDescription(
                                    new NodeDescriptionParameters("ElementDefinition"),
                                    new AttributeQueryDescription("path")
                                )
                            )
                        ).build(),
                    new NodeDescription(
                        new NodeDescriptionParameters("ElementDefinition"),
                        new UuidIdentityDescription(),
                        new AttributeQueryDescription("path"),
                        new AttributeQueryDescription("min"),
                        new AttributeQueryDescription("max")
                    )
                )
            )
        )
    );

    var actual = this.inMemoryGraphLoader.get(
        new UniqueIdentifier("CodeableConcept"),
        graphDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH,
        GraphLoaderReturnType.OBJECT
    );

    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldJoinAndFilterDeeplyByListAttribute() {
    var filter = new AllMatchFilterOption(
        new ContainsFilterOption(
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters("address"),
                SearchQueryParameters.from(),
                CollectionComparisonOperator.ANY,
                new NodeDescription(
                    new NodeDescriptionParameters("Address"),
                    new AttributeQueryDescription("line")
                )
            ),
            "c"
        )
    );
    var searchQueryParameters = SearchQueryParameters.builder()
        .addFilterOption(filter)
        .build();
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters("Organization"),
            searchQueryParameters,
            new AttributeQueryDescription("name"),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters("address"),
                SearchQueryParameters.builder()
                    .addSortOption(
                        new AscendingSortOption(
                            new NodeDescription(
                                new NodeDescriptionParameters("Address"),
                                new AttributeQueryDescription("line")
                            )
                        )
                    ).build(),
                new NodeDescription(
                    new NodeDescriptionParameters("Address"),
                    new AttributeQueryDescription("country"),
                    new AttributeQueryDescription("line")
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldJoinDeeperAndSortSecondNodeWithDeepSort() {
    var innerSort = new AscendingSortOption(
        new NodeDescription(
            new NodeDescriptionParameters("ElementDefinitionType"),
            new AttributeQueryDescription("code")
        )
    );
    var sort = new DescendingSortOption(
        new NodeDescription(
            new NodeDescriptionParameters("ElementDefinition"),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters("type"),
                SearchQueryParameters.from(innerSort),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters("ElementDefinitionType"),
                    SearchQueryParameters.from(),
                    new AttributeQueryDescription("code")
                )
            )
        )
    );
    var secondSort = new DescendingSortOption(
        new NodeDescription(
            new NodeDescriptionParameters("ElementDefinition"),
            new AttributeQueryDescription("path")
        )
    );
    var searchQueryParameters = SearchQueryParameters.builder()
        .addSortOption(sort)
        .addSortOption(secondSort)
        .build();

    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters("StructureDefinition"),
            SearchQueryParameters.builder()
                .addSortOption(new AscendingSortOption("type"))
                .setPaginationOption(new OffsetPaginationOption(0, 20))
                .build(),
            new AttributeQueryDescription("type"),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters("differential"),
                SearchQueryParameters.from(),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters("StructureDefinitionDifferential"),
                    SearchQueryParameters.from(),
                    new OutgoingEdgeQueryDescription(
                        new EdgeDescriptionParameters("element"),
                        searchQueryParameters,
                        new NodeDescription(
                            new NodeDescriptionParameters("ElementDefinition"),
                            new AttributeQueryDescription("path"),
                            new AttributeQueryDescription("min"),
                            new AttributeQueryDescription("max"),
                            new OutgoingEdgeQueryDescription(
                                new EdgeDescriptionParameters("type"),
                                SearchQueryParameters.from(innerSort),
                                new NodeDescription(
                                    new NodeDescriptionParameters("ElementDefinitionType"),
                                    new AttributeQueryDescription("code")
                                )
                            )
                        )
                    )
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }
}
