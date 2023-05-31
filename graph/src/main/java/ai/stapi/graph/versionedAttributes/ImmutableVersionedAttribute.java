package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import java.util.Arrays;
import java.util.List;

public class ImmutableVersionedAttribute<T extends Attribute<?>>
    extends AbstractVersionedAttribute<T> {

  public static final String SERIALIZATION_TYPE = "ImmutableVersionedAttribute";

  private ImmutableVersionedAttribute() {
  }

  @SafeVarargs
  public ImmutableVersionedAttribute(T... attributeVersions) {
    this(Arrays.stream(attributeVersions).toList());
  }

  public ImmutableVersionedAttribute(List<T> attributeVersions) {
    super(attributeVersions);
  }

  public ImmutableVersionedAttribute(List<T> attributeVersions, Attribute<?> attribute) {
    super(attributeVersions, attribute);
  }

  public VersionedAttribute<T> add(Attribute<?> attribute) {
    return new ImmutableVersionedAttribute<>(this.attributeVersions, attribute);
  }

  public VersionedAttribute<T> mergeOverwrite(VersionedAttribute<?> other) {
    return this.protectedMergeOverwrite(other);
  }
}
