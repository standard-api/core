package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLoader.search.exceptions.SearchOptionResolverRuntimeException;

public class OneValueFilterOptionParameters<ValueType> extends AbstractLeafFilterOptionParameters {
    
    private final ValueType attributeValue;

    public OneValueFilterOptionParameters(
        PositiveGraphDescription attributeNamePath,
        ValueType attributeValue
    ) {
        super(attributeNamePath);
        this.attributeValue = attributeValue;
        if (!GraphDescriptionBuilder.isGraphDescriptionSinglePath(attributeNamePath)) {
            throw SearchOptionResolverRuntimeException.becauseGraphDescriptionInsideOneValueFilterOptionMustBeSinglePath();
        }
        if (!GraphDescriptionBuilder.isGraphDescriptionEndingWithAttributeOrUuidDescription(attributeNamePath)) {
            throw SearchOptionResolverRuntimeException.becauseGraphDescriptionInsideOneValueFilterOptionMustEndWithValueDescription();
        }
    }

    public OneValueFilterOptionParameters(String attributeName, ValueType attributeValue) {
        this(new AttributeQueryDescription(attributeName), attributeValue);
    }

    public ValueType getAttributeValue() {
        return attributeValue;
    }
}
