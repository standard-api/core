package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.AttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.StringValueFactory;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.test.base.UnitTestCase;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.LoggerFactory;

class LeafAttributeFactoryTest extends UnitTestCase {

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
  void itShouldCreateLeafStringAttributes() {

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
    
    this.genericAttributeFactory.create(
        "exampleName",
        LeafAttribute.DATA_STRUCTURE_TYPE,
        List.of(
            new AttributeValueFactoryInput(
                "ExampleValue1",
                StringAttributeValue.SERIALIZATION_TYPE
            ),
            new AttributeValueFactoryInput(
                "ExampleValue2",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        ),
        new HashMap<>()
    );

    List<ILoggingEvent> list = listAppender.list;
    var loggedWarning = list.stream()
        .filter(event -> event.getLevel() == Level.WARN)
        .map(iLoggingEvent -> iLoggingEvent.getFormattedMessage())
        .collect(Collectors.toList());
    
    Assertions.assertEquals(1, loggedWarning.size());

    this.thenStringApproved(loggedWarning.get(0));
    
  }

  @Test
  void itShouldThrowCannotCreateAttributeWhenCreatingStringAttributeFromNoInputs() {

    this.givenGenericAttributeFactorySupportingLeafWithValueFactories(
        new StringValueFactory()
    );
    Executable exception = () -> this.genericAttributeFactory.create(
        "exampleName",
        LeafAttribute.DATA_STRUCTURE_TYPE,
        List.of(),
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
