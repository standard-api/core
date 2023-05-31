package ai.stapi.identity;

import java.io.Serializable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class UniqueIdentifier implements Comparable<UniqueIdentifier>, Serializable {

  private String id;

  protected UniqueIdentifier() {
  }

  public UniqueIdentifier(String id) {
    this.id = id;
  }

  public static UniqueIdentifier fromString(String string) {
    return new UniqueIdentifier(string);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UniqueIdentifier that)) {
      return false;
    }
    return this.toString().equals(that.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public String getId() {
    return this.toString();
  }

  @Override
  public String toString() {
    return this.id;
  }

  @Override
  public int compareTo(@NotNull UniqueIdentifier o) {
    return this.id.compareTo(o.toString());
  }
}
