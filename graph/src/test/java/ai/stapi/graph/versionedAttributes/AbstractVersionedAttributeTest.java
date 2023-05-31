package ai.stapi.graph.versionedAttributes;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.graph.versionedAttributes.exceptions.CannotAddNewVersionOfAttribute;
import ai.stapi.graph.versionedAttributes.exceptions.VersionedAttributeCannotBeEmpty;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

abstract public class AbstractVersionedAttributeTest extends UnitTestCase {

  abstract protected VersionedAttribute<Attribute<?>> createVersionedAttribute(
      Attribute<?>... attributes);

  @Test
  public void itCannotCreateEmptyVersionedAttribute() {
    Executable throwing = () -> this.createVersionedAttribute();
    assertThrows(VersionedAttributeCannotBeEmpty.class, throwing);
  }

  @Test
  public void itCanNotBeCreatedWithAttributesOfDifferentName() {

    var attributeA = new LeafAttribute<>("type_String", new StringAttributeValue("value_A"));
    var attributeB = new LeafAttribute<>("type_Boolean", new BooleanAttributeValue(false));

    Executable throwing = () -> this.createVersionedAttribute(attributeA, attributeB);
    assertThrows(CannotAddNewVersionOfAttribute.class, throwing);
  }

  @Test
  public void itCanBeCreatedWithAttributesOfSameNameAndSameType() {

    var expectedVersion1 = new LeafAttribute<>("type_String", new StringAttributeValue("version1"));
    var expectedVersion2 = new LeafAttribute<>("type_String", new StringAttributeValue("version2"));

    var actual = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );

    var expectedList = List.of(expectedVersion2, expectedVersion1);
    var expectedCopy = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );

    Assertions.assertTrue(actual.containsValue(expectedVersion1.getValue()));
    Assertions.assertTrue(actual.containsValue(expectedVersion2.getValue()));
    Assertions.assertEquals(expectedVersion2, actual.getCurrent());
    Assertions.assertEquals(expectedList, actual.getAttributeVersions());
    Assertions.assertEquals(expectedList, actual.streamAttributeVersions().toList());
    var iterator = actual.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1.getValue(), iterator.next().getValue());
    Assertions.assertEquals(expectedVersion2.getValue(), iterator.next().getValue());
    Assertions.assertEquals(2, actual.numberOfVersions());
    Assertions.assertEquals(expectedCopy, actual);
    Assertions.assertEquals(expectedCopy.hashCode(), actual.hashCode());
    Assertions.assertEquals(expectedVersion1.getName(), actual.getName());
  }
  @Test
  public void itCanBeCreatedWithAttributesOfSameNameAndDifferentType() {

    var expectedVersion1 = new LeafAttribute<>("name", new BooleanAttributeValue(false));
    var expectedVersion2 = new LeafAttribute<>("name", new StringAttributeValue("version2"));

    var actual = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );

    var expectedList = List.of(expectedVersion2, expectedVersion1);
    var expectedCopy = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );

    Assertions.assertTrue(actual.containsValue(expectedVersion1.getValue()));
    Assertions.assertTrue(actual.containsValue(expectedVersion2.getValue()));
    Assertions.assertEquals(expectedVersion2, actual.getCurrent());
    Assertions.assertEquals(expectedList, actual.getAttributeVersions());
    Assertions.assertEquals(expectedList, actual.streamAttributeVersions().toList());
    var iterator = actual.iterateVersionsFromOldest();
    Assertions.assertEquals(expectedVersion1.getValue(), iterator.next().getValue());
    Assertions.assertEquals(expectedVersion2.getValue(), iterator.next().getValue());
    Assertions.assertEquals(2, actual.numberOfVersions());
    Assertions.assertEquals(expectedCopy, actual);
    Assertions.assertEquals(expectedCopy.hashCode(), actual.hashCode());
    Assertions.assertEquals(expectedVersion1.getName(), actual.getName());
  }

  @Test
  public void itShouldProperlyCheckForEquality() {

    var expectedVersion1 = new LeafAttribute<>("type_String", new StringAttributeValue("version1"));
    var expectedVersion2 = new LeafAttribute<>("type_String", new StringAttributeValue("version2"));

    var actual = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );
    var same = this.createVersionedAttribute(
        expectedVersion1,
        expectedVersion2
    );

    Assertions.assertTrue(actual.equals(same));
    Assertions.assertFalse(actual.equals(new HashMap()));
    Assertions.assertTrue(actual.equals(actual));
  }
}
