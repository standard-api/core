package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations;

import ai.stapi.graphoperations.exampleImplementations.ExampleDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListAttribute;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListOfStrings;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithInterface;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithListOfInterfaces;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithMapOfInterfaces;
import ai.stapi.graphoperations.exampleImplementations.NestedDtoInterface;
import ai.stapi.serialization.classNameProvider.specific.AbstractSerializableObjectClassNameProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ExampleClassNameProvider extends AbstractSerializableObjectClassNameProvider {

  @Override
  protected Map<String, Class<?>> getClassMap() {
    var map = new HashMap<String, Class<?>>();
    map.put(ExampleDto.SERIALIZATION_TYPE, ExampleDto.class);
    map.put(ExampleNestedDto.SERIALIZATION_TYPE, ExampleNestedDto.class);
    map.put(ExampleDtoWithListOfStrings.SERIALIZATION_TYPE, ExampleDtoWithListOfStrings.class);
    map.put(ExampleNestedDtoWithInterface.SERIALIZATION_TYPE, ExampleNestedDtoWithInterface.class);
    map.put(ExampleNestedDtoWithListOfInterfaces.SERIALIZATION_TYPE,
        ExampleNestedDtoWithListOfInterfaces.class);
    map.put(ExampleNestedDtoWithMapOfInterfaces.SERIALIZATION_TYPE,
        ExampleNestedDtoWithMapOfInterfaces.class);
    map.put(NestedDtoInterface.SERIALIZATION_TYPE, NestedDtoInterface.class);
    map.put(ExampleDtoWithListAttribute.SERIALIZATION_TYPE, ExampleDtoWithListAttribute.class);
    return map;
  }
}
