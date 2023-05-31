package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.exceptions.AttributeNotFoundException;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class AbstractVersionedAttributeGroup implements VersionedAttributeGroup {

  protected HashMap<String, VersionedAttribute<?>> rawMap;

  public AbstractVersionedAttributeGroup() {
    this.rawMap = new HashMap<>();
  }

  public AbstractVersionedAttributeGroup(HashMap<String, VersionedAttribute<?>> rawMap) {
    this.rawMap = rawMap;
  }

  @Override
  public Attribute<?> getCurrentAttribute(String name) {
    if (!this.rawMap.containsKey(name)) {
      throw new AttributeNotFoundException(name);
    }
    return this.rawMap.get(name).getCurrent();
  }

  @Override
  public Object getCurrentAttributeValue(String name) {
    return this.getCurrentAttribute(name).getValue();
  }

  @Override
  public boolean hasAttribute(String name) {
    return this.rawMap.containsKey(name);
  }

  @Override
  public boolean isValuePresentInAnyVersion(String attributeName, Object attributeValue) {
    if (!this.rawMap.containsKey(attributeName)) {
      return false;
    }
    return this.rawMap.get(attributeName).containsValue(attributeValue);
  }

  @Override
  public VersionedAttribute<?> getVersionedAttribute(String name) {
    return this.rawMap.get(name);
  }

  @Override
  public List<VersionedAttribute<?>> getVersionedAttributeList() {
    return ImmutableList.copyOf(this.rawMap.values());
  }

  @Override
  public int numberOfUniqueAttributeNames() {
    return this.rawMap.size();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AbstractVersionedAttributeGroup otherAbstractVersionedAttributeGroup)) {
      return false;
    }
    return rawMap.equals(otherAbstractVersionedAttributeGroup.rawMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rawMap);
  }

  protected HashMap<String, VersionedAttribute<?>> addAttributeToRawMap(
      Attribute<?> attribute,
      HashMap<String, VersionedAttribute<?>> rawMap
  ) {
    HashMap<String, VersionedAttribute<?>> newMap = new HashMap<>(rawMap);
    if (newMap.containsKey(attribute.getName())) {
      var versionedAttribute = newMap.get(attribute.getName());
      var newVersionedAttribute = versionedAttribute.add(attribute);
      newMap.put(attribute.getName(), newVersionedAttribute);
    } else {
      newMap.put(
          attribute.getName(),
          new ImmutableVersionedAttribute<>(attribute)
      );
    }
    return newMap;
  }


}
