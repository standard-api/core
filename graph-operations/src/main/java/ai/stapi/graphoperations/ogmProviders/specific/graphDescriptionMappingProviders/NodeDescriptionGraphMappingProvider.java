package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

public class NodeDescriptionGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  @NotNull
  protected String getGraphDescriptionNodeType() {
    return "graph_description_node";
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
    return serializationType.equals(NodeDescription.SERIALIZATION_TYPE);
  }
}
