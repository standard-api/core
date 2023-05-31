package ai.stapi.schema.scopeProvider;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScopeCacher {

  private final ScopeProvider scopeProvider;
  private final Map<ScopeOptions, Map<Class<?>, Object>> cache;

  public ScopeCacher(ScopeProvider scopeProvider) {
    this.scopeProvider = scopeProvider;
    this.cache = new ConcurrentHashMap<>();
  }

  public void cache(Class<?> consumerClass, Object cachedData) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    scoped.put(consumerClass, cachedData);
  }

  public boolean hasCached(Class<?> consumerClass) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    return scoped.containsKey(consumerClass);
  }

  public <T> T getCachedOrCompute(
      Class<?> consumerClass,
      ComputeFunction<T> computeFunction
  ) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    var current = (T) scoped.get(consumerClass);
    if (current != null) {
      return current;
    }
    var computed = computeFunction.compute(currentScope);
    scoped.put(consumerClass, computed);
    return computed;
  }

  @Nullable
  public <T> T getCached(Class<?> consumerClass) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    return (T) scoped.get(consumerClass);
  }

  public <T> T recompute(
      Class<?> consumerClass,
      T initialValue,
      RecomputeWithInitialValueFunction<T> computeFunction
  ) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    var previous = (T) scoped.get(consumerClass);
    var newCache = computeFunction.compute(
        currentScope,
        previous == null ? initialValue : previous
    );
    scoped.put(consumerClass, newCache);

    return newCache;
  }

  public <T> T recompute(
      Class<?> consumerClass,
      RecomputeFunction<T> computeFunction
  ) {
    var currentScope = this.scopeProvider.provide();
    var scoped = this.getCurrentScopeCache(currentScope);
    var previous = (T) scoped.get(consumerClass);
    var newCache = computeFunction.compute(
        currentScope,
        previous == null ? Optional.empty() : Optional.of(previous)
    );
    scoped.put(consumerClass, newCache);

    return newCache;
  }

  @NotNull
  private Map<Class<?>, Object> getCurrentScopeCache(ScopeOptions currentScope) {
    return this.cache.computeIfAbsent(
        currentScope,
        key -> new ConcurrentHashMap<>()
    );
  }

  public interface ComputeFunction<T> {

    T compute(ScopeOptions scopeOptions);
  }

  public interface RecomputeFunction<T> {

    T compute(ScopeOptions scopeOptions, Optional<T> previous);
  }

  public interface RecomputeWithInitialValueFunction<T> {

    T compute(ScopeOptions scopeOptions, T previous);
  }
}
