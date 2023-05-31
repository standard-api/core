package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.AttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.StringValueFactory;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.test.base.UnitTestCase;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class GenericAttributeFactoryTest extends UnitTestCase {

  private GenericAttributeValueFactory genericAttributeValueFactory;
  private GenericAttributeFactory genericAttributeFactory;

  @Test
  void itShouldCorrectlyCreateStringAttribute() {

    this.givenGenericAttributeFactorySupportingLeafWithValueFactories(
        new StringValueFactory()
    );

    var actualLeaf = this.genericAttributeFactory.create(
        "exampleName",
        LeafAttribute.DATA_STRUCTURE_TYPE,
        List.of(
            new AttributeValueFactoryInput(
                "ExampleValue1",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        ),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(LeafAttribute.class, actualLeaf);
  }
  @Test
  void itShouldLogWarningWhenCreatingStringAttributeFromTwoInputs() {

    this.givenGenericAttributeFactorySupportingLeafWithValueFactories(
        new StringValueFactory()
    );
    Executable exception = () -> this.genericAttributeFactory.create(
        "exampleName",
        "unsupported_structure_type",
        List.of(
            new AttributeValueFactoryInput(
                "ExampleValue1",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        ),
        new HashMap<>()
    );
    this.thenExceptionMessageApproved(CannotCreateAttribute.class, exception);
  }

  @Test
  void itShouldThrowCannotCreateAttributeWhenMoreThanOneFactoriesForStructureType() {
    this.givenGenericAttributeFactorySupportingLeafTwiceWithValueFactories(
        new StringValueFactory()
    );
    
    Executable exception = () -> this.genericAttributeFactory.create(
        "exampleName",
        LeafAttribute.DATA_STRUCTURE_TYPE,
        List.of(
            new AttributeValueFactoryInput(
                "ExampleValue1",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        ),
        new HashMap<>()
    );
    this.thenExceptionMessageApproved(CannotCreateAttribute.class, exception);
  }

  private void givenGenericAttributeFactorySupportingLeafWithValueFactories(AttributeValueFactory... attributeValueFactories) {
    this.genericAttributeValueFactory = this.createGenericAttributeValueFactory(
        attributeValueFactories
    );
    this.genericAttributeFactory = this.createGenericAttributeFactory(
        new LeafAttributeFactory(this.genericAttributeValueFactory)
    );
  }
  
  private void givenGenericAttributeFactorySupportingLeafTwiceWithValueFactories(AttributeValueFactory... attributeValueFactories) {
    this.genericAttributeValueFactory = this.createGenericAttributeValueFactory(
        attributeValueFactories
    );
    this.genericAttributeFactory = this.createGenericAttributeFactory(
        new LeafAttributeFactory(this.genericAttributeValueFactory),
        new LeafAttributeFactory(this.genericAttributeValueFactory)
    );
  }

  private GenericAttributeFactory createGenericAttributeFactory(
      AttributeFactory... attributeFactories
  ) {
    return new GenericAttributeFactory(
        Arrays.stream(attributeFactories).toList()
    );
  }
  private GenericAttributeValueFactory createGenericAttributeValueFactory(
      AttributeValueFactory... attributeFactories
  ) {
    return new GenericAttributeValueFactory(
        Arrays.stream(attributeFactories).toList()
    );
  }

}
