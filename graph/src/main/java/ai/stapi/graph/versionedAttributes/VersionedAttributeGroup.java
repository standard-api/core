package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import java.util.List;

public interface VersionedAttributeGroup {

  Attribute<?> getCurrentAttribute(String name);

  Object getCurrentAttributeValue(String name);

  boolean hasAttribute(String name);

  boolean isValuePresentInAnyVersion(String attributeName, Object attributeValue);

  int numberOfUniqueAttributeNames();

  VersionedAttribute<?> getVersionedAttribute(String name);

  List<VersionedAttribute<?>> getVersionedAttributeList();

  VersionedAttributeGroup add(Attribute<?> attribute);

  VersionedAttributeGroup mergeOverwrite(VersionedAttributeGroup other);

  @Override
  boolean equals(Object other);

  @Override
  int hashCode();
}
