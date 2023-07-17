package ai.stapi.graphoperations.synchronization;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.Graph;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.inMemoryGraph.EdgeBuilder;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentifyingFiltersResolver;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.map.LinkedMap;

public class IdentifyingGraphSynchronizer implements GraphSynchronizer {

    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    private final NodeIdentifyingFiltersResolver nodeIdentifyingFiltersResolver;
    private final GraphLoader graphLoader;
    private final InMemoryGenericSearchOptionResolver searchOptionResolver;
    private final ObjectMapper objectMapper;
    private final StructureSchemaFinder structureSchemaFinder;
    
    public IdentifyingGraphSynchronizer(
        NodeRepository nodeRepository,
        EdgeRepository edgeRepository,
        NodeIdentifyingFiltersResolver nodeIdentifyingFiltersResolver,
        GraphLoader graphLoader,
        InMemoryGenericSearchOptionResolver searchOptionResolver,
        ObjectMapper objectMapper,
        StructureSchemaFinder structureSchemaFinder
    ) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.nodeIdentifyingFiltersResolver = nodeIdentifyingFiltersResolver;
        this.graphLoader = graphLoader;
        this.searchOptionResolver = searchOptionResolver;
        this.objectMapper = objectMapper;
        this.structureSchemaFinder = structureSchemaFinder;
    }

    @Override
    public void synchronize(Graph graph) throws GraphException {
        var newGraph = new InMemoryGraphRepository(graph);
        var idChanges = this.synchronizeNodes(newGraph);
        this.synchronizeEdges(newGraph, idChanges);
    }

    public InMemoryGraphRepository mergeDuplicateNodesByIdentificators(
        InMemoryGraphRepository graphWithDuplications
    ) {
        Map<UniqueIdentifier, Node> processedNodes = new LinkedMap<>();

        var resultingGraph = new InMemoryGraphRepository();
        var inMemoryGraphLoader = new InMemoryGraphLoader(
            resultingGraph, 
            this.searchOptionResolver, 
            this.structureSchemaFinder,
            this.objectMapper
        );
        for (Node mergingNode : graphWithDuplications.getGraph().getAllNodes()) {
            var identifyingQuery = this.nodeIdentifyingFiltersResolver.resolve(
                mergingNode,
                graphWithDuplications
            );
            var identfiedNodes = inMemoryGraphLoader.findAsTraversable(identifyingQuery);
            var sameTypeIdentifiedNodes = identfiedNodes.stream()
                .map(TraversableNode.class::cast)
                .map(Node::new)
                .filter(graphNode -> mergingNode.getType().equals(graphNode.getType()))
                .toList();
            
            if (sameTypeIdentifiedNodes.isEmpty()) {
                resultingGraph.save(mergingNode);
                processedNodes.put(
                    mergingNode.getId(),
                    mergingNode
                );
                continue;
            }
            if (sameTypeIdentifiedNodes.size() == 1) {
                var existingNode = sameTypeIdentifiedNodes.get(0);
                var newNode = (Node) existingNode.mergeAttributesWithAttributesOf(mergingNode);
                resultingGraph.replace(newNode);
                processedNodes.put(mergingNode.getId(), newNode);
                continue;
            }
            throw new GraphException(
                "Multiple nodes found by identificators. This should never happen. " +
                    "If occurs, it MUST BE investigated."
            );
        }

        List<Edge> fixedEdges = graphWithDuplications.loadAllEdges().stream().map(
            oldEdge ->
            {
                Node fixedNodeFrom = processedNodes.get(oldEdge.getNodeFromId());
                Node fixedNodeTo = processedNodes.get(oldEdge.getNodeToId());

                return new EdgeBuilder()
                    .setEdgeId(oldEdge.getId())
                    .setEdgeType(oldEdge.getType())
                    .setNodeFromId(fixedNodeFrom.getId())
                    .setNodeToId(fixedNodeTo.getId())
                    .setNodeFromType(fixedNodeFrom.getType())
                    .setNodeToType(fixedNodeTo.getType())
                    .setVersionedAttributes(oldEdge.getVersionedAttributes())
                    .create();
            }
        ).toList();

        List<Edge> mergedEdges = new ArrayList<>();
        fixedEdges.forEach(fixedEdge -> {
            List<Edge> foundEdges = mergedEdges.stream().filter(processedEdge ->
                processedEdge.getNodeFromId().equals(fixedEdge.getNodeFromId()) &&
                    processedEdge.getNodeToId().equals(fixedEdge.getNodeToId()) &&
                    processedEdge.getType().equals(fixedEdge.getType())
            ).toList();
            if (foundEdges.isEmpty()) {
                resultingGraph.save(fixedEdge);
                mergedEdges.add(fixedEdge);
                return;
            }
            if (foundEdges.size() == 1) {
                Edge edge =
                    (Edge) foundEdges.get(0).mergeAttributesWithAttributesOf(fixedEdge);
                resultingGraph.replace(edge);
                return;
            }
            throw new GraphException("Multiple edges found by end nodes and attributes.");
        });

        return resultingGraph.getGraph().traversable();
    }

    private LinkedMap<UniqueIdentifier, UniqueIdentifier> synchronizeNodes(
        InMemoryGraphRepository newGraph
    ) {
        var nodeIdChangesMap = new LinkedMap<UniqueIdentifier, UniqueIdentifier>();
        for (var node : newGraph.getGraph().getAllNodes()) {
            this.synchronizeNode(
                newGraph,
                node,
                nodeIdChangesMap
            );
        }
        return nodeIdChangesMap;
    }

    private void synchronizeNode(
        InMemoryGraphRepository newGraph,
        Node inMemoryNode,
        LinkedMap<UniqueIdentifier, UniqueIdentifier> nodeIdChangesMap
    ) {
        var identifyingQuery = this.nodeIdentifyingFiltersResolver.resolve(
            inMemoryNode,
            newGraph
        );
        var foundNodes = this.graphLoader.findAsTraversable(identifyingQuery);
        if (foundNodes.size() > 1) {
            throw new GraphException(
                "Multiple nodes found by identificators. This should never happen. " +
                    "If occurs, it MUST BE investigated."
            );
        }
        if (foundNodes.isEmpty()) {
            this.nodeRepository.save(
                new Node(
                    inMemoryNode.getId(),
                    inMemoryNode.getType(),
                    inMemoryNode.getVersionedAttributes()
                )
            );
            return;
        }
        var foundNode = (TraversableNode) foundNodes.get(0);
        foundNode = (TraversableNode) foundNode.mergeAttributesWithAttributesOf(inMemoryNode);
        this.nodeRepository.replace(
            new Node(
                foundNode.getId(),
                foundNode.getType(),
                foundNode.getVersionedAttributes()
            )
        );
        nodeIdChangesMap.put(
            inMemoryNode.getId(),
            foundNode.getId()
        );
    }

    private void synchronizeEdges(
        InMemoryGraphRepository newGraph,
        LinkedMap<UniqueIdentifier, UniqueIdentifier> nodeIdChangeMap
    ) {
        newGraph.getGraph().getAllEdges().forEach(
            potentiallyRottenEdge ->
            {
                var fixedNodeFromId = nodeIdChangeMap.getOrDefault(
                    potentiallyRottenEdge.getNodeFromId(),
                    potentiallyRottenEdge.getNodeFromId()
                );
                var fixedNodeFrom = new Node(fixedNodeFromId, potentiallyRottenEdge.getNodeFromType());
                var fixedNodeToId = nodeIdChangeMap.getOrDefault(
                    potentiallyRottenEdge.getNodeToId(),
                    potentiallyRottenEdge.getNodeToId()
                );
                var fixedNodeTo = new Node(fixedNodeToId, potentiallyRottenEdge.getNodeToType());
                var fixedEdge = new Edge(
                    potentiallyRottenEdge.getId(),
                    fixedNodeFrom,
                    potentiallyRottenEdge.getType(),
                    fixedNodeTo
                );
                fixedEdge = (Edge) fixedEdge.mergeAttributesWithAttributesOf(potentiallyRottenEdge);

                var foundEdge = edgeRepository.findEdgeByTypeAndNodes(
                    fixedEdge.getType(),
                    new NodeIdAndType(fixedEdge.getNodeFromId(), fixedEdge.getNodeFromType()),
                    new NodeIdAndType(fixedEdge.getNodeToId(), fixedEdge.getNodeToType())
                );
                if (foundEdge != null) {
                    foundEdge = (TraversableEdge) foundEdge.mergeAttributesWithAttributesOf(fixedEdge);
                    edgeRepository.replace(
                        new Edge(
                            foundEdge.getId(),
                            foundEdge.getType(),
                            new Node(foundEdge.getNodeFrom().getId(), foundEdge.getNodeFrom().getType()),
                            new Node(foundEdge.getNodeTo().getId(), foundEdge.getNodeTo().getType()),
                            foundEdge.getVersionedAttributes()
                        )
                    );
                } else {
                    edgeRepository.save(fixedEdge);
                }
            }
        );
    }
}
