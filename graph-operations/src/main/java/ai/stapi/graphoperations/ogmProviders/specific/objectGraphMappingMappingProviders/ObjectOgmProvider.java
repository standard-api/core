package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class ObjectOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
    var objectOgmBuilder = new ObjectGraphMappingBuilder();
    objectOgmBuilder
        .setGraphDescription(
            new GraphDescriptionBuilder().addNodeDescription(OgmGraphElementTypes.OGM_OBJECT_NODE));
    objectOgmBuilder
        .addField("graphDescription")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge(
            OgmGraphElementTypes.EDGE_FROM_OGM_TO_GRAPH_DESCRIPTION))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(GraphDescription.INTERFACE_UUID);
    var mapOgmBuilder = objectOgmBuilder
        .addField("fields")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge(
            OgmGraphElementTypes.EDGE_FROM_OBJECT_OGM_TO_FIELD_OGM))
        .addMapAsObjectFieldMapping();
    mapOgmBuilder
        .addLeafKeyMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute(OgmGraphElementTypes.FIELD_NAME_ATTRIBUTE)
                .addStringAttributeValue()
        );
    mapOgmBuilder
        .addInterfaceObjectValueMapping()
        .setUuid(ObjectFieldDefinition.SERIALIZATION_TYPE);
    return objectOgmBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ObjectObjectGraphMapping.SERIALIZATION_TYPE);
  }
}
