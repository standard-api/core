package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;

public abstract class AbstractEdgeDescriptionGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {
    parameters.addField("edgeType")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("edge_type")
                .addStringAttributeValue()
        );
  }
}
