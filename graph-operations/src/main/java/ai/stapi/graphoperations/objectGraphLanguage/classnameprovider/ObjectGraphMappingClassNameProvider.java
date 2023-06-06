package ai.stapi.graphoperations.objectGraphLanguage.classnameprovider;

import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ReferenceObjectGraphMapping;
import ai.stapi.serialization.classNameProvider.specific.AbstractSerializableObjectClassNameProvider;
import java.util.Map;

public class ObjectGraphMappingClassNameProvider extends AbstractSerializableObjectClassNameProvider {
  @Override
  protected Map<String, Class<?>> getClassMap() {
    return Map.of(
        ObjectObjectGraphMapping.SERIALIZATION_TYPE, ObjectObjectGraphMapping.class,
        InterfaceObjectGraphMapping.SERIALIZATION_TYPE, InterfaceObjectGraphMapping.class,
        LeafObjectGraphMapping.SERIALIZATION_TYPE, LeafObjectGraphMapping.class,
        ListObjectGraphMapping.SERIALIZATION_TYPE, ListObjectGraphMapping.class,
        MapObjectGraphMapping.SERIALIZATION_TYPE, MapObjectGraphMapping.class,
        ReferenceObjectGraphMapping.SERIALIZATION_TYPE, ReferenceObjectGraphMapping.class,
        ObjectFieldDefinition.SERIALIZATION_TYPE, ObjectFieldDefinition.class
    );
  }
}
