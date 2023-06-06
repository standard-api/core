package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class ListOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
    var listBuilder = new ObjectGraphMappingBuilder();
    listBuilder.setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("ogm_list"));
    listBuilder.addField("graphDescription")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_graph_description"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(GraphDescription.INTERFACE_UUID);
    listBuilder.addField("childObjectGraphMapping")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_child_ogm"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(ObjectGraphMapping.INTERFACE_UUID);
    return listBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ListObjectGraphMapping.SERIALIZATION_TYPE);
  }
}
