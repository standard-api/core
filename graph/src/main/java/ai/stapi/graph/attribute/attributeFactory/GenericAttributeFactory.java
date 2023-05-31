package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class GenericAttributeFactory {

  private final List<AttributeFactory> attributeFactories;

  public GenericAttributeFactory(List<AttributeFactory> attributeFactories) {
    this.attributeFactories = attributeFactories;
  }

  public Attribute<?> create(
      String attributeName,
      String structureType,
      List<AttributeValueFactoryInput> values,
      Map<String, MetaData> metaData
  ) {
    var supported = attributeFactories.stream()
        .filter(factory -> factory.supportsStructureType(structureType))
        .toList();
    if (supported.isEmpty()) {
      throw CannotCreateAttribute.becauseProvidedStructureTypeIsNotSupportedByAnyFactory(
          attributeName,
          structureType,
          supported.stream().map(AttributeFactory::getClass).collect(Collectors.toList())
      );
    }
    if (supported.size() > 1) {
      throw CannotCreateAttribute.becauseProvidedStructureTypeIsSupportedByMoreThanOneFactory(
          attributeName,
          structureType,
          supported.stream().map(AttributeFactory::getClass).collect(Collectors.toList())
      );
    }
    return supported.get(0).create(
        attributeName,
        values,
        metaData
    );
  }
}
