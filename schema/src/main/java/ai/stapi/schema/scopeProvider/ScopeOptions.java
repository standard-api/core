package ai.stapi.schema.scopeProvider;

import java.util.HashSet;
import java.util.List;

public class ScopeOptions {

  public static final String DOMAIN_TAG = "domain";
  public static final String TEST_TAG = "test";
  private List<String> scopes;
  private List<String> tags;

  public ScopeOptions() {
    this.scopes = List.of();
    this.tags = List.of();
  }

  public ScopeOptions(List<String> scopes, List<String> tags) {
    this.scopes = scopes;
    this.tags = tags;
  }

  public ScopeOptions(String scope, String tag) {
    this.scopes = List.of(scope);
    this.tags = List.of(tag);
  }

  public ScopeOptions with(ScopeOptions scopeOptions) {
    var newScopes = new HashSet<>(this.scopes);
    newScopes.addAll(scopeOptions.getScopes());
    var newTags = new HashSet<>(this.tags);
    newTags.addAll(scopeOptions.getTags());
    return new ScopeOptions(
        newScopes.stream().toList(),
        newTags.stream().toList()
    );
  }

  public List<String> getScopes() {
    return scopes;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getStringHash() {
    return scopes.toString() + tags.toString();
  }

  public String toReadableString() {
    return "ScopeOptions{" +
        "scopes=" + scopes +
        ", tags=" + tags +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ScopeOptions that = (ScopeOptions) o;

    if (scopes != null ? !scopes.equals(that.scopes) : that.scopes != null) {
      return false;
    }
    return tags != null ? tags.equals(that.tags) : that.tags == null;
  }

  @Override
  public int hashCode() {
    int result = scopes != null ? scopes.hashCode() : 0;
    result = 31 * result + (tags != null ? tags.hashCode() : 0);
    return result;
  }
}
