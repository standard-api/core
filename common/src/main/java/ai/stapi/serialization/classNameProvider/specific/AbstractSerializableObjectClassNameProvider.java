package ai.stapi.serialization.classNameProvider.specific;

import ai.stapi.serialization.classNameProvider.exception.SerializableObjectClassNameProviderException;
import java.util.List;
import java.util.Map;

public abstract class AbstractSerializableObjectClassNameProvider
    implements SpecificClassNameProvider {

  public Class<?> getClassType(String serializationType) {
    var map = this.getClassMap();
    if (!map.containsKey(serializationType)) {
      throw SerializableObjectClassNameProviderException.becauseTypeIsNotDefined(serializationType);
    }
    return map.get(serializationType);
  }

  @Override
  public String getSerializationType(Class<?> classType) {
    return this.getClassMap().entrySet().stream()
        .filter(entry -> entry.getValue().equals(classType))
        .findAny()
        .map(Map.Entry::getKey)
        .orElse("throw exception here");
  }

  @Override
  public List<Class<?>> getAllClasses() {
    return this.getClassMap().values().stream().toList();
  }

  @Override
  public boolean supports(String serializationType) {
    return this.getClassMap().containsKey(serializationType);
  }

  @Override
  public boolean supports(Class<?> classType) {
    return this.getClassMap().values().stream().anyMatch(value -> value.equals(classType));
  }

  protected abstract Map<String, Class<?>> getClassMap();
}
