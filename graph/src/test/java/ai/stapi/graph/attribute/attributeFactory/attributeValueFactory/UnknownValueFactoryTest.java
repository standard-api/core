package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.UnknownAttributeValue;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class UnknownValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final String TESTED_ATTRIBUTE_TYPE = UnknownAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = UnknownAttributeValue.class;
  public static final Class EXPECTED_RAW_VALUE_TYPE = String.class;

  @Test
  void itCanCreateAttribute() {
    this.testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
        EXPECTED_RAW_VALUE_TYPE,
        TESTED_ATTRIBUTE_TYPE,
        TESTED_ATTRIBUTE_CLASS,
        List.of("example_code", "example_code2", 666)
    );
  }
  
}
