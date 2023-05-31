package ai.stapi.graph.attribute.attributeFactory.exceptions;

import ai.stapi.graph.attribute.attributeFactory.AttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.AttributeValueFactory;
import java.util.List;
import java.util.stream.Collectors;

public class CannotCreateAttribute extends RuntimeException {

  protected CannotCreateAttribute(String becauseMessage) {
    super("Cannot create attribute, because " + becauseMessage);
  }

  public static CannotCreateAttribute becauseProvidedStructureTypeIsNotSupportedByAnyFactory(
      String attributeName,
      String structureType,
      List<Class<? extends AttributeFactory>> foundFactories
  ) {
    return new CannotCreateAttribute(
        "provided structure type is not supported by any factory." +
            "\nAttribute name: " + attributeName +
            "\nStructure type: " + structureType +
            "\n" + (
            foundFactories.size() == 0 ?
                "No supported factories found." :
                "Found Factories: " + foundFactories.stream().map(Class::getSimpleName)
                    .collect(Collectors.joining(", "))
        )
    );
  }
  
  public static CannotCreateAttribute becauseProvidedStructureTypeIsSupportedByMoreThanOneFactory(
      String attributeName,
      String structureType,
      List<Class<? extends AttributeFactory>> foundFactories
  ) {
    return new CannotCreateAttribute(
        "provided structure type is supported by more than one factory." +
            "\nAttribute name: " + attributeName +
            "\nStructure type: " + structureType +
            "\n" + "Found Factories: " + foundFactories.stream().map(Class::getSimpleName)
                    .collect(Collectors.joining(", ")
        )
    );
  }

  public static CannotCreateAttribute becauseProvidedDataTypeIsNotSupportedByExactlyOneValueFactory(
      String attributeName,
      String dataType,
      List<Class<? extends AttributeValueFactory>> foundFactories
  ) {
    return new CannotCreateAttribute(
        "provided data type is not supported by exactly one factory." +
            "\nAttribute name: " + attributeName +
            "\nData type: " + dataType +
            "\n" + (
            foundFactories.size() == 0 ?
                "No supported factories found." :
                "Found Factories: " + foundFactories.stream().map(Class::getSimpleName)
                    .collect(Collectors.joining(", "))
        )
    );
  }

  public static CannotCreateAttribute becauseLeafAttributeHadNoValues(
      String attributeName
  ) {
    return new CannotCreateAttribute(
        "there was no provided a value for leaf attribute." +
            "\nAttribute name: " + attributeName
    );
  }

  public static CannotCreateAttribute becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
      String attributeName,
      String dataType,
      Object providedValue
  ) {
    return new CannotCreateAttribute(
        "because provided value could not be converted to provided data type." +
            "\nAttribute name: " + attributeName +
            "\nData type: " + dataType +
            "\nProvided value class: " + providedValue.getClass().getSimpleName() +
            "\nProvided value in string: " + providedValue
    );
  }
}
