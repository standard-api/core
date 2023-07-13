package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeValue.DateTimeAttributeValue;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DateTimeValueFactoryTest extends AbstractAttributeValueFactoryTest {

  public static final Class EXPECTED_RAW_VALUE_TYPE = Timestamp.class;
  public static final String TESTED_ATTRIBUTE_TYPE = DateTimeAttributeValue.SERIALIZATION_TYPE;
  public static final Class TESTED_ATTRIBUTE_CLASS = DateTimeAttributeValue.class;
  
  @Autowired
  private DateTimeAttributeValueFactory dateTimeAttributeValueFactory;

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
  
  @Test
  void itCanCorrectlyParseTimestampWithTimezone() {
    var givenDate = "2023-06-11T12:47:40+10:00";
    var actualTimestamp = (DateTimeAttributeValue) this.dateTimeAttributeValueFactory.create(
        givenDate, 
        "someName"
    );
    this.thenStringApproved(actualTimestamp.toStringValue());
  }
}
