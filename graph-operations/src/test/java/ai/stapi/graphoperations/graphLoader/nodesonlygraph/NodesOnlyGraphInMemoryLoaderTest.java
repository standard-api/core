package ai.stapi.graphoperations.graphLoader.nodesonlygraph;

import ai.stapi.graphoperations.fixtures.AttributeTypes;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.repositorypruner.RepositoryPruner;
import ai.stapi.graphoperations.fixtures.NodesOnlyGraphFixturesProvider;
import ai.stapi.graphoperations.fixtures.model.GraphLoaderTestDefinitionsLoader;
import ai.stapi.graphoperations.synchronization.GraphSynchronizer;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoaderReturnType;
import ai.stapi.graphoperations.graphLoader.exceptions.GraphLoaderException;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AndFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.ContainsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EndsWithFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanOrEqualFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanOrEqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.OrFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.StartsWithFilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.AscendingSortOption;
import ai.stapi.graphoperations.graphLoader.search.sortOption.DescendingSortOption;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({
    GraphLoaderTestDefinitionsLoader.SCOPE,
    TestSystemModelDefinitionsLoader.SCOPE
})
class NodesOnlyGraphInMemoryLoaderTest extends SchemaIntegrationTestCase {

  @Autowired
  private InMemoryGraphLoader inMemoryGraphLoader;

  @BeforeAll
  public static void beforeAll(
      @Autowired GraphSynchronizer graphSynchronizer,
      @Autowired NodesOnlyGraphFixturesProvider nodesOnlyGraphFixturesProvider,
      @Autowired RepositoryPruner repositoryPruner
  ) {
    repositoryPruner.prune();
    graphSynchronizer.synchronize(nodesOnlyGraphFixturesProvider.getFixturesGraph());
  }

