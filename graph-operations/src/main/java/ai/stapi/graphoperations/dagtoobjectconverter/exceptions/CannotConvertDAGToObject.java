package ai.stapi.graphoperations.dagtoobjectconverter.exceptions;

import ai.stapi.graph.traversableGraphElements.TraversableNode;

public class CannotConvertDAGToObject extends RuntimeException {

    private CannotConvertDAGToObject(String message) {
        super("Cannot convert DAG to Object, because " + message);
    }
    
    public static CannotConvertDAGToObject becauseItContainsCycle(TraversableNode twiceVisitedNode) {
        return new CannotConvertDAGToObject(
            String.format(
                "it contains cycle. Twice visited node: '%s/%s'",
                twiceVisitedNode.getType(),
                twiceVisitedNode.getId()
            )
        );
    }
}
