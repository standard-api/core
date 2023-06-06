package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.exceptions.SpecificGraphWriterException;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import org.springframework.stereotype.Service;

@Service
public class AttributeGraphWriter implements SpecificGraphWriter {

    @Override
    public GraphBuilder write(
        GraphDescription graphDescription,
        GraphBuilder builder
    ) {
        var attributeDescription = (AbstractAttributeDescription) graphDescription;
        var parameters = (AttributeDescriptionParameters) attributeDescription.getParameters();
        var attributeBuilder = builder.addAttributeToLastElement();
        attributeBuilder
            .setAttributeName(parameters.getAttributeName())
            .setAttributeStructureType(attributeDescription.getDescribedAttributeStructureType());

        var attributeValuesDescriptions = attributeDescription.getChildGraphDescriptions()
            .stream()
            .filter(AbstractAttributeValueDescription.class::isInstance)
            .map(AbstractAttributeValueDescription.class::cast)
            .toList();

        attributeValuesDescriptions.forEach(valueDescription -> {
            var constant = valueDescription.getChildGraphDescriptions().stream()
                .filter(ConstantDescription.class::isInstance)
                .findAny()
                .orElseThrow(
                    () -> SpecificGraphWriterException.becauseProvidedAttributeValueDoesNotContainAnyConstantDescription(
                        valueDescription
                    )
                );

            var constantParameters = (ConstantDescriptionParameters) constant.getParameters();
            var value = constantParameters.getValue();
            attributeBuilder.addAttributeValue(
                new AttributeValueFactoryInput(
                    value,
                    valueDescription.getDescribedAttributeDataTypeId()
                )
            );
        });

        return builder;
    }

    @Override
    public boolean supports(GraphDescription graphDescription) {
        return graphDescription instanceof AbstractAttributeDescription;
    }
}
