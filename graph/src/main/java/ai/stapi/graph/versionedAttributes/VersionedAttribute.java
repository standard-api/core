package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public interface VersionedAttribute<T extends Attribute<?>>  {

  T getCurrent();

  String getName();

  List<T> getAttributeVersions();

  Iterator<T> iterateVersionsFromOldest();

  Stream<T> streamAttributeVersions();

  boolean containsValue(Object value);

  int numberOfVersions();

  @Override
  boolean equals(Object other);

  @Override
  int hashCode();

  VersionedAttribute<T> add(Attribute<?> attribute);

  VersionedAttribute<T> mergeOverwrite(VersionedAttribute<?> other);

}
