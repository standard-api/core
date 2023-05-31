package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.AttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.StringValueFactory;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.test.base.UnitTestCase;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class ListAttributeFactoryTest extends UnitTestCase {

  private GenericAttributeValueFactory genericAttributeValueFactory;
  private GenericAttributeFactory genericAttributeFactory;
  private Logger logger;
  private ListAppender<ILoggingEvent> listAppender;

  @BeforeEach
  public void setUp() {
    this.logger = (Logger) LoggerFactory.getLogger(LeafAttributeFactory.class);
    this.listAppender = new ListAppender<ILoggingEvent>();
    this.listAppender.start();
    this.logger.addAppender(this.listAppender);
  }
  
  @Test
  void itShouldCorrectlyCreateListStringAttributes() {

    this.givenGenericAttributeFactorySupportingLeafWithValueFactories(
        new StringValueFactory()
    );

    var actualLeaf = this.genericAttributeFactory.create(
        "exampleName",
        ListAttribute.DATA_STRUCTURE_TYPE,
        List.of(
            new AttributeValueFactoryInput(
                "ExampleValue1",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        ),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(ListAttribute.class, actualLeaf);
  }

  @Test
  void itShouldCorrectlyCreateEmptyListAttribute() {

    this.givenGenericAttributeFactorySupportingLeafWithValueFactories(
        new StringValueFactory()
    );

    var actualLeaf = this.genericAttributeFactory.create(
        "exampleName",
        ListAttribute.DATA_STRUCTURE_TYPE,
        List.of(),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(ListAttribute.class, actualLeaf);
  }

  private void givenGenericAttributeFactorySupportingLeafWithValueFactories(AttributeValueFactory... attributeValueFactories) {
    this.genericAttributeValueFactory = this.createGenericAttributeValueFactory(
        attributeValueFactories
    );
    this.genericAttributeFactory = this.createGenericAttributeFactory(
        new ListAttributeFactory(this.genericAttributeValueFactory)
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
