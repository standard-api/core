package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;

public abstract class AbstractAttributeGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {
    parameters.addField("attributeName")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("attribute_name")
                .addStringAttributeValue()
        );
  }
}
