package ai.stapi.graphoperations.graphLoader.complexjoinedgraph;

import ai.stapi.graphoperations.fixtures.AttributeTypes;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.repositorypruner.RepositoryPruner;
import ai.stapi.graphoperations.fixtures.ComplexJoinedGraphFixturesProvider;
import ai.stapi.graphoperations.fixtures.GraphLoaderExpectedTestClass;
import ai.stapi.graphoperations.fixtures.model.GraphLoaderTestDefinitionsLoader;
import ai.stapi.graphoperations.synchronization.GraphSynchronizer;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.CollectionComparisonOperator;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoaderReturnType;
import ai.stapi.graphoperations.graphLoader.exceptions.GraphLoaderException;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AndFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanOrEqualFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotNullFilterOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AscendingSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.DescendingSortOption;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({
    GraphLoaderTestDefinitionsLoader.SCOPE,
    TestSystemModelDefinitionsLoader.SCOPE
})
class ComplexJoinedGraphInMemoryGraphLoaderTest extends SchemaIntegrationTestCase {

  @Autowired
  private InMemoryGraphLoader inMemoryGraphLoader;

  @BeforeAll
  public static void beforeAll(
      @Autowired GraphSynchronizer graphSynchronizer,
      @Autowired ComplexJoinedGraphFixturesProvider complexJoinedGraphFixturesProvider,
      @Autowired RepositoryPruner repositoryPruner
  ) {
    repositoryPruner.prune();
    graphSynchronizer.synchronize(complexJoinedGraphFixturesProvider.getFixturesGraph());
  }

