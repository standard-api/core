package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.InterfaceGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.springframework.stereotype.Service;

public class InterfaceGraphDescriptionGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  protected String getGraphDescriptionNodeType() {
    return "graph_description_interface";
  }

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {
    parameters.addField("interfaceId")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("interface_id")
                .addStringAttributeValue()
        );
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(InterfaceGraphDescription.SERIALIZATION_TYPE);
  }
}
