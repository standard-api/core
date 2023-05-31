package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    
    return this.createAttributeWithTimeStamp(date.orElseThrow());
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

  protected abstract AttributeValue<?> createAttributeWithTimeStamp(
      Timestamp timestamp
  );

  protected abstract String getSupportedDataType();

  protected Optional<Timestamp> parseValueToDate(String value) {
    try {
      return Optional.of(Timestamp.valueOf(value));
    } catch (IllegalArgumentException e) {
      for (SimpleDateFormat supportedFormat : this.getSupportedFormats()) {
        try {
          var dateInstant = supportedFormat.parse(value).toInstant();
          var result = Timestamp.from(dateInstant);
          return Optional.of(result);
        } catch (ParseException ignored) {
          //throw exception later
        }
      }
    }
    return Optional.empty();
  }

  private List<SimpleDateFormat> getSupportedFormats() {
    return List.of(
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS"),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS"),
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
        new SimpleDateFormat("yyyy\\MM\\dd HH:mm:ss.SS"),
        new SimpleDateFormat("yyyy\\MM\\dd HH:mm:ss"),
        new SimpleDateFormat("yy\\MM\\dd HH:mm:ss.SS"),
        new SimpleDateFormat("yy\\MM\\dd HH:mm:ss"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZ")
    );
  }
}
