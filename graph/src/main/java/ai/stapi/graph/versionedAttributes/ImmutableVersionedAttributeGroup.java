package ai.stapi.graph.versionedAttributes;

import ai.stapi.graph.attribute.Attribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImmutableVersionedAttributeGroup extends AbstractVersionedAttributeGroup {

  public static final String SERIALIZATION_TYPE = "ImmutableVersionedAttributeGroup";

  public ImmutableVersionedAttributeGroup() {
    super();
  }

  public ImmutableVersionedAttributeGroup(Attribute<?>... attributes) {
    super();
    this.rawMap = addAttributesToEmptyMap(Arrays.stream(attributes).toList());
  }

  public ImmutableVersionedAttributeGroup(List<Attribute<?>> attributes) {
    super();
    this.rawMap = addAttributesToEmptyMap(attributes);
  }

  public ImmutableVersionedAttributeGroup(
      HashMap<String, VersionedAttribute<?>> rawMap
  ) {
    super(rawMap);
  }

  private HashMap<String, VersionedAttribute<?>> addAttributesToEmptyMap(
      List<Attribute<?>> attributes) {
    HashMap<String, VersionedAttribute<?>> map = new HashMap<>();
    for (Attribute<?> attribute : attributes) {
      map = addAttributeToRawMap(attribute, map);
    }
    return map;
  }

  public VersionedAttributeGroup add(Attribute<?> attribute) {
    var newMap = this.addAttributeToRawMap(attribute, this.rawMap);
    return new ImmutableVersionedAttributeGroup(newMap);
  }

  public VersionedAttributeGroup mergeOverwrite(VersionedAttributeGroup other) {
    var newrawMap = new HashMap<>(this.rawMap);
    other.getVersionedAttributeList().forEach(
        (otherVersionedAttribute) -> {
          var otherName = otherVersionedAttribute.getName();
          if (newrawMap.containsKey(otherName)) {
            newrawMap.put(
                otherName, newrawMap.get(otherName).mergeOverwrite(otherVersionedAttribute)
            );
          } else {
            newrawMap.put(
                otherName,
                otherVersionedAttribute
            );
          }
        }
    );
    return new ImmutableVersionedAttributeGroup(newrawMap);
  }
}
