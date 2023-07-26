package ai.stapi.graphoperations.graphLoader.graphLoaderOgm;

import ai.stapi.graphoperations.fixtures.AttributeTypes;
import ai.stapi.graphoperations.fixtures.ComplexJoinedGraphFixturesProvider;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.sortOption.DescendingSortOption;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GraphLoaderOgmFactoryTest extends IntegrationTestCase {

    @Autowired
    private GraphLoaderOgmFactory graphLoaderOgmFactory;

    @Test
    void itCanMapComplexGraphDescription() {
        var sort = new DescendingSortOption(
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
        );
        var searchQueryParameters = SearchQueryParameters.builder().addSortOption(sort).build();
        var graphDescription = new NodeQueryGraphDescription(
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
                        new NodeQueryGraphDescription(
                            new NodeDescriptionParameters(ComplexJoinedGraphFixturesProvider.NODE_TYPE_3),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_NAME),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_QUANTITY),
                            new AttributeQueryDescription(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE)
                        )
                    )
                )
            )
        );
        var actual = this.graphLoaderOgmFactory.create(graphDescription);
        this.thenObjectApproved(actual);
    }
}