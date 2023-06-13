package ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;

public class InterfaceOgmProvider implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType,
                                                String fieldName) {
    var definition = new ObjectGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("ogm_interface"));
    definition.addField("interfaceUuid")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("interfaceUuid")
                .addStringAttributeValue()
        );
    return definition.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(InterfaceObjectGraphMapping.SERIALIZATION_TYPE);
  }
}
