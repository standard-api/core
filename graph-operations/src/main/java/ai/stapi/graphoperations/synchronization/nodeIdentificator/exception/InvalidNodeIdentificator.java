package ai.stapi.graphoperations.synchronization.nodeIdentificator.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.utils.Stringifier;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidNodeIdentificator extends RuntimeException {

    private InvalidNodeIdentificator(String becauseMessage) {
        super("Node identificator was invalid, because " + becauseMessage);
    }
    
    public static InvalidNodeIdentificator becausePathToIdentifiactionValueDoesntStartWithEdgeOrAttributeDescription(
        List<PositiveGraphDescription> invalidPaths
    ) {
        return new InvalidNodeIdentificator(
            String.format(
                "path to identification value does not start with edge or attribute description. Invalid paths: %n%s",
                invalidPaths.stream().map(Stringifier::convertToString).collect(Collectors.joining("\n"))
            )
        );
    }

    public static InvalidNodeIdentificator becausePathToIdentifiactionValueIsNotSinglePath(
        List<PositiveGraphDescription> invalidPaths
    ) {
        return new InvalidNodeIdentificator(
            String.format(
                "path to identification value is not single path. Invalid paths: %n%s",
                invalidPaths.stream().map(Stringifier::convertToString).collect(Collectors.joining("\n"))
            )
        );
    }

    public static InvalidNodeIdentificator becausePathToIdentifiactionValueIsNotEndingWithUuidIdentityOrAttributeDescription(
        List<PositiveGraphDescription> invalidPaths
    ) {
        return new InvalidNodeIdentificator(
            String.format(
                "path to identification value is not ending with uuid identity or attribute description. " +
                    "Invalid paths: %n%s",
                invalidPaths.stream().map(Stringifier::convertToString).collect(Collectors.joining("\n"))
            )
        );
    }
}
