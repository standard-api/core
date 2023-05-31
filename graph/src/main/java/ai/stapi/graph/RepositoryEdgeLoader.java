package ai.stapi.graph;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.identity.UniqueIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryEdgeLoader implements EdgeLoader {

    private final EdgeRepository edgeRepository;

    public RepositoryEdgeLoader(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    @Override
    public List<TraversableEdge> loadEdges(
        UniqueIdentifier nodeId,
        String nodeType,
        String edgeType
    ) {
        return this.edgeRepository.findInAndOutEdgesForNode(
                nodeId,
                nodeType
            ).stream()
            .filter(edge -> edge.getType().equals(edgeType))
            .collect(Collectors.toList());
    }

    @Override
    public List<TraversableEdge> loadEdges(UniqueIdentifier nodeId, String nodeType) {
        return new ArrayList<>(this.edgeRepository.findInAndOutEdgesForNode(
            nodeId,
            nodeType
        ));
    }

    @Override
    public int getIdlessHashCodeForEdgesOnNode(UniqueIdentifier nodeId, String nodeType) {
        var inAndOutEdgesForNode = this.edgeRepository.findInAndOutEdgesForNode(
            nodeId,
            nodeType
        );
        return inAndOutEdgesForNode.stream()
            .mapToInt(AbstractAttributeContainer::getIdlessHashCode)
            .sum();
    }
}
