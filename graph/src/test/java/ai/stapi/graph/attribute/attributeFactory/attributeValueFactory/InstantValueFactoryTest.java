package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class InstantValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final Class EXPECTED_RAW_VALUE_TYPE = Instant.class;
  public static final String TESTED_ATTRIBUTE_TYPE = InstantAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = InstantAttributeValue.class;

  @Test
  void itCanCreateAttribute() {
    this.testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
        EXPECTED_RAW_VALUE_TYPE,
        TESTED_ATTRIBUTE_TYPE,
        TESTED_ATTRIBUTE_CLASS,
        List.of("2016-05-21T11:11:11Z", "2016-05-22T05:05:05.666Z")
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
