package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.MarkdownAttributeValue;
import java.util.List;
import org.junit.jupiter.api.Test;

class MarkdownValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final String TESTED_ATTRIBUTE_TYPE = MarkdownAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = MarkdownAttributeValue.class;
  public static final Class EXPECTED_RAW_VALUE_TYPE = String.class;

  @Test
  void itCanCreateAttribute() {
    this.testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
        EXPECTED_RAW_VALUE_TYPE,
        TESTED_ATTRIBUTE_TYPE,
        TESTED_ATTRIBUTE_CLASS,
        List.of("example_code", "example_code2")
    );
  }

  @Test
  void itCanFailsToCreateAttributeWithBadValueType() {
    this.testItFailsToCreateIncorrectValueType(
        TESTED_ATTRIBUTE_TYPE,
        List.of(666)
    );
  }
  
}
