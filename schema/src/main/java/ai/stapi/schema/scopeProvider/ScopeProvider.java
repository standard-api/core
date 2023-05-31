package ai.stapi.schema.scopeProvider;


public interface ScopeProvider {

  ScopeOptions provide();

  void set(ScopeOptions scopeOptions);
  
  default void set(String scope, String tag) {
    this.set(new ScopeOptions(scope, tag));
  }
}
