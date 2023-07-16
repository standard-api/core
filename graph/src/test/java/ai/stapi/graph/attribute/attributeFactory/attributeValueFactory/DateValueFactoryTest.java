package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DateValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final Class EXPECTED_RAW_VALUE_TYPE = LocalDate.class;
  public static final String TESTED_ATTRIBUTE_TYPE = DateAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = DateAttributeValue.class;

  @Test
  void itCanCreateAttribute() {
    this.testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
        EXPECTED_RAW_VALUE_TYPE,
        TESTED_ATTRIBUTE_TYPE,
        TESTED_ATTRIBUTE_CLASS,
        List.of("2016-05-21", "2016-05-22")
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