  @Test
  void itShouldJoinEdgeOnlyWithSpecifiedTypeAndDirection() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new IngoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                new SearchQueryParameters(),
                new AllAttributesDescription()
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldJoinEdgeWithOnlySpecifiedAttributes() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                new SearchQueryParameters(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldJoinEdgeWithOnlySpecifiedFilters() {
    var searchQueryParameters = SearchQueryParameters.from(
        new EqualsFilterOption<>(AttributeTypes.EXAMPLE_NAME, "Example Edge Name 3")
    );
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                searchQueryParameters,
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  //TODO: Rename
  void itShouldJoinAllEdgesRegardlessOfTypeOfChildNodeDescription() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription()
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldJoinEdgeAndOtherNodeWithOnlySpecifiedAttributes() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AllAttributesDescription(),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  //TODO: rename
  void itShouldJoinEdgeAndOtherNodeWhichSatisfiesProvidedSearchOptionsRegardlessOfSpecifiedNodeTypeInDescription() {
    var searchQueryParameters = SearchQueryParameters.from(
        new GreaterThanFilterOption<>(
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
            ), 
            39
        )
    );
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                searchQueryParameters,
                new AllAttributesDescription(),
                new NodeDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    new AllAttributesDescription()
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldJoinDeeper() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AllAttributesDescription(),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription(),
                    new OutgoingEdgeQueryDescription(
                        new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new SearchQueryParameters(),
                        new AllAttributesDescription(),
                        new NodeQueryGraphDescription(
                            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3)
                        )
                    )
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldHandleMoreJoins() {
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            SearchQueryParameters.from(),
            new AllAttributesDescription(),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription()
                ),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2V2),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription()
                )
            ),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(
                    ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1_OTHER),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2_OTHER),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription()
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );
    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldGetOnlyMainRequestedNodeWhenNothingSet() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.getAsTraversable(
        ComplexJoinedGraphFixturesProvider.MAIN_NODE_UUID_1,
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchParam
        )
    );

    this.thenGraphElementApproved(actual);
  }

  @Test
  void itShouldGetNodeWithSpecifiedAttributes() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.getAsTraversable(
        ComplexJoinedGraphFixturesProvider.MAIN_NODE_UUID_1,
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
        )
    );

    this.thenGraphElementApproved(actual);
  }

  @Test
  void itShouldGetWithJoins() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_NODE_UUID_1,
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription(),
                    new OutgoingEdgeQueryDescription(
                        new EdgeDescriptionParameters(
                            ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new SearchQueryParameters(),
                        new AllAttributesDescription(),
                        new NodeQueryGraphDescription(
                            new NodeDescriptionParameters(
                                ComplexJoinedGraphFixturesProvider.NODE_TYPE_3)
                        )
                    )
                )
            ),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(
                    ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1_OTHER),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2_OTHER),
                    SearchQueryParameters.from(),
                    new AllAttributesDescription()
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );

    this.thenGraphApproved(actual.getGraph());
  }

  @Test
  void itShouldThrowExceptionWhenGettingEdgeAndItsNotPresent() {
    var searchParam = SearchQueryParameters.from();
    Executable throwable = () -> this.inMemoryGraphLoader.getAsTraversable(
        UniversallyUniqueIdentifier.fromString("576a533b-5764-401b-876c-9df37461bf90"),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AllAttributesDescription()
        )
    );

    this.thenExceptionMessageApproved(GraphLoaderException.class, throwable);
  }

  @Test
  void itShouldGetOnlyMainRequestedEdgeWhenNothingSet() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_EDGE_UUID_1,
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AllAttributesDescription()
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );

    this.thenGraphApproved(actual.getGraph());
  }

  @Test
  void itShouldGetEdgeWithMoreNodeDescriptionsAndMapToObject() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_EDGE_UUID_1,
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
            ),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );

    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldGetEdgeWithAttributesAndNeighborhood() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_EDGE_UUID_1,
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                searchParam,
                new AllAttributesDescription(),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    searchParam,
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );

    this.thenGraphApproved(actual.getGraph());
  }

  @Test
  void itShouldFoundEdgesAsFirst() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenGraphElementsApproved(actual);
  }

  @Test
  void itShouldFindEdgesWithAttributesAndNeighborhood() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.find(
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                searchParam,
                new AllAttributesDescription(),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    searchParam,
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.GRAPH
    );

    this.thenGraphApproved(actual.getGraphLoaderFindAsGraphOutput().getGraph());
  }

  @Test
  void itShouldJoinComplexAndMapIntoObject() {
    var graphDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
        SearchQueryParameters.from(),
        new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
        new UuidIdentityDescription(),
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new SearchQueryParameters(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new UuidIdentityDescription(),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2V2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new UuidIdentityDescription()
            ),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new UuidIdentityDescription(),
                OutgoingEdgeQueryDescription.asConnections(
                    new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new SearchQueryParameters(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new UuidIdentityDescription(),
                    new NodeQueryGraphDescription(
                        new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                        new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                        new UuidIdentityDescription()
                    )
                )
            )
        )
    );

    var actual = this.inMemoryGraphLoader.find(
        graphDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH,
        GraphLoaderReturnType.OBJECT
    );

    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldJoinComplexAndMapIntoCompactObject() {
    var graphDescription = new NodeQueryGraphDescription(
        new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
        SearchQueryParameters.from(),
        new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
        new UuidIdentityDescription(),
        new OutgoingEdgeQueryDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new SearchQueryParameters(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new UuidIdentityDescription(),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2V2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new UuidIdentityDescription()
            ),
            new NodeQueryGraphDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new UuidIdentityDescription(),
                new OutgoingEdgeQueryDescription(
                    new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new SearchQueryParameters(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new UuidIdentityDescription(),
                    new NodeQueryGraphDescription(
                        new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                        new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                        new UuidIdentityDescription()
                    )
                )
            )
        )
    );

    var actual = this.inMemoryGraphLoader.find(
        graphDescription,
        Object.class,
        GraphLoaderReturnType.GRAPH,
        GraphLoaderReturnType.OBJECT
    );

    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldFindEdgesWithAttributesAndNeighborhoodAndMapToObject() {
    var actual = this.inMemoryGraphLoader.find(
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new SearchQueryParameters(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                OutgoingEdgeQueryDescription.asConnections(
                    new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new SearchQueryParameters(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldGetEdgeJoinedWithNodeAndMapToObject() {
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_EDGE_UUID_1,
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new SearchQueryParameters(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldBeAbleToReturnSpecifiedClass() {
    var actual = this.inMemoryGraphLoader.get(
        ComplexJoinedGraphFixturesProvider.MAIN_EDGE_UUID_1,
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new SearchQueryParameters(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
            )
        ),
        GraphLoaderExpectedTestClass.class,
        GraphLoaderReturnType.OBJECT
    );
    Assertions.assertInstanceOf(GraphLoaderExpectedTestClass.class, actual.getData());
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itCanFindNodesAndApplyDeepSort() {
    var searchQueryParameters = SearchQueryParameters.builder().addSortOption(
        new DescendingSortOption(
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                new NodeDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                )
            )
        )
    ).build();

    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itCanFindNodesFilteredByDeepFilter() {
    var searchQueryParameters = SearchQueryParameters.builder().addFilterOption(
        new EqualsFilterOption<>(
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new NodeDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
                )
            ),
            "Deep From Middle"
        )
    ).build();

    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldFindEdgesAdnApplyDeepSort() {
    var sort = new AscendingSortOption(
        new NodeDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
        )
    );
    var searchQueryParameters = SearchQueryParameters.builder().addSortOption(sort).build();
    var actual = this.inMemoryGraphLoader.find(
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                OutgoingEdgeQueryDescription.asConnections(
                    new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new SearchQueryParameters(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldFindEdgesAndFilterThemByDeepFilter() {
    var filter = new GreaterThanOrEqualFilterOption<>(
        new NodeDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
        ),
        40
    );
    var searchQueryParameters = SearchQueryParameters.builder().addFilterOption(filter).build();
    var actual = this.inMemoryGraphLoader.find(
        OutgoingEdgeQueryDescription.asConnections(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                OutgoingEdgeQueryDescription.asConnections(
                    new EdgeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new SearchQueryParameters(),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                    new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                )
            )
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldJoinDeeperAndApplyDeepSortOverTwoEdges() {
    var sort = new DescendingSortOption(
        new OutgoingEdgeDescription(
            new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
            new NodeDescription(
                new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                new OutgoingEdgeDescription(
                    new EdgeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                    new NodeDescription(
                        new NodeDescriptionParameters(
                            ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                        new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                    )
                )
            )
        )
    );
    var searchQueryParameters = SearchQueryParameters.builder().addSortOption(sort).build();
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(
                        ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new OutgoingEdgeQueryDescription(
                        new EdgeDescriptionParameters(
                            ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new SearchQueryParameters(),
                        new NodeQueryGraphDescription(
                            new NodeDescriptionParameters(
                                ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                            new AttributeQueryDescription(
                                AttributeTypes.EXAMPLE_QUANTITY),
                            new AttributeQueryDescription(
                                AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
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

  @Test
  void itShouldJoinDeeperAndFilterThemByValueDeepOverTwoEdges() {
    var filter = new AndFilterOption(
        new LowerThanFilterOption<>(
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                new NodeDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    new OutgoingEdgeDescription(
                        new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new NodeDescription(
                            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                        )
                    )
                )
            ),
            65
        ),
        new NotNullFilterOption(
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                new NodeDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    new OutgoingEdgeDescription(
                        new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new NodeDescription(
                            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY)
                        )
                    )
                )
            )
        )
    );
    var searchQueryParameters = SearchQueryParameters.builder().addFilterOption(filter).build();
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_1),
            searchQueryParameters,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
            new OutgoingEdgeQueryDescription(
                new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_1),
                SearchQueryParameters.from(),
                new NodeQueryGraphDescription(
                    new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_2),
                    SearchQueryParameters.from(),
                    new OutgoingEdgeQueryDescription(
                        new EdgeDescriptionParameters(ComplexJoinedGraphFixturesProvider.EDGE_TYPE_2),
                        new SearchQueryParameters(),
                        CollectionComparisonOperator.ANY,
                        new NodeQueryGraphDescription(
                            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
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
