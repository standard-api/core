package ai.stapi.graphsystem.structuredefinition.classnameprovider;

import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.serialization.classNameProvider.specific.AbstractSerializableObjectClassNameProvider;
import java.util.Map;

public class StructureDefinitionClassNameProvider extends AbstractSerializableObjectClassNameProvider {
  @Override
  protected Map<String, Class<?>> getClassMap() {
    return Map.of(
        StructureDefinitionData.SERIALIZATION_TYPE, StructureDefinitionData.class
    );
  }
}
