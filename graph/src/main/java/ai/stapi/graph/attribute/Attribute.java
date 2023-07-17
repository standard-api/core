package ai.stapi.graph.attribute;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public interface Attribute<T> extends Serializable {

  String getName();

  Instant getCreatedAt();

  void setCreatedAt(Instant createdAt);

  Map<String, MetaData> getMetaData();

  T getValue();

  Attribute<T> getCopy();
}
