package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SetAttributeFactory implements AttributeFactory {

  private final GenericAttributeValueFactory attributeValueFactory;

  public SetAttributeFactory(GenericAttributeValueFactory attributeValueFactory) {
    this.attributeValueFactory = attributeValueFactory;
  }

  @Override
  public Attribute<?> create(
      String attributeName,
      List<AttributeValueFactoryInput> parameters,
      Map<String, MetaData> metaData
  ) {
    return new SetAttribute(
        attributeName,
        metaData,
        parameters.stream()
            .map(param -> this.attributeValueFactory.create(attributeName, param))
            .collect(Collectors.toSet())
    );
  }

  @Override
  public boolean supportsStructureType(String structureType) {
    return structureType.equals(SetAttribute.DATA_STRUCTURE_TYPE);
  }
}
