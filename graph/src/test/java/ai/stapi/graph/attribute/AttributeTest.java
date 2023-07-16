package ai.stapi.graph.attribute;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.test.base.UnitTestCase;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AttributeTest extends UnitTestCase {

  @Test
  void itCanCreateDoubleAttribute() {
    //Given
    var expectedName = "name";
    var expectedValue = 5.0d;
    //When
    var attribute = new LeafAttribute<>(
        expectedName,
        new DecimalAttributeValue(expectedValue)
    );
    //Then
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateStringAttribute() {
    //Given
    var expectedName = "name";
    var expectedValue = "value";
    //When
    var attribute = new LeafAttribute<>(
        expectedName,
        new StringAttributeValue(expectedValue)
    );
    //Then
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateIntegerAttribute() {
    //Given
    var expectedName = "name";
    var expectedValue = 1;
    //When
    var attribute = new LeafAttribute<>(
        expectedName,
        new IntegerAttributeValue(expectedValue)
    );
    //Then
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateBooleanAttribute() {
    //Given
    var expectedName = "name";
    var expectedValue = false;
    //When
    var attribute = new LeafAttribute<>(
        expectedName,
        new BooleanAttributeValue(expectedValue)
    );
    //Then
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateDateTimeAttribute() {
    //Given
    var expectedName = "name";
    var expectedValue = Instant.now();
    //When
    var attribute = new LeafAttribute<>(
        expectedName,
        new InstantAttributeValue(expectedValue)
    );
    //Then
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateListAttribute() {
    //Given
    var expectedName = "name";
    //When
    var attribute = new ListAttribute(
        expectedName,
        new StringAttributeValue("Value1"),
        new StringAttributeValue("Value2"),
        new StringAttributeValue("Value3")
    );
    //Then
    var expectedValue = List.of("Value1", "Value2", "Value3");
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }

  @Test
  void itCanCreateSetAttribute() {
    //Given
    var expectedName = "name";
    //When
    var attribute = new SetAttribute(
        expectedName,
        new StringAttributeValue("Value1"),
        new StringAttributeValue("Value2"),
        new StringAttributeValue("Value3")
    );
    //Then
    var expectedValue = Set.of("Value3", "Value2", "Value1");
    assertEquals(expectedName, attribute.getName());
    assertEquals(expectedValue, attribute.getValue());
  }
}
