package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public class NoValueFilterOptionParameters extends AbstractLeafFilterOptionParameters {

    public NoValueFilterOptionParameters(PositiveGraphDescription attributeNamePath) {
        super(attributeNamePath);
    }

    public NoValueFilterOptionParameters(String attributeName) {
        super(attributeName);
    }
}
