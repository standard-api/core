package ai.stapi.graphoperations.graphLoader.graphLoaderOgm.exception;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.utils.Stringifier;

public class GraphLoaderOGMIsInvalid extends RuntimeException {

    private GraphLoaderOGMIsInvalid(String becauseMessage) {
        super("OGM to be used in Graph Loader is invalid, because " + becauseMessage);
    }
    
    public static GraphLoaderOGMIsInvalid becauseLeafOGMHasUnexpectedGraphDescription(
        LeafObjectGraphMapping leafObjectGraphMapping
    ) {
        return new GraphLoaderOGMIsInvalid(
            String.format(
                "leaf OGM has unexpected graph description. " +
                    "It should contain either UUID or Attribute description.%nLeaf OGM: '%s'",
                Stringifier.convertToString(leafObjectGraphMapping)
            )
        );
    }
    
    public static GraphLoaderOGMIsInvalid becauseUnexpectedOGMEncountered(
        ObjectGraphMapping invalidOGM
    ) {
        return new GraphLoaderOGMIsInvalid(
            String.format(
                "unexpected OGM encountered. There should only be Leaf, List, Object, Reference, Interface OGMs." +
                    "%nInvalid OGM: '%s'",
                Stringifier.convertToString(invalidOGM)
            )
        ); 
    }
    
    public static GraphLoaderOGMIsInvalid becauseListOGMContainedAttributeDescriptionButNotChildLeafOGM(
        ObjectGraphMapping invalidChildOGM
    ) {
        return new GraphLoaderOGMIsInvalid(
            String.format(
                "list OGM contained attribute description, but child leaf OGM wasnt leaf." +
                    "%nInvalid child OGM: '%s'",
                Stringifier.convertToString(invalidChildOGM)
            )
        );
    }

    public static GraphLoaderOGMIsInvalid becauseListChildLeafOgmHadUnexpectedGraphDescription(
        GraphDescription childDescription
    ) {
        return new GraphLoaderOGMIsInvalid(
            String.format(
                "leaf OGM inside list OGM had unexpected graph description. It should either be null or attribute value."
                    + "%nInvalid description: '%s'",
                Stringifier.convertToString(childDescription)
            )
        );
    }

    public static GraphLoaderOGMIsInvalid becauseListOGMHadUnexpectedGraphDescription(GraphDescription graphDescription) {
        return new GraphLoaderOGMIsInvalid(
            String.format(
                "list OGM had unexpected graph description. It should either be null or attribute description."
                    + "%nInvalid description: '%s'",
                Stringifier.convertToString(graphDescription)
            )
        );
    }
}
