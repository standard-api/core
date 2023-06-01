package ai.stapi.graph.attribute;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.attribute.attributeValue.AttributeValue;
import ai.stapi.graph.attribute.attributeValue.Base64BinaryAttributeValue;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.CanonicalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.CodeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DateAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graph.attribute.attributeValue.InstantAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.MarkdownAttributeValue;
import ai.stapi.graph.attribute.attributeValue.OidAttributeValue;
import ai.stapi.graph.attribute.attributeValue.PositiveIntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.attribute.attributeValue.TimeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UnsignedIntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UriAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UrlAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UuidAttributeValue;
import ai.stapi.graph.attribute.exceptions.AttributeNotFoundException;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public abstract class AttributeContainerTest extends UnitTestCase {

  protected abstract AttributeContainer getAttributeContainer();

  @Test
  public void itCanAddAttribute() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue = true;
    var attributeContainer = this.getAttributeContainer();
    var attribute = new LeafAttribute<>(
        expectedAttributeName,
        new BooleanAttributeValue(expectedAttributeValue)
    );
    //When
    var actualAttributeContainer = attributeContainer.add(attribute);
    //Then
    Assertions.assertTrue(
        actualAttributeContainer.containsAttribute(expectedAttributeName, expectedAttributeValue)
    );
    var actualAttribute = actualAttributeContainer.getAttribute(expectedAttributeName);
    Assertions.assertEquals(expectedAttributeName, actualAttribute.getName());
    Assertions.assertEquals(expectedAttributeValue, actualAttribute.getValue());
    Assertions.assertEquals(
        expectedAttributeValue,
        actualAttributeContainer.getAttributeValue(expectedAttributeName)
    );
  }

  @Test
  public void itThrowsExceptionWhenGettingAttributeNotPresentInAttributeContainer() {
    //Given
    var nonExistingAttributeName = "name";
    var attributeContainer = this.getAttributeContainer();
    //When
    Executable runnable = () -> attributeContainer.getAttribute(nonExistingAttributeName);
    //Then
    Assertions.assertThrows(AttributeNotFoundException.class, runnable);
  }

  @Test
  public void itCanAddAttributeWithSameNameAndSameValueTwiceWithoutDuplication() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue = "value";
    var attributeContainer = this.getAttributeContainer();
    var attribute1 = new LeafAttribute<>(
        expectedAttributeName,
        new StringAttributeValue(expectedAttributeValue)
    );
    var attribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new StringAttributeValue(expectedAttributeValue)
    );
    //When
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //Then
    var actualAttribute = attributeContainer.getAttribute(attribute1.getName());
    Assertions.assertEquals(1,
        attributeContainer.getVersionedAttributes().numberOfUniqueAttributeNames());
    Assertions.assertEquals(expectedAttributeName, actualAttribute.getName());
    Assertions.assertEquals(expectedAttributeValue, actualAttribute.getValue());
  }

  @Test
  public void itCanAddListAttributeWithSameNameAndSameValuesTwiceWithoutDuplication() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue = new StringAttributeValue[] {
        new StringAttributeValue("value1"),
        new StringAttributeValue("value2")
    };
    var attributeContainer = this.getAttributeContainer();
    var attribute1 = new ListAttribute(
        expectedAttributeName,
        expectedAttributeValue
    );
    var attribute2 = new ListAttribute(
        expectedAttributeName,
        new StringAttributeValue("value1"),
        new StringAttributeValue("value2")
    );
    //When
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //Then
    var actualAttribute = attributeContainer.getAttribute(attribute1.getName());
    Assertions.assertEquals(1,
        attributeContainer.getVersionedAttributes().numberOfUniqueAttributeNames());
    Assertions.assertEquals(expectedAttributeName, actualAttribute.getName());
    Assertions.assertEquals(List.of("value1", "value2"), actualAttribute.getValue());
  }

  @Test
  public void itCanAddListAttributeWithSameNameAndSameValuesButDifferentOrderAndCreateNewVersion() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue = new StringAttributeValue[] {
        new StringAttributeValue("value1"),
        new StringAttributeValue("value2")
    };
    var attributeContainer = this.getAttributeContainer();
    var attribute1 = new ListAttribute(
        expectedAttributeName,
        expectedAttributeValue
    );
    var attribute2 = new ListAttribute(
        expectedAttributeName,
        new StringAttributeValue("value2"), new StringAttributeValue("value1")
    );
    //When
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //Then
    var actualAttribute = attributeContainer.getVersionedAttribute(attribute1.getName());
    Assertions.assertEquals(2, actualAttribute.getAttributeVersions().size());
    Assertions.assertEquals(expectedAttributeName, actualAttribute.getName());
    Assertions.assertEquals(List.of("value2", "value1"), actualAttribute.getCurrent().getValue());
  }

  @Test
  public void itCanAddSetAttributeWithSameNameAndSameValuesRegardlessOfOrderTwiceWithoutDuplication() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue = new StringAttributeValue[] {
        new StringAttributeValue("value1"),
        new StringAttributeValue("value2")
    };
    var attributeContainer = this.getAttributeContainer();
    var attribute1 = new SetAttribute(
        expectedAttributeName,
        expectedAttributeValue
    );
    var attribute2 = new SetAttribute(
        expectedAttributeName,
        new StringAttributeValue("value2"), new StringAttributeValue("value1")
    );
    //When
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //Then
    var actualAttribute = attributeContainer.getVersionedAttribute(attribute1.getName());
    Assertions.assertEquals(1, actualAttribute.getAttributeVersions().size());
    Assertions.assertEquals(expectedAttributeName, actualAttribute.getName());
    Assertions.assertEquals(Set.of("value1", "value2"), actualAttribute.getCurrent().getValue());
  }

  @Test
  public void itCanAddAttributeOfSameTypeAndNameButDifferentValueTwice() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var attribute1 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var attribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    //When
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //Then
    var actualAttribute = attributeContainer.getAttribute(expectedAttributeName);
    Assertions.assertEquals(attribute2, actualAttribute);
  }

  @Test
  public void itCanAddAttributeOfSameNameButDifferentType() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = "differentValue";
    var attributeContainer = this.getAttributeContainer();
    var doubleAttribute = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var stringAttribute = new LeafAttribute<>(
        expectedAttributeName,
        new StringAttributeValue(expectedAttributeValue2)
    );
    //When
    attributeContainer = attributeContainer.add(doubleAttribute);
    attributeContainer = attributeContainer.add(stringAttribute);
    //Then
    var actualAttribute = attributeContainer.getAttribute(expectedAttributeName);
    Assertions.assertEquals(stringAttribute, actualAttribute);

  }

  @Test
  public void itReturnCurrentAttributeWhenGettingAttributeButMoreVersionsExists() {
    //Given
    var expectedAttributeName = "name";
    var attributeContainer = this.getAttributeContainer();

    var attribute1 = new LeafAttribute<>(
        expectedAttributeName,
        new StringAttributeValue("old value")
    );
    var attribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new StringAttributeValue("new value")
    );
    attributeContainer = attributeContainer.add(attribute1);
    attributeContainer = attributeContainer.add(attribute2);
    //When
    var currentAttribute = attributeContainer.getAttribute(expectedAttributeName);
    //Then
    Assertions.assertEquals(attribute2, currentAttribute);
  }

  @Test
  public void itCanOnlyGetAttributesWithGivenName() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var doubleAttribute1 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var doubleAttribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    var differentAttribute = new LeafAttribute<>(
        "differentName",
        new BooleanAttributeValue(false)
    );
    attributeContainer = attributeContainer.add(doubleAttribute1);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    attributeContainer = attributeContainer.add(differentAttribute);
    //When
    var actualAttributes = attributeContainer.getVersionedAttribute(expectedAttributeName);
    //Then
    var actualAttributesIterator = actualAttributes.iterateVersionsFromOldest();
    Assertions.assertEquals(2, actualAttributes.numberOfVersions());
    Assertions.assertEquals(expectedAttributeName, actualAttributesIterator.next().getName());
    Assertions.assertEquals(expectedAttributeName, actualAttributesIterator.next().getName());
    assertAttributesContainValue(expectedAttributeValue1, actualAttributes);
    assertAttributesContainValue(expectedAttributeValue2, actualAttributes);
  }

  @Test
  public void itShouldNotCreateNewVersionWhenAddingEqualAttributeToCurrentAttribute() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var doubleAttribute1 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var doubleAttribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    attributeContainer = attributeContainer.add(doubleAttribute1);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    //When
    var actualAttributes = attributeContainer.getVersionedAttribute(expectedAttributeName);
    //Then
    Assertions.assertEquals(2, actualAttributes.numberOfVersions());
  }

  @Test
  public void itShouldNotCreateNewVersionWhenAddingAttributeWithSameValueAsCurrent() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var timestamp1 = Timestamp.valueOf("2022-01-01 01:01:01");
    var timeStamp2 = Timestamp.valueOf("2022-02-02 02:02:02");
    var timeStamp3 = Timestamp.valueOf("2022-03-03 03:03:03");
    var doubleAttribute1 = new LeafAttribute<>(
        expectedAttributeName,
        timestamp1,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var doubleAttribute2 = new LeafAttribute<>(
        expectedAttributeName,
        timeStamp2,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    var doubleAttribute3 = new LeafAttribute<>(
        expectedAttributeName,
        timeStamp3,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    attributeContainer = attributeContainer.add(doubleAttribute1);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    //When
    attributeContainer = attributeContainer.add(doubleAttribute3);
    var actualAttributes = attributeContainer.getVersionedAttribute(expectedAttributeName);
    //Then
    Assertions.assertEquals(2, actualAttributes.numberOfVersions());
    Assertions.assertEquals(timeStamp2, actualAttributes.getCurrent().getCreatedAt());
  }

  @Test
  public void itShouldAddAttributeWithSameNameAndValueButDifferentValueThanCurrent() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var timeStamp1 = Timestamp.from(Instant.now());
    var doubleAttribute1 = new LeafAttribute<>(
        expectedAttributeName,
        timeStamp1,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    var timeStamp2 = Timestamp.from(Instant.now().plus(45L, ChronoUnit.HOURS));
    var doubleAttribute2 = new LeafAttribute<>(
        expectedAttributeName,
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var doubleAttribute3 = new LeafAttribute<>(
        expectedAttributeName,
        timeStamp2,
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    attributeContainer = attributeContainer.add(doubleAttribute1);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    attributeContainer = attributeContainer.add(doubleAttribute3);
    //When
    var actualAttributes = attributeContainer.getVersionedAttribute(expectedAttributeName);
    //Then
    Assertions.assertEquals(3, actualAttributes.numberOfVersions());
    Assertions.assertEquals(timeStamp2, actualAttributes.getCurrent().getCreatedAt());
    Assertions.assertEquals(expectedAttributeValue2, actualAttributes.getCurrent().getValue());
  }

  @Test
  public void itShouldAddNewVersionOfAttributeWhenVersionWithSameCurrentValueExistsButIsNotCurrent() {
    //Given
    var expectedAttributeName = "name";
    var expectedAttributeValue1 = 1.0d;
    var expectedAttributeValue2 = 2.0d;
    var attributeContainer = this.getAttributeContainer();
    var doubleAttribute1 = new LeafAttribute<>(
        expectedAttributeName,
        Timestamp.valueOf("2022-01-01 01:01:01"),
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    var doubleAttribute2 = new LeafAttribute<>(
        expectedAttributeName,
        Timestamp.valueOf("2022-01-01 02:01:01"),
        new DecimalAttributeValue(expectedAttributeValue1)
    );
    var doubleAttribute3 = new LeafAttribute<>(
        expectedAttributeName,
        Timestamp.valueOf("2022-01-01 03:01:01"),
        new DecimalAttributeValue(expectedAttributeValue2)
    );
    attributeContainer = attributeContainer.add(doubleAttribute1);
    attributeContainer = attributeContainer.add(doubleAttribute2);
    //When
    attributeContainer = attributeContainer.add(doubleAttribute3);
    var actualAttributes = attributeContainer.getVersionedAttribute(expectedAttributeName);
    //Then
    Assertions.assertEquals(3, actualAttributes.numberOfVersions());
    Assertions.assertEquals(expectedAttributeValue2, actualAttributes.getCurrent().getValue());
  }

  @Test
  public void itShouldReturnImmutableVersionedAttributes() {
    //Given
    var attributeContainer = this.getAttributeContainer();
    attributeContainer = attributeContainer.add(
        new LeafAttribute<>(
            "name",
            Timestamp.from(Instant.now()),
            new DecimalAttributeValue(2.0d)
        )
    );
    attributeContainer = attributeContainer.add(
        new LeafAttribute<>(
            "name",
            Timestamp.from(Instant.now()),
            new DecimalAttributeValue(3.0d)
        )
    );
    //When
    var actualAttributes = attributeContainer.getVersionedAttributes();
    //Then
    Assertions.assertTrue(actualAttributes instanceof ImmutableVersionedAttributeGroup);
  }

  @Test
  public void itShouldWorkWithVariousFhirAttributeTypes() {
    this.approveOperationOnLeafAttribute(
        new Base64BinaryAttributeValue("ZXhhbXBsZVZhbHVl="),
        new Base64BinaryAttributeValue("ZXhhbXBsZU90aGVyVmFsdWU=")
    );
    this.approveOperationOnLeafAttribute(
        new BooleanAttributeValue(true),
        new BooleanAttributeValue(false)
    );
    this.approveOperationOnLeafAttribute(
        new CanonicalAttributeValue("http://hl7.org/fhir/exampleType.html"),
        new CanonicalAttributeValue("http://hl7.org/fhir/exampleOtherType.html")
    );
    this.approveOperationOnLeafAttribute(
        new CodeAttributeValue("ExampleCodeValue"),
        new CodeAttributeValue("ExampleOtherCodeValue")
    );
    this.approveOperationOnLeafAttribute(
        new DateAttributeValue(Timestamp.valueOf("2016-05-21 00:00:00")),
        new DateAttributeValue(Timestamp.valueOf("2016-05-22 00:00:00"))
    );
    this.approveOperationOnLeafAttribute(
        new DateAttributeValue(Timestamp.valueOf("2016-05-21 12:53:40")),
        new DateAttributeValue(Timestamp.valueOf("2016-05-22 12:45:30"))
    );
    this.approveOperationOnLeafAttribute(
        new DecimalAttributeValue(4.0d),
        new DecimalAttributeValue(4.3d)
    );
    this.approveOperationOnLeafAttribute(
        new IdAttributeValue("exampleId"),
        new IdAttributeValue("exampleOtherId")
    );
    this.approveOperationOnLeafAttribute(
        new InstantAttributeValue(Timestamp.from(Instant.now())),
        new InstantAttributeValue(Timestamp.from(Instant.now()))
    );
    this.approveOperationOnLeafAttribute(
        new IntegerAttributeValue(-5),
        new IntegerAttributeValue(-4)
    );
    this.approveOperationOnLeafAttribute(
        new IntegerAttributeValue(-5),
        new IntegerAttributeValue(-4)
    );
    this.approveOperationOnLeafAttribute(
        new MarkdownAttributeValue("# Heading level 1"),
        new MarkdownAttributeValue("# Heading level 2")
    );
    this.approveOperationOnLeafAttribute(
        new OidAttributeValue("1.2.3.4.5.6.7"),
        new OidAttributeValue("1.2.3.4.5.7.7")
    );
    this.approveOperationOnLeafAttribute(
        new PositiveIntegerAttributeValue(15),
        new PositiveIntegerAttributeValue(14)
    );
    this.approveOperationOnLeafAttribute(
        new StringAttributeValue("ExampleValue"),
        new StringAttributeValue("ExampleOtherValue")
    );
    this.approveOperationOnLeafAttribute(
        new TimeAttributeValue(Timestamp.valueOf("2016-05-21 12:53:40")),
        new TimeAttributeValue(Timestamp.valueOf("2016-05-21 12:55:00"))
    );
    this.approveOperationOnLeafAttribute(
        new UnsignedIntegerAttributeValue(15),
        new UnsignedIntegerAttributeValue(14)
    );
    this.approveOperationOnLeafAttribute(
        new UriAttributeValue("http://hl7.org/fhir/exampleType.html"),
        new UriAttributeValue("http://hl7.org/fhir/exampleOtherType.html")
    );
    this.approveOperationOnLeafAttribute(
        new UrlAttributeValue("http://geniolab.io/fhir/exampleType.html"),
        new UrlAttributeValue("http://geniolab.io/fhir/exampleOtherType.html")
    );
    this.approveOperationOnLeafAttribute(
        new UuidAttributeValue("ff7c0dc2-22da-423d-bf19-cf9d6fd9ddbd"),
        new UuidAttributeValue("597cd326-9396-4643-8178-e121ca95883f")
    );
  }

  protected void approveOperationOnLeafAttribute(
      AttributeValue<?> value1,
      AttributeValue<?> value2
  ) {
//    var otherAttributeValues = List.of(
//        new Base64BinaryAttributeValue("ZXhhbXBsZVdyb25nVmFsdWU="),
//        new BooleanAttributeValue(false),
//        new CanonicalAttributeValue("http://hl7.org/fhir/exampleType.html"),
//        new CodeAttributeValue("exampleValue"),
//        new DateAttributeValue(Timestamp.valueOf("2016-05-21 00:00:00")),
//        new DateTimeAttributeValue(Timestamp.valueOf("2016-05-21 12:53:40")),
//        new DecimalAttributeValue(5.0d),
//        new IdAttributeValue("exampleId"),
//        new InstantAttributeValue(Timestamp.from(Instant.now())),
//        new IntegerAttributeValue(-5),
//        new MarkdownAttributeValue("# Heading level 1"),
//        new OidAttributeValue("1.2.3.4.5.6.7"),
//        new PositiveIntegerAttributeValue(15),
//        new StringAttributeValue("exampleValue"),
//        new TimeAttributeValue(Timestamp.valueOf("2016-05-21 12:53:40")),
//        new UnsignedIntegerAttributeValue(14),
//        new UriAttributeValue("http://hl7.org/fhir/exampleType.html"),
//        new UrlAttributeValue("http://geniolab.io/fhir/exampleType.html"),
//        new UuidAttributeValue("ff7c0dc2-22da-423d-bf19-cf9d6fd9ddbd")
//    );
    var attributeContainer = this.getAttributeContainer();
    attributeContainer = attributeContainer.add(
        new LeafAttribute<>(
            "exampleAttributeName",
            Timestamp.from(Instant.now()),
            value1
        )
    );

    attributeContainer = attributeContainer.add(
        new LeafAttribute<>(
            "exampleAttributeName",
            Timestamp.from(Instant.now()),
            value1
        )
    );
    attributeContainer = attributeContainer.add(
        new LeafAttribute<>(
            "exampleAttributeName",
            Timestamp.from(Instant.now()),
            value2
        )
    );
    //When
    var actualAttributes = attributeContainer.getVersionedAttributes();
    //Then
    Assertions.assertTrue(actualAttributes.hasAttribute("exampleAttributeName"));
    var actualAttribute = actualAttributes.getVersionedAttribute(
        "exampleAttributeName"
    );
    Assertions.assertEquals(2, actualAttribute.getAttributeVersions().size());
    this.assertAttributesContainValue(value1.getValue(), actualAttribute);
    Assertions.assertEquals(value2.getValue(), actualAttribute.getCurrent().getValue());
  }

  private void assertAttributesContainValue(Object expectedAttributeValue,
      VersionedAttribute<?> actualAttributes) {
    Assertions.assertTrue(
        actualAttributes.streamAttributeVersions()
            .anyMatch(attribute -> attribute.getValue().equals(expectedAttributeValue)),
        "No attribute has the value: " + expectedAttributeValue);
  }
}
