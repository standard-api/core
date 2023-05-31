package ai.stapi.graph.attribute;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

public interface Attribute<T> extends Serializable {

  String getName();

  Timestamp getCreatedAt();

  void setCreatedAt(Timestamp createdAt);

  Map<String, MetaData> getMetaData();

  T getValue();

  Attribute<T> getCopy();
}
