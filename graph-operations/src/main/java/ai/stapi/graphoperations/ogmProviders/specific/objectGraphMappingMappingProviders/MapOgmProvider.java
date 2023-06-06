package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class MapOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
    var mapBuilder = new ObjectGraphMappingBuilder();
    mapBuilder.setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("ogm_map"));
    mapBuilder.addField("graphDescription")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_graph_description"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(GraphDescription.INTERFACE_UUID);
    mapBuilder.addField("keyObjectGraphMapping")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_key_ogm"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(ObjectGraphMapping.INTERFACE_UUID);
    mapBuilder.addField("valueObjectGraphMapping")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_value_ogm"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(ObjectGraphMapping.INTERFACE_UUID);
    return mapBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(MapObjectGraphMapping.SERIALIZATION_TYPE);
  }
}
