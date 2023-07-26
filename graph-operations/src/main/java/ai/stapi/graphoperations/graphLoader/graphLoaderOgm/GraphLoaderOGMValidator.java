package ai.stapi.graphoperations.graphLoader.graphLoaderOgm;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.*;
import ai.stapi.graphoperations.graphLoader.graphLoaderOgm.exception.GraphLoaderOGMIsInvalid;
import ai.stapi.graphoperations.objectGraphLanguage.*;

public class GraphLoaderOGMValidator {

    private GraphLoaderOGMValidator() {
    }
    
    public static void validate(ObjectGraphMapping objectGraphMapping) {
        if (objectGraphMapping instanceof LeafObjectGraphMapping leaf) {
            GraphLoaderOGMValidator.validateLeaf(leaf);
            return;
        }
        if (objectGraphMapping instanceof ListObjectGraphMapping list) {
            GraphLoaderOGMValidator.validateList(list);
            return;
        }
        if (objectGraphMapping instanceof ObjectObjectGraphMapping object) {
            GraphLoaderOGMValidator.validateObject(object);
            return;
        }
        if (objectGraphMapping instanceof ReferenceObjectGraphMapping reference) {
            GraphLoaderOGMValidator.validateReference(reference);
            return;
        }
        if (objectGraphMapping instanceof InterfaceObjectGraphMapping inter) {
            GraphLoaderOGMValidator.validateInterface(inter);
            return;
        }
        throw GraphLoaderOGMIsInvalid.becauseUnexpectedOGMEncountered(objectGraphMapping);
    }

    private static void validateLeaf(LeafObjectGraphMapping leaf) {
        var graphDescription = leaf.getGraphDescription();
        if (graphDescription instanceof UuidIdentityDescription uuid) {
            GraphLoaderOGMValidator.validateUuid(uuid);
            return;
        }
        if (graphDescription instanceof AbstractAttributeDescription attribute) {
            GraphLoaderOGMValidator.validateLeafAttribute(attribute);
            return;
        }
        throw GraphLoaderOGMIsInvalid.becauseLeafOGMHasUnexpectedGraphDescription(leaf);
    }

    private static void validateList(ListObjectGraphMapping list) {
        var graphDescription = list.getGraphDescription();
        var childOGM = list.getChildObjectGraphMapping();
        if (graphDescription instanceof AbstractAttributeDescription attribute) {
            GraphLoaderOGMValidator.validateListAttribute(attribute);
            if (childOGM instanceof LeafObjectGraphMapping leaf) {
                var childDescription = leaf.getGraphDescription();
                if (childDescription instanceof NullGraphDescription) {
                    return;
                }
                if (childDescription instanceof AbstractAttributeValueDescription) {
                    return;
                }
                throw GraphLoaderOGMIsInvalid.becauseListChildLeafOgmHadUnexpectedGraphDescription(childDescription);
            }
            throw GraphLoaderOGMIsInvalid.becauseListOGMContainedAttributeDescriptionButNotChildLeafOGM(childOGM);
        }
        if (graphDescription instanceof NullGraphDescription) {
            if (
                childOGM instanceof ReferenceObjectGraphMapping
                || childOGM instanceof InterfaceObjectGraphMapping
                || childOGM instanceof ObjectObjectGraphMapping
            ) {
                GraphLoaderOGMValidator.validate(childOGM);
                return;
            }
        }
        throw GraphLoaderOGMIsInvalid.becauseListOGMHadUnexpectedGraphDescription(graphDescription);
    }

    private static void validateObject(ObjectObjectGraphMapping object) {
        
    }

    private static void validateReference(ReferenceObjectGraphMapping reference) {
        
    }

    private static void validateInterface(InterfaceObjectGraphMapping inter) {
        
    }

    private static void validateUuid(UuidIdentityDescription uuidIdentityDescription) {
        
    }

    private static void validateLeafAttribute(AbstractAttributeDescription attribute) {

    }

    private static void validateListAttribute(AbstractAttributeDescription attribute) {

    }
}
