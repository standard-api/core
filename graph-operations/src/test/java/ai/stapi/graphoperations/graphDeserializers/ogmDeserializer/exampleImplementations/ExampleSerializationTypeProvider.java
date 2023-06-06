package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations;

import ai.stapi.graphoperations.exampleImplementations.ExampleDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListAttribute;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListOfStrings;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithInterface;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithListOfInterfaces;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithMapOfInterfaces;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleDtoWithListAttributeSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithInterfaceSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithListOfInterfacesGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithListOfStringsSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithMapOfInterfacesGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleSimpleDtoSpecificGraphMappingProvider;
import ai.stapi.graphoperations.serializationTypeProvider.specific.SpecificSerializationTypeProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ExampleSerializationTypeProvider implements SpecificSerializationTypeProvider {

  @Override
  public String provideSerializationType(TraversableGraphElement element) {
    return this.getSerializationTypeMap().get(element.getType());
  }

  @Override
  public boolean supports(TraversableGraphElement element) {
    return this.getSerializationTypeMap().containsKey(element.getType());
  }

  private Map<String, String> getSerializationTypeMap() {
    var map = new HashMap<String, String>();
    map.put(ExampleSimpleDtoSpecificGraphMappingProvider.NODE_TYPE, ExampleDto.SERIALIZATION_TYPE);
    map.put(ExampleNestedDtoSpecificGraphMappingProvider.NODE_TYPE,
        ExampleNestedDto.SERIALIZATION_TYPE);
    map.put(ExampleNestedDtoWithInterfaceSpecificGraphMappingProvider.NODE_TYPE,
        ExampleNestedDtoWithInterface.SERIALIZATION_TYPE);
    map.put(ExampleNestedDtoWithListOfStringsSpecificGraphMappingProvider.NODE_TYPE,
        ExampleDtoWithListOfStrings.SERIALIZATION_TYPE);
    map.put(ExampleNestedDtoWithListOfInterfacesGraphMappingProvider.NODE_TYPE,
        ExampleNestedDtoWithListOfInterfaces.SERIALIZATION_TYPE);
    map.put(ExampleNestedDtoWithMapOfInterfacesGraphMappingProvider.NODE_TYPE,
        ExampleNestedDtoWithMapOfInterfaces.SERIALIZATION_TYPE);
    map.put(ExampleDtoWithListAttributeSpecificGraphMappingProvider.NODE_TYPE,
        ExampleDtoWithListAttribute.SERIALIZATION_TYPE);
    return map;
  }
}
