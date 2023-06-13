package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders;

import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListOfStrings;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class ExampleNestedDtoWithListOfStringsSpecificGraphMappingProvider
    implements SpecificGraphMappingProvider {

  public static final String NODE_TYPE = "example_nested_dto_with_list_of_strings_node";

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType,
                                                String fieldName) {
    var objectBuilder = new ObjectGraphMappingBuilder();
    objectBuilder
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(NODE_TYPE));
    objectBuilder
        .addField("names")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_name"))
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addNodeDescription("name_in_list_node")
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    return objectBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ExampleDtoWithListOfStrings.SERIALIZATION_TYPE);
  }
}