  @Test
  void itShouldFindAllWhenQuerySearchParamsAreEmpty() {
    var searchParam = SearchQueryParameters.from();
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldSortDescendingByQuantity() {
    var descendingSortOption = new DescendingSortOption(AttributeTypes.EXAMPLE_QUANTITY);
    var searchParam = SearchQueryParameters.from(descendingSortOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldSortByStringAttributeAscAndThanQuantityDesc() {
    var ascendingStringAttributeSortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE);
    var descendingNameSortOption = new DescendingSortOption(AttributeTypes.EXAMPLE_QUANTITY);
    var searchParam =
        SearchQueryParameters.from(ascendingStringAttributeSortOption, descendingNameSortOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldIgnoreSortOptionWithNonexistingAttributeName() {
    var nonexistingSortOption = new DescendingSortOption("nonexising_attribute_name");
    var searchParam = SearchQueryParameters.from(nonexistingSortOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldIgnoreSortOptionWithNonexistingAttributeNameAndThanByQuantityAsc() {
    var nonexistingSortOption = new DescendingSortOption("nonexising_attribute_name");
    var ascQuantitySortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_QUANTITY);
    var searchParam = SearchQueryParameters.from(nonexistingSortOption, ascQuantitySortOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldReturnEmptyListWhenFilterOptionWithNonexistingAttributeName() {
    var nonexistingFilterOption = new EqualsFilterOption<>(
        "nonexising_attribute_name",
        "irrelevant value"
    );
    var searchParam = SearchQueryParameters.from(nonexistingFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    Assertions.assertEquals(0, actual.size());
  }

  @Test
  void itShouldFilterWithEqualsFilterOption() {
    var equalsFilterOption = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var searchParam = SearchQueryParameters.from(equalsFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterWithGreaterThanFilterOption() {
    var equalsFilterOption = new GreaterThanFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        653
    );
    var searchParam = SearchQueryParameters.from(equalsFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterWithGreaterThanOrEqualsFilterOption() {
    var equalsFilterOption = new GreaterThanOrEqualFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        653
    );
    var searchParam = SearchQueryParameters.from(equalsFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterWithLowerThanFilterOption() {
    var equalsFilterOption = new LowerThanFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        653
    );
    var searchParam = SearchQueryParameters.from(equalsFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterWithLowerThanOrEqualFilterOption() {
    var equalsFilterOption = new LowerThanOrEqualsFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        653
    );
    var searchParam = SearchQueryParameters.from(equalsFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterWithEqualsFilterOptionAndSortByQuantityAsc() {
    var equalsFilterOption = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var quantitySortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_QUANTITY);
    var searchParam = SearchQueryParameters.from(equalsFilterOption, quantitySortOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByTwoFiltersAndSortByQuantityAsc() {
    var equalsFilterOption = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var greaterFilterOption = new GreaterThanFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        20
    );
    var quantitySortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_QUANTITY);
    var searchParam = SearchQueryParameters.from(
        equalsFilterOption, greaterFilterOption, quantitySortOption
    );
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByAndFilterOptions() {
    var equalsFilterOption = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var greaterFilterOption = new GreaterThanFilterOption<>(
        AttributeTypes.EXAMPLE_QUANTITY,
        20
    );
    var andFilterOption = new AndFilterOption(equalsFilterOption, greaterFilterOption);
    var searchParam = SearchQueryParameters.from(andFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByOrFilterOptions() {
    var equalsFilterOption1 = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var equalsFilterOption2 = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Unique Attribute Value"
    );
    var orFilterOption = new OrFilterOption(List.of(equalsFilterOption1, equalsFilterOption2));
    var searchParam = SearchQueryParameters.from(orFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByNotFilterOptions() {
    var equalsFilterOption = new EqualsFilterOption<>(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute Value"
    );
    var notFilterOption = new NotFilterOption(equalsFilterOption);
    var searchParam = SearchQueryParameters.from(notFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByLikeFilterOptions_WhenSearchedStringIsInMiddle() {
    var likeFilterOption = new ContainsFilterOption(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Other"
    );
    var searchParam = SearchQueryParameters.from(likeFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByLikeFilterOptions_WhenSearchedStringIsAtStart() {
    var likeFilterOption = new ContainsFilterOption(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute"
    );
    var searchParam = SearchQueryParameters.from(likeFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByLikeFilterOptions_WhenSearchedStringIsAtEnd() {
    var likeFilterOption = new ContainsFilterOption(
        AttributeTypes.EXAMPLE_NAME,
        "Name 4"
    );
    var searchParam = SearchQueryParameters.from(likeFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByStartsWithFilterOption() {
    var startsWithFilterOption = new StartsWithFilterOption(
        AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
        "Attribute"
    );
    var searchParam = SearchQueryParameters.from(startsWithFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldFilterByEndsWithFilterOption() {
    var endsWithFilterOption = new EndsWithFilterOption(
        AttributeTypes.EXAMPLE_NAME,
        "Name 4"
    );
    var searchParam = SearchQueryParameters.from(endsWithFilterOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldPaginateResult() {
    var paginationOption = new OffsetPaginationOption(2, 3);
    var ascendingSortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_NAME);
    var searchParam = SearchQueryParameters.from(ascendingSortOption, paginationOption);
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(
                NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldPaginateResultAndMapToObject() {
    var paginationOption = new OffsetPaginationOption(2, 2);
    var ascendingSortOption = new AscendingSortOption(AttributeTypes.EXAMPLE_NAME);
    var searchParam = SearchQueryParameters.from(ascendingSortOption, paginationOption);
    var actual = this.inMemoryGraphLoader.find(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
        ),
        Object.class,
        GraphLoaderReturnType.OBJECT
    );
    this.thenObjectApproved(actual.getData());
  }

  @Test
  void itShouldKeepOnlySpecifiedAttributes() {
    var actual = this.inMemoryGraphLoader.findAsTraversable(
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            SearchQueryParameters.from(),
            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME)
        )
    );
    this.thenUnsortedGraphElementsApproved(actual);
  }

  @Test
  void itShouldThrowExceptionWhenGettingNodeAndItsNotPresent() {
    var searchParam = SearchQueryParameters.from();
    Executable throwable = () -> this.inMemoryGraphLoader.getAsTraversable(
        UniversallyUniqueIdentifier.fromString("576a533b-5764-401b-876c-9df37461bf90"),
        new NodeQueryGraphDescription(
            new NodeDescriptionParameters(NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE),
            searchParam,
            new AllAttributesDescription()
        )
    );

    this.thenExceptionMessageApproved(GraphLoaderException.class, throwable);
  }
}
