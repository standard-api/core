package ai.stapi.graph.attribute;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractAttributeContainer implements AttributeContainer {

  private final VersionedAttributeGroup versionedAttributes;

  protected AbstractAttributeContainer() {
    this.versionedAttributes = new ImmutableVersionedAttributeGroup();
  }

  protected AbstractAttributeContainer(VersionedAttributeGroup versionedAttributes) {
    this.versionedAttributes = versionedAttributes;
  }

  protected AbstractAttributeContainer(Attribute<?>... attributes) {
    this(new ImmutableVersionedAttributeGroup(attributes));
  }

  @Override
  public <T extends Attribute<?>> AttributeContainer add(T attribute) {
    var newAttributes = this.versionedAttributes.add(attribute);
    return this.withNewAttributes(newAttributes);
  }

  protected abstract AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes);

  public VersionedAttributeGroup getVersionedAttributes() {
    return versionedAttributes;
  }


  public List<VersionedAttribute<?>> getVersionedAttributeList() {
    return versionedAttributes.getVersionedAttributeList();
  }

  public List<Attribute<?>> getFlattenAttributes() {
    return this.versionedAttributes.getVersionedAttributeList().stream()
        .flatMap(VersionedAttribute::streamAttributeVersions)
        .collect(Collectors.toList());
  }

  @Override
  public VersionedAttribute<?> getVersionedAttribute(String name) {
    return versionedAttributes.getVersionedAttribute(name);
  }

  @Override
  public Attribute<?> getAttribute(String name) throws GraphException {
    return this.versionedAttributes.getCurrentAttribute(name);
  }

  @Override
  public boolean containsAttribute(String name, Object value) {
    return this.versionedAttributes.isValuePresentInAnyVersion(name, value);
  }

  @Override
  public boolean containsAttribute(String name) {
    return this.versionedAttributes.hasAttribute(name);
  }

  @Override
  public Object getAttributeValue(String name) {
    return this.versionedAttributes.getCurrentAttributeValue(name);
  }

  public AttributeContainer mergeAttributesWithAttributesOf(AbstractAttributeContainer other) {
    return this.withNewAttributes(
        this.versionedAttributes.mergeOverwrite(other.getVersionedAttributes()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractAttributeContainer that)) {
      return false;
    }
    return getVersionedAttributes().equals(that.getVersionedAttributes());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getVersionedAttributes()) + this.getHashCodeWithoutAttributes();
  }

  public int getIdlessHashCode() {
    VersionedAttributeGroup versionedAttributes1 = this.getVersionedAttributes();
    int idlessHashCodeWithoutAttributes = this.getIdlessHashCodeWithoutAttributes();
    return Objects.hash(versionedAttributes1) + idlessHashCodeWithoutAttributes;
  }

  protected abstract int getHashCodeWithoutAttributes();

  protected abstract int getIdlessHashCodeWithoutAttributes();

  @Override
  public boolean hasAttribute(String name) {
    return this.versionedAttributes.hasAttribute(name);
  }

  public int getAttributeCount() {
    return this.versionedAttributes.numberOfUniqueAttributeNames();
  }


  public String guessBestName() {
    if (this.hasAttribute("name")) {
      return (String) this.getAttribute("name").getValue();
    }
    return null;
  }
}
