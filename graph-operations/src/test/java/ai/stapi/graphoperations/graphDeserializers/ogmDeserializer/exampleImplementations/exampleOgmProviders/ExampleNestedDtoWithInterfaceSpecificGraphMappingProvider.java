package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders;

import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithInterface;
import ai.stapi.graphoperations.exampleImplementations.NestedDtoInterface;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class ExampleNestedDtoWithInterfaceSpecificGraphMappingProvider
    implements SpecificGraphMappingProvider {

  public static final String NODE_TYPE = "example_nested_dto_with_interface_node";

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType,
                                                String fieldName) {
    var objectBuilder = new ObjectGraphMappingBuilder();
    objectBuilder
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(NODE_TYPE));
    objectBuilder
        .addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    objectBuilder
        .addField("childDto")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_child"))
        .addInterfaceAsObjectFieldMapping()
        .setUuid(NestedDtoInterface.SERIALIZATION_TYPE);
    return objectBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ExampleNestedDtoWithInterface.SERIALIZATION_TYPE);
  }
}
