package ai.stapi.schema.scopeProvider;

import java.util.List;

public class SpringScopeProvider implements ScopeProvider {

  private ScopeOptions scopeOptions;

  public SpringScopeProvider(List<ScopeOptions> autowiredScopes) {
    this.scopeOptions = autowiredScopes.stream()
        .reduce(ScopeOptions::with)
        .orElse(new ScopeOptions());
  }

  @Override
  public ScopeOptions provide() {
    return this.scopeOptions;
  }

  @Override
  public void set(ScopeOptions scopeOptions) {
    this.scopeOptions = scopeOptions;
  }
}
