package ai.stapi.graph.renderer.infrastructure.responseRenderer.responseGraph;

import java.util.ArrayList;
import java.util.Objects;

public class ResponseAttribute<T> {

  private String name;
  private ArrayList<ResponseAttributeVersion<T>> values;

  private ResponseAttribute() {
  }

  public ResponseAttribute(String name, ArrayList<ResponseAttributeVersion<T>> values) {
    this.name = name;
    this.values = values;
  }

  public String getName() {
    return name;
  }

  public ArrayList<ResponseAttributeVersion<T>> getValues() {
    return values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseAttribute<?> that = (ResponseAttribute<?>) o;
    return name.equals(that.name) && values.equals(that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, values);
  }
}
