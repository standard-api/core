package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.util.List;
import java.util.stream.Collectors;


public class GenericAttributeValueFactory {

  private final List<AttributeValueFactory> attributeValueFactories;

  public GenericAttributeValueFactory(List<AttributeValueFactory> attributeValueFactories) {
    this.attributeValueFactories = attributeValueFactories;
  }

  public AttributeValue<?> create(
      String attributeName,
      AttributeValueFactoryInput parameters
  ) {
    var valueFactory = this.getSupportedValueFactory(
        attributeName,
        parameters.getDataType()
    );
    return valueFactory.create(parameters.getValue(), attributeName);
  }

  public boolean isValidValue(
      String dataType,
      Object value
  ) {
    var valueFactory = this.getSupportedValueFactory("", dataType);
    return valueFactory.isValidValue(value);
  }

  private AttributeValueFactory getSupportedValueFactory(String attributeName, String dataType) {
    var supported = attributeValueFactories.stream()
        .filter(factory -> factory.supportsDataType(dataType))
        .toList();

    if (supported.size() != 1) {
      throw CannotCreateAttribute.becauseProvidedDataTypeIsNotSupportedByExactlyOneValueFactory(
          attributeName,
          dataType,
          supported.stream().map(AttributeValueFactory::getClass).collect(Collectors.toList())
      );
    }
    return supported.get(0);
  }
}
