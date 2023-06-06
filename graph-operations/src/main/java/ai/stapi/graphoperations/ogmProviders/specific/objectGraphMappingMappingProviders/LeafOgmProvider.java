package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

public class LeafOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
    var definition = new ObjectGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(OgmGraphElementTypes.OGM_LEAF_NODE));
    definition.addField("graphDescription")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge(
            OgmGraphElementTypes.EDGE_FROM_OGM_TO_GRAPH_DESCRIPTION))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(GraphDescription.INTERFACE_UUID);
    return definition.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(LeafObjectGraphMapping.SERIALIZATION_TYPE);
  }
}
