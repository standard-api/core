package ai.stapi.serialization.classNameProvider.specific;

import java.util.List;

public interface SpecificClassNameProvider {

  Class<?> getClassType(String serializationType);

  String getSerializationType(Class<?> classType);

  List<Class<?>> getAllClasses();

  boolean supports(String serializationType);

  boolean supports(Class<?> classType);
}
