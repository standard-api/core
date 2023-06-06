package ai.stapi.graphoperations.objectLanguage.classnameprovider;

import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.serialization.classNameProvider.specific.AbstractSerializableObjectClassNameProvider;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ObjectLanguageClassNameProvider extends AbstractSerializableObjectClassNameProvider {
  @Override
  protected Map<String, Class<?>> getClassMap() {
    return Map.of(
        EntityIdentifier.SERIALIZATION_TYPE, EntityIdentifier.class
    );
  }
}
