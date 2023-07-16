package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.versionedAttributes.exceptions.CannotAddNewVersionOfAttribute;
import ai.stapi.graph.versionedAttributes.exceptions.CannotMergeTwoVersionedAttributes;
import ai.stapi.graph.versionedAttributes.exceptions.VersionedAttributeCannotBeEmpty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractVersionedAttribute<T extends Attribute<?>>
    implements VersionedAttribute<T> {

  protected String attributeName;
  protected List<T> attributeVersions;

  protected AbstractVersionedAttribute() {
  }

  protected AbstractVersionedAttribute(List<T> attributeVersions) {
    this();
    if (attributeVersions.isEmpty()) {
      throw new VersionedAttributeCannotBeEmpty();
    }
    this.attributeName = attributeVersions.get(0).getName();
    List<T> newAttributeVersions = new ArrayList<>();
    for (T attributeVersion : attributeVersions) {
      newAttributeVersions = protectedAddToList(attributeVersion, newAttributeVersions);
    }
    this.attributeVersions = ImmutableList.copyOf(newAttributeVersions);
  }

  protected AbstractVersionedAttribute(List<T> attributeVersions, Attribute<?> attribute) {
    this();
    this.attributeName = attributeVersions.get(0).getName();

    this.attributeVersions = this.protectedAddToList(attribute, attributeVersions);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.attributeVersions.stream()
        .anyMatch(attribute -> attribute.getValue().equals(value));
  }

  @Override
  public T getCurrent() {
    return this.attributeVersions.get(0);
  }

  protected VersionedAttribute<T> protectedMergeOverwrite(VersionedAttribute<?> other) {
    if (!this.getName().equals(other.getName())) {
      throw CannotMergeTwoVersionedAttributes.becauseTheyHaveDifferentNames(
          this,
          other
      );
    }
    var updatedVersions = new ArrayList<>(this.attributeVersions);
    //TODO Check Structure Type
    updatedVersions.addAll(
        other.streamAttributeVersions()
            .map(attribute -> (T) attribute)
            .filter(otherAttribute -> !this.attributeVersions.contains(otherAttribute))
            .toList()
    );

    return new ImmutableVersionedAttribute<T>(updatedVersions);
  }

  @Override
  public List<T> getAttributeVersions() {
    return attributeVersions;
  }

  @Override
  public Stream<T> streamAttributeVersions() {
    return this.attributeVersions.stream();
  }

  @Override
  public Iterator<T> iterateVersionsFromOldest() {
    return Lists.reverse(this.attributeVersions).iterator();
  }

  @Override
  public int numberOfVersions() {
    return attributeVersions.size();
  }

  @Override
  public String getName() {
    return this.attributeName;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof VersionedAttribute<?> otherVersionedAttribute)) {
      return false;
    }
    return this.getAttributeVersions().equals(otherVersionedAttribute.getAttributeVersions());
  }

  @Override
  public int hashCode() {
    return Objects.hash(Lists.reverse(this.getAttributeVersions()));
  }

  protected List<T> protectedAddToList(Attribute<?> attribute, List<T> currentList) {
    if (currentList == null) {
      return currentList;
    }
    List<T> newAttributeList = new ArrayList<>(currentList);
    T mostRecentFromCurrentList = null;
    if (!currentList.isEmpty()) {
      mostRecentFromCurrentList = currentList.get(0);
      if (attribute.getValue().equals(mostRecentFromCurrentList.getValue())) {
        return newAttributeList;
      }
      if (!attribute.getName().equals(mostRecentFromCurrentList.getName())) {
        throw CannotAddNewVersionOfAttribute.becauseNewVersionHasDifferentNames(
            mostRecentFromCurrentList.getName(),
            attribute.getName()
        );
      }
    }
    if (currentList.isEmpty() || !Objects.requireNonNull(mostRecentFromCurrentList)
        .equals(attribute.getValue())) {
      if (attribute.getCreatedAt() == null) {
        attribute.setCreatedAt(Instant.now());
      }
      newAttributeList.add((T) attribute);
      newAttributeList.sort((a1, a2) -> a2.getCreatedAt().compareTo(a1.getCreatedAt()));
    }

    return newAttributeList;
  }
}
