package ai.stapi.graph.versionedAttributes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.attribute.exceptions.AttributeNotFoundException;
import ai.stapi.graph.test.base.UnitTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

abstract public class AbstractVersionedAttributeGroupTest extends UnitTestCase {

  protected VersionedAttributeGroup getTestedVersionedAttributes() {
    return this.getTestedVersionedAttributesWith(
        new LeafAttribute<>("test_boolean", new BooleanAttributeValue(true)),
        new LeafAttribute<>("test_double", new DecimalAttributeValue(10d)),
        new LeafAttribute<>("test_integer", new IntegerAttributeValue(15)),
        new LeafAttribute<>("test_string", new StringAttributeValue("version1")),
        new LeafAttribute<>("test_string", new StringAttributeValue("version2"))
    );
  }

  abstract protected VersionedAttributeGroup getTestedVersionedAttributesWith(
      Attribute<?>... attributes);

  @Test
  public void itShouldBeCreatedByListOfAttributes() {
    var versionedAttributes = getTestedVersionedAttributes();

    Assertions.assertTrue(versionedAttributes.hasAttribute("test_boolean"));
    Assertions.assertTrue(versionedAttributes.hasAttribute("test_double"));
    Assertions.assertTrue(versionedAttributes.hasAttribute("test_integer"));
    Assertions.assertTrue(versionedAttributes.hasAttribute("test_string"));
    Assertions.assertEquals(-955124571, versionedAttributes.hashCode());
  }

  @Test
  public void itShouldFailOnAttributeNotFound() {
    var versionedAttributes = getTestedVersionedAttributes();

    Executable executable = () -> versionedAttributes.getCurrentAttribute("not_existing_name");
    Assertions.assertThrows(AttributeNotFoundException.class, executable);
  }

  @Test
  public void itShouldGetCurrentAttribute() {
    var versionedAttributes = getTestedVersionedAttributes();

    Assertions.assertEquals(true,
        versionedAttributes.getCurrentAttribute("test_boolean").getValue());
    Assertions.assertEquals(10d, versionedAttributes.getCurrentAttribute("test_double").getValue());
    Assertions.assertEquals(15, versionedAttributes.getCurrentAttribute("test_integer").getValue());
    Assertions.assertEquals("version2",
        versionedAttributes.getCurrentAttribute("test_string").getValue());
  }

  @Test
  public void itShouldGetCurrentAttributeValue() {
    var versionedAttributes = getTestedVersionedAttributes();

    Assertions.assertEquals(true, versionedAttributes.getCurrentAttributeValue("test_boolean"));
    Assertions.assertEquals(10d, versionedAttributes.getCurrentAttributeValue("test_double"));
    Assertions.assertEquals(15, versionedAttributes.getCurrentAttributeValue("test_integer"));
    Assertions.assertEquals("version2",
        versionedAttributes.getCurrentAttributeValue("test_string"));
  }

  @Test
  public void itShouldAnswerWhetherIsValuePresentInAnyVersion() {
    var versionedAttributes = getTestedVersionedAttributes();

    assertTrue(versionedAttributes.isValuePresentInAnyVersion("test_string", "version1"));
    assertFalse(versionedAttributes.isValuePresentInAnyVersion("different_name", "irrelevant"));
  }

}
