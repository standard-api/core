package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.exception.InvalidNodeIdentificator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NodeIdentificator implements Serializable {
    
    private final List<PositiveGraphDescription> graphDescriptions;
    
    public NodeIdentificator(String... attributeNames) {
        this(Arrays.stream(attributeNames).map(AttributeQueryDescription::new).collect(Collectors.toList()));
    }

    public NodeIdentificator(PositiveGraphDescription graphDescription) {
        this(List.of(graphDescription));
    }

    public NodeIdentificator(List<PositiveGraphDescription> graphDescriptions) {
        var invalidStart = graphDescriptions.stream()
            .filter(AbstractNodeDescription.class::isInstance)
            .toList();
        
        if (!invalidStart.isEmpty()) {
            throw InvalidNodeIdentificator.becausePathToIdentifiactionValueDoesntStartWithEdgeOrAttributeDescription(
                invalidStart
            );
        }
        var invalidBranching = graphDescriptions.stream()
            .filter(description -> !GraphDescriptionBuilder.isGraphDescriptionSinglePath(description))
            .toList();

        if (!invalidBranching.isEmpty()) {
            throw InvalidNodeIdentificator.becausePathToIdentifiactionValueIsNotSinglePath(
                invalidBranching
            );
        }
        var invalidEnd = graphDescriptions.stream()
            .filter(desc -> !GraphDescriptionBuilder.isGraphDescriptionEndingWithAttributeOrUuidDescription(desc))
            .toList();
        
        if (!invalidEnd.isEmpty()) {
            throw InvalidNodeIdentificator.becausePathToIdentifiactionValueIsNotEndingWithUuidIdentityOrAttributeDescription(
                invalidEnd
            );
        }
        this.graphDescriptions = graphDescriptions;
    }

    public List<PositiveGraphDescription> getGraphDescriptions() {
        return graphDescriptions;
    }
}
