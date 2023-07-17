package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDateTimeAttributeValueFactory implements AttributeValueFactory {

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

    return this.createAttributeWithUtcTime(date.orElseThrow());
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

  protected abstract AttributeValue<?> createAttributeWithUtcTime(
      Instant utcTime
  );

  protected abstract String getSupportedDataType();

  protected Optional<Instant> parseValueToDate(String value) {
    for (DateTimeFormatter formatter : getSupportedFormats()) {
      try {
        Instant instant = Instant.from(formatter.parse(value));
        return Optional.of(Instant.from(instant));
      } catch (DateTimeParseException ex) {
        // If this formatter doesn't work, try the next one
      }
    }
    // If none of the formatters worked, return empty
    return Optional.empty();
  }

  private List<DateTimeFormatter> getSupportedFormats() {
    return List.of(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_INSTANT
    );
  }
}
