package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalNodeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

public class RemovalNodeDescriptionGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  @NotNull
  protected String getGraphDescriptionNodeType() {
    return "graph_description_removal_node";
  }

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {
    parameters.addField("nodeType")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("node_type")
                .addStringAttributeValue()
        );
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(RemovalNodeDescription.SERIALIZATION_TYPE);
  }
}
