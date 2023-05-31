package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.Test;

class TimeValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final Class EXPECTED_RAW_VALUE_TYPE = Timestamp.class;
  public static final String TESTED_ATTRIBUTE_TYPE = TimeAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = TimeAttributeValue.class;

  @Test
  void itCanCreateAttribute() {
    this.testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
        EXPECTED_RAW_VALUE_TYPE,
        TESTED_ATTRIBUTE_TYPE,
        TESTED_ATTRIBUTE_CLASS,
        List.of("2016-05-21 11:11:11.0", "2016-05-22 05:05:05.0")
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
