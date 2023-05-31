package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ListAttributeFactory implements AttributeFactory {

  private final GenericAttributeValueFactory attributeValueFactory;

  public ListAttributeFactory(GenericAttributeValueFactory attributeValueFactory) {
    this.attributeValueFactory = attributeValueFactory;
  }

  @Override
  public Attribute<?> create(
      String attributeName,
      List<AttributeValueFactoryInput> parameters,
      Map<String, MetaData> metaData
  ) {
    if (parameters.isEmpty()) {
      return new ListAttribute(
          attributeName,
          metaData
      );
    }

    return new ListAttribute(
        attributeName,
        metaData,
        parameters.stream()
            .map(param -> this.attributeValueFactory.create(attributeName, param))
            .collect(Collectors.toList())
    );
  }

  @Override
  public boolean supportsStructureType(String structureType) {
    return structureType.equals(ListAttribute.DATA_STRUCTURE_TYPE);
  }
}
