package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.versionedAttributes.exceptions.CannotAddNewVersionOfAttribute;
import ai.stapi.graph.versionedAttributes.exceptions.CannotMergeTwoVersionedAttributes;
import java.sql.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


public class ImmutableVersionedAttributeTest extends AbstractVersionedAttributeTest {

  @Override
  protected VersionedAttribute<Attribute<?>> createVersionedAttribute(Attribute<?>... attributes) {
    return new ImmutableVersionedAttribute<>(attributes);
  }
  
  @Test
  public void itShouldPreventAddingNewVersionOfDifferentNames() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name", 
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "different_name", 
        new StringAttributeValue("version2")
    );
    var actualVersionedAttribute = this.createVersionedAttribute(
        expectedVersion1
    );
    //When
    Executable executable = () -> actualVersionedAttribute.add(expectedVersion2);
    //Then
    Assertions.assertThrows(CannotAddNewVersionOfAttribute.class, executable);
  }

  @Test
  public void itCanAddNewVersion() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name", 
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name", 
        new StringAttributeValue("version2")
    );
    var initialVersionedAttribute = this.createVersionedAttribute(
        expectedVersion1
    );
    //When
    var actualVersionedAttribute = initialVersionedAttribute.add(expectedVersion2);
    //Then
    var iterator = actualVersionedAttribute.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1, iterator.next());
    Assertions.assertEquals(expectedVersion2, iterator.next());
  }

  @Test
  void itCanAddNewVersionOfDifferentDataType() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name",
        new IntegerAttributeValue(2)
    );
    var initialAttribute = this.createVersionedAttribute(
        expectedVersion1
    );
    //When
    var actual = initialAttribute.add(expectedVersion2);
    //Then
    var iterator = actual.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1, iterator.next());
    Assertions.assertEquals(expectedVersion2, iterator.next());
  }

  @Test
  public void itCanAddManyNewVersionsAndAutomaticallyAttachTimestampIfNeeded() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name", 
        Timestamp.valueOf("2022-01-01 01:01:01"),
        new StringAttributeValue("versionD-1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name", 
        Timestamp.valueOf("2022-01-01 01:02:01"),
        new StringAttributeValue("versionS-2")
    );
    var expectedVersion3 = new LeafAttribute<>(
        "test_name", 
        Timestamp.valueOf("2022-01-01 01:03:01"),
        new StringAttributeValue("versionX-3")
    );
    var expectedVersion4 = new LeafAttribute<>(
        "test_name", 
        Timestamp.valueOf("2022-01-01 01:04:01"),
        new StringAttributeValue("versionU-4")
    );
    var expectedVersion5 = new LeafAttribute<>(
        "test_name", 
        new StringAttributeValue("versionF-5")
    );
    var actualVersionedAttribute = this.createVersionedAttribute(
        expectedVersion5
    );
    actualVersionedAttribute = actualVersionedAttribute.add(expectedVersion4);
    actualVersionedAttribute = actualVersionedAttribute.add(expectedVersion3);
    actualVersionedAttribute = actualVersionedAttribute.add(expectedVersion2);
    //When
    actualVersionedAttribute = actualVersionedAttribute.add(expectedVersion1);
    //Then
    var iterator = actualVersionedAttribute.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1, iterator.next());
    Assertions.assertEquals(expectedVersion2, iterator.next());
    Assertions.assertEquals(expectedVersion3, iterator.next());
    Assertions.assertEquals(expectedVersion4, iterator.next());
    Assertions.assertEquals(expectedVersion5, iterator.next());

    Assertions.assertEquals(expectedVersion5, actualVersionedAttribute.getCurrent());
  }

  @Test
  void itShouldMergeWithVersionedAttributeOfDifferentType() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version2")
    );
    var firstVersionedAttribute = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );
    var expectedVersion3 = new LeafAttribute<>(
        "test_name",
        new BooleanAttributeValue(false)
    );
    var secondVersionedAttribute = this.createVersionedAttribute(
        expectedVersion3
    );
    //When
    var actual = firstVersionedAttribute.mergeOverwrite(secondVersionedAttribute);
    //Then
    var iterator = actual.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1, iterator.next());
    Assertions.assertEquals(expectedVersion2, iterator.next());
    Assertions.assertEquals(expectedVersion3, iterator.next());

    Assertions.assertEquals(expectedVersion3, actual.getCurrent());
  }

  @Test
  public void itShouldNotMergeWithVersionedAttributeOfDifferentName() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version2")
    );
    var firstVersionedAttribute = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );
    var secondVersionedAttribute = this.createVersionedAttribute(
        new LeafAttribute<>(
            "different_name",
            new StringAttributeValue("version3")
        )
    );
    //When
    Executable executable = () -> firstVersionedAttribute.mergeOverwrite(secondVersionedAttribute);
    //Then
    Assertions.assertThrows(CannotMergeTwoVersionedAttributes.class, executable);
  }

  @Test
  public void itShouldMergeWithOtherVersionedAttribute() {
    //Given
    var expectedVersion1 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version1")
    );
    var expectedVersion2 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version2")
    );
    var firstVersionedAttribute = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );
    var expectedVersion3 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version3")
    );
    var expectedVersion4 = new LeafAttribute<>(
        "test_name",
        new StringAttributeValue("version4")
    );
    var secondVersionedAttribute = this.createVersionedAttribute(
        expectedVersion3,
        expectedVersion4
    );
    //When
    var actualVersionedAttribute = firstVersionedAttribute.mergeOverwrite(secondVersionedAttribute);
    //Then
    var iterator = actualVersionedAttribute.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1.getValue(), iterator.next().getValue());
    Assertions.assertEquals(expectedVersion2.getValue(), iterator.next().getValue());
    Assertions.assertEquals(expectedVersion3.getValue(), iterator.next().getValue());
    Assertions.assertEquals(expectedVersion4.getValue(), iterator.next().getValue());
  }
}