package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AndFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOptionParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.OrFilterOption;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphoperations.graphReader.GraphReader;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class NodeIdentifyingFiltersResolver {

    private final GenericNodeIdentificatorsProvider identificatorsProvider;
    private final GraphReader graphReader;

    public NodeIdentifyingFiltersResolver(
        GenericNodeIdentificatorsProvider identificatorsProvider,
        GraphReader graphReader
    ) {
        this.identificatorsProvider = identificatorsProvider;
        this.graphReader = graphReader;
    }

    public NodeQueryGraphDescription resolve(
        Node node,
        InMemoryGraphRepository contextGraph
    ) {
        var filters = this.resolveFilters(node, contextGraph);
        return new NodeQueryGraphDescription(
            new NodeDescriptionParameters(node.getType()),
            SearchQueryParameters.builder()
                .addFilterOption(filters.size() == 1 ? filters.get(0) : new OrFilterOption(filters))
                .setPaginationOption(new OffsetPaginationOption(0, 2))
                .build(),
            new AllAttributesDescription()
        );
    }

    private List<FilterOption<?>> resolveFilters(Node node, InMemoryGraphRepository contextGraph) {
        var identificators = this.identificatorsProvider.provide(node.getType());
        return identificators.stream()
            .map(NodeIdentificator::getGraphDescriptions)
            .map(descriptions -> this.readByDescriptions(node, contextGraph, descriptions))
            .filter(readOutputs -> readOutputs.stream().noneMatch(ReadOutput::isEmpty))
            .map(this::mapToFilterOption)
            .collect(Collectors.toList());
    }

    @NotNull
    private List<ReadOutput> readByDescriptions(
        Node node,
        InMemoryGraphRepository contextGraph,
        List<PositiveGraphDescription> descriptions
    ) {
        return descriptions.stream().map(
            description -> {
                var wrappedDescription = new NodeDescription(
                    new NodeDescriptionParameters(node.getType()),
                    description
                );
                return new ReadOutput(
                    this.graphReader.readValues(node.getId(), wrappedDescription, contextGraph),
                    description
                );
            }
        ).toList();
    }

    private FilterOption<?> mapToFilterOption(List<ReadOutput> readOutputs) {
        if (readOutputs.isEmpty()) {
            throw new RuntimeException(
                "Should not ever happen, because there should be at least default id node identifier."
            );
        }
        if (readOutputs.size() == 1) {
            return this.mapReadOutput(readOutputs.get(0));
        }
        return new AndFilterOption(
            readOutputs.stream().map(this::mapReadOutput).collect(Collectors.toList())
        );
    }

    @NotNull
    private FilterOption<? extends FilterOptionParameters> mapReadOutput(ReadOutput readOutPut) {
        if (readOutPut.getReadValues().size() == 1) {
            return new EqualsFilterOption<>(
                readOutPut.getReadGraphDescription(),
                readOutPut.getReadValues().get(0)
            );
        }
        return new AndFilterOption(
            readOutPut.getReadValues().stream().map(
                readValue -> new EqualsFilterOption<>(
                    readOutPut.getReadGraphDescription(),
                    readValue
                )
            ).collect(Collectors.toList())
        );
    }

    private static class ReadOutput {

        private final List<Object> readValues;
        private final PositiveGraphDescription readGraphDescription;

        public ReadOutput(List<Object> readValues, PositiveGraphDescription readGraphDescription) {
            this.readValues = readValues;
            this.readGraphDescription = readGraphDescription;
        }

        public boolean isEmpty() {
            return this.readValues.isEmpty();
        }

        public List<Object> getReadValues() {
            return readValues;
        }

        public PositiveGraphDescription getReadGraphDescription() {
            return readGraphDescription;
        }
    }
}
