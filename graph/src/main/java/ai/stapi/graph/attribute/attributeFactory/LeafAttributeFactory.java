package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.utils.Stringifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LeafAttributeFactory implements AttributeFactory {

  private final Logger logger;
  private final GenericAttributeValueFactory attributeValueFactory;

  public LeafAttributeFactory(GenericAttributeValueFactory attributeValueFactory) {
    this.logger = LoggerFactory.getLogger(LeafAttributeFactory.class);
    this.attributeValueFactory = attributeValueFactory;
  }

  @Override
  public Attribute<?> create(
      String attributeName,
      List<AttributeValueFactoryInput> parameters,
      Map<String, MetaData> metaData
  ) {
    if (parameters.isEmpty()) {
      throw CannotCreateAttribute.becauseLeafAttributeHadNoValues(attributeName);
    }
    if (parameters.size() > 1) {
      var stringyfiedValues = parameters.stream()
          .map(Stringifier::convertToString)
          .collect(Collectors.joining(", "));

      var message = (
          """
              More values were provided, when creating Leaf Attribute, but it should have exactly one.
              Attribute name: %s
              Choosing value at first position.
              Values: %s
              """
      ).formatted(
          attributeName,
          stringyfiedValues
      );
      this.logger.warn(message);
    }
    var parameter = parameters.get(0);
    return new LeafAttribute<>(
        attributeName,
        metaData,
        this.attributeValueFactory.create(
            attributeName,
            parameter
        )
    );
  }

  @Override
  public boolean supportsStructureType(String structureType) {
    return structureType.equals(LeafAttribute.DATA_STRUCTURE_TYPE);
  }
}
