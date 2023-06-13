package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;

public class ObjectFieldOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType,
                                                String fieldName) {
    var fieldOgmBuilder = new ObjectGraphMappingBuilder();
    fieldOgmBuilder
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("ogm_object_field"));
    fieldOgmBuilder
        .addField("relation")
        .setRelation(
            new GraphDescriptionBuilder().addOutgoingEdge("has_child_relation_declaration"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(Declaration.INTERFACE_UUID);
    fieldOgmBuilder
        .addField("fieldObjectGraphMapping")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_child_ogm"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(ObjectGraphMapping.INTERFACE_UUID);
    return fieldOgmBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ObjectFieldDefinition.SERIALIZATION_TYPE);
  }
}
