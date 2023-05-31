package ai.stapi.graph.attribute.attributeValue;

import java.io.Serializable;

public interface AttributeValue<T> extends Serializable, Comparable<AttributeValue<?>> {

  T getValue();

  String getDataType();
}
