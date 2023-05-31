package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ImmutableVersionedAttributeGroupTest extends AbstractVersionedAttributeGroupTest {

  @Override
  protected ImmutableVersionedAttributeGroup getTestedVersionedAttributesWith(
      Attribute<?>... attributes) {
    return new ImmutableVersionedAttributeGroup(
        attributes
    );
  }

  @Test
  public void itShouldReturnImmutableVersionedAttributeFromGetter() {
    var versionedAttributes = this.getTestedVersionedAttributes();
    var actualVersionedAttribute = versionedAttributes.getVersionedAttribute("test_string");
    Assertions.assertTrue(actualVersionedAttribute instanceof ImmutableVersionedAttribute);
  }

  @Test
  public void itShouldCreateEmpty() {
    var versionedAttributes = new ImmutableVersionedAttributeGroup();

    Assertions.assertEquals(0, versionedAttributes.numberOfUniqueAttributeNames());
  }

  @Test
  public void itCanMergeOtherVersionedAttributes() {
    var originalAttribute =
        new LeafAttribute<>("original", new StringAttributeValue("original value"));
    var originalBAttribute =
        new LeafAttribute<>("original", new StringAttributeValue("original value"));
    var updatedOldAttribute = new LeafAttribute<>("updated", new StringAttributeValue("old value"));
    var updatedNewAttribute =
        new LeafAttribute<>("updated", new StringAttributeValue("updated value"));
    var newAttribute =
        new LeafAttribute<>("new", new StringAttributeValue("new value"));
    var originalVersionedAttributes = new ImmutableVersionedAttributeGroup(
        originalAttribute,
        updatedOldAttribute
    );
    var newVersionedAttributes = new ImmutableVersionedAttributeGroup(
        updatedNewAttribute,
        originalBAttribute,
        newAttribute
    );

    var actualVersionedAttributes =
        originalVersionedAttributes.mergeOverwrite(newVersionedAttributes);

    var expectedVersionedAttributes = new ImmutableVersionedAttributeGroup(
        originalAttribute,
        updatedOldAttribute,
        updatedNewAttribute,
        newAttribute
    );

    Assertions.assertEquals(3, actualVersionedAttributes.numberOfUniqueAttributeNames());
    Assertions.assertEquals(expectedVersionedAttributes, actualVersionedAttributes);
  }
}