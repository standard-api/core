package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders;

import ai.stapi.graphoperations.exampleImplementations.ExampleDto;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class ExampleSimpleDtoSpecificGraphMappingProvider implements SpecificGraphMappingProvider {

  public static final String NODE_TYPE = "example_dto_node";

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
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
    return objectBuilder.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ExampleDto.SERIALIZATION_TYPE);
  }
}
