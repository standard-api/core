package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;


public class TimeAttributeValueFactory implements AttributeValueFactory {

  protected AttributeValue<?> createAttributeWithTime(LocalTime time) {
    return new TimeAttributeValue(time);
  }

  protected String getSupportedDataType() {
    return TimeAttributeValue.SERIALIZATION_TYPE;
  }


  @Override
  public AttributeValue<?> create(Object value, String attributeName) {
    if (!this.isValidValue(value)) {
      throw CannotCreateAttribute.becauseProvidedValueCouldNotBeConvertedToProvidedDataType(
          attributeName,
          this.getSupportedDataType(),
          value
      );
    }
    var date = this.parseValueToDate((String) value);

    return this.createAttributeWithTime(date.orElseThrow());
  }

  @Override
  public boolean isValidValue(Object value) {
    if (!(value instanceof String stringValue)) {
      return false;
    }

    return this.parseValueToDate(stringValue).isPresent();
  }

  @Override
  public boolean supportsDataType(String dataType) {
    return dataType.equals(this.getSupportedDataType());
  }

  protected Optional<LocalTime> parseValueToDate(String value) {
    try {
      LocalTime time = LocalTime.parse(value);
      return Optional.of(time);
    } catch (DateTimeParseException ex) {
      // If the parse operation fails, return an empty Optional
      return Optional.empty();
    }
  }

  private List<DateTimeFormatter> getSupportedFormats() {
    return List.of(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_INSTANT
    );
  }
}
