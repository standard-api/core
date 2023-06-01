package ai.stapi.graph;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.exceptions.GraphException;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import java.io.Serializable;

public interface AttributeContainer extends Serializable {

  AttributeContainer add(Attribute<?> attribute);

  VersionedAttribute<?> getVersionedAttribute(String name);

  Attribute<?> getAttribute(String name) throws GraphException;

  boolean containsAttribute(String name, Object value);

  boolean containsAttribute(String name);

  Object getAttributeValue(String name);

  boolean hasAttribute(String name);

  VersionedAttributeGroup getVersionedAttributes();
}
