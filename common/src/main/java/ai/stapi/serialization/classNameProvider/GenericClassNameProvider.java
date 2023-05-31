package ai.stapi.serialization.classNameProvider;

import ai.stapi.serialization.classNameProvider.exception.GenericClassNameProviderException;
import ai.stapi.serialization.classNameProvider.specific.SpecificClassNameProvider;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenericClassNameProvider {

  private final List<SpecificClassNameProvider> classNameProviderList;

  public GenericClassNameProvider(List<SpecificClassNameProvider> classNameProviderList) {
    this.classNameProviderList = classNameProviderList;
  }

  public Class<?> getClassType(String serializationType) {
    var provider = this.getSupportingSpecificProvider(serializationType);
    return provider.getClassType(serializationType);
  }

  public String getSerializationType(Class<?> classType) {
    var provider = this.getSupportingSpecificProvider(classType);
    return provider.getSerializationType(classType);
  }

  public List<Class<?>> getAllClasses() {
    return this.classNameProviderList.stream()
        .map(SpecificClassNameProvider::getAllClasses)
        .flatMap(List::stream)
        .toList();
  }

  private SpecificClassNameProvider getSupportingSpecificProvider(String serializationType) {
    var supportingGraphWriters = this.classNameProviderList.stream()
        .filter(specificObjectGraphMapper -> specificObjectGraphMapper.supports(serializationType));

    var listOfSupportingResolvers = supportingGraphWriters.toList();
    if (listOfSupportingResolvers.size() == 0) {
      throw GenericClassNameProviderException.becauseNoSupportingSpecificProvider(
          serializationType);
    }
    if (listOfSupportingResolvers.size() > 1) {
      throw GenericClassNameProviderException.becauseMoreThanOneSpecificProvider(
          serializationType,
          listOfSupportingResolvers
      );
    }
    return listOfSupportingResolvers.get(0);
  }

  private SpecificClassNameProvider getSupportingSpecificProvider(Class<?> classType) {
    var supportingGraphWriters = this.classNameProviderList.stream()
        .filter(specificObjectGraphMapper -> specificObjectGraphMapper.supports(classType));

    var listOfSupportingResolvers = supportingGraphWriters.toList();
    if (listOfSupportingResolvers.size() == 0) {
      throw GenericClassNameProviderException.becauseNoSupportingSpecificProvider(
          classType.getName());
    }
    if (listOfSupportingResolvers.size() > 1) {
      throw GenericClassNameProviderException.becauseMoreThanOneSpecificProvider(
          classType.getName(),
          listOfSupportingResolvers
      );
    }
    return listOfSupportingResolvers.get(0);
  }


}
