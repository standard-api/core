package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graphoperations.synchronization.nodeIdentificator.exception.InvalidNodeIdentificator;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.test.base.UnitTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class NodeIdentificatorTest extends UnitTestCase {

    @Test
    void itWillThrowExceptionWhenProvidedIdentifyingPathStartsWithNode() {
        Executable executable = () -> new NodeIdentificator(new NodeDescription("irrelevant"));
        this.thenExceptionMessageApproved(InvalidNodeIdentificator.class, executable);
    }

    @Test
    void itWillThrowExceptionWhenProvidedIdentifyingPathIsNotSingleBranch() {
        Executable executable = () -> new NodeIdentificator(
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters("irrelevantEdge"),
                new AttributeQueryDescription("oneAttribute"),
                new AttributeQueryDescription("secondAttribute")
            )
        );
        this.thenExceptionMessageApproved(InvalidNodeIdentificator.class, executable);
    }

    @Test
    void itWillThrowExceptionWhenProvidedIdentifyingPathIsNotEndingWithValueDescription() {
        Executable executable = () -> new NodeIdentificator(
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters("irrelevantEdge")
            )
        );
        this.thenExceptionMessageApproved(InvalidNodeIdentificator.class, executable);
    }
}