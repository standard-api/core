package ai.stapi.graph.attribute;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractAttribute<T> implements Attribute<T> {

  private final String name;
  private final Map<String, MetaData> metaData;

  protected AbstractAttribute(String name) {
    this.name = name;
    this.metaData = new HashMap<>();
  }

  protected AbstractAttribute(String name, Instant createdAt) {
    this.name = name;
    this.metaData = new HashMap<>(
        Map.of(CreatedAtMetaData.NAME, new CreatedAtMetaData(createdAt))
    );
  }

  protected AbstractAttribute(String name, Map<String, MetaData> metaData) {
    this.name = name;
    this.metaData = new HashMap<>(metaData);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Instant getCreatedAt() {
    var createdAt = this.metaData.get(CreatedAtMetaData.NAME);
    if (createdAt == null) {
      return null;
    }
    return CreatedAtMetaData
        .fromMetaData(createdAt)
        .getInstant();
  }

  @Override
  public void setCreatedAt(Instant createdAt) {
    this.metaData.put(
        CreatedAtMetaData.NAME,
        new CreatedAtMetaData(createdAt)
    );
  }

  @Override
  public Map<String, MetaData> getMetaData() {
    return this.metaData;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AbstractAttribute<?> otherAttribute)) {
      return false;
    }
    return this.getName().equals(otherAttribute.getName()) &&
        this.getValue().equals(otherAttribute.getValue());
  }

  @Override
  //TODO: Why null?
  public int hashCode() {
    return Objects.hash(getName(), getValue(), null);
  }
}
