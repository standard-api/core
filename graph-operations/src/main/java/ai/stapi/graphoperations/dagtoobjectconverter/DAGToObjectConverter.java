package ai.stapi.graphoperations.dagtoobjectconverter;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.dagtoobjectconverter.exceptions.CannotConvertDAGToObject;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DAGToObjectConverter {
    
    private static final String ID_FIELD_NAME = "id";

    private final StructureSchemaFinder structureSchemaFinder;

    public DAGToObjectConverter(StructureSchemaFinder structureSchemaFinder) {
        this.structureSchemaFinder = structureSchemaFinder;
    }

    public Map<String, Object> convert(TraversableNode startNode) throws CannotConvertDAGToObject {
        return new Convertor(this.structureSchemaFinder).convert(startNode);
    }

    private static class Convertor {

        private final NodeSet nodeSet;
        private final StructureSchemaFinder structureSchemaFinder;
        
        public Convertor(StructureSchemaFinder structureSchemaFinder) {
            this.nodeSet = new NodeSet();
            this.structureSchemaFinder = structureSchemaFinder;
        }

        public Map<String, Object> convert(TraversableNode startNode) throws CannotConvertDAGToObject {
            this.nodeSet.saveNode(startNode);
            var object = this.convertNodeToObject(startNode);
            startNode.getOutgoingEdges().forEach(edge -> {
                var nodeTo = edge.getNodeTo();
                if (this.nodeSet.hasNode(nodeTo)) {
                    throw CannotConvertDAGToObject.becauseItContainsCycle(nodeTo);
                }
                var fieldDefinition = this.structureSchemaFinder.getFieldDefinitionOrFallback(
                    startNode.getType(),
                    edge.getType()
                );
                if (fieldDefinition.isList()) {
                    var list = (List<Object>) object.computeIfAbsent(edge.getType(), key -> new ArrayList<>());
                    list.add(this.convert(nodeTo));
                } else {
                    object.put(edge.getType(), this.convert(nodeTo));
                }
            });
                
            return object;
        }

        @NotNull
        private HashMap<String, Object> convertNodeToObject(TraversableNode startNode) {
            var object = new HashMap<String, Object>(Map.of(
                ID_FIELD_NAME, startNode.getId().getId()
            ));
            startNode.getVersionedAttributeList().forEach(versionedAttribute -> {
                var current = versionedAttribute.getCurrent();
                object.put(current.getName(), current.getValue());
            });
            return object;
        }
    }

    private static class NodeSet {

        private final Map<String, Set<UniqueIdentifier>> visitedNodes;

        public NodeSet() {
            this.visitedNodes = new HashMap<>();
        }

        public void saveNode(TraversableNode node) {
            this.visitedNodes
                .computeIfAbsent(node.getType(), key -> new HashSet<>())
                .add(node.getId());
        }
        
        public boolean hasNode(TraversableNode node) {
            var nodesByType = this.visitedNodes.get(node.getType());
            return nodesByType != null && nodesByType.contains(node.getId());
        }
    }
}
