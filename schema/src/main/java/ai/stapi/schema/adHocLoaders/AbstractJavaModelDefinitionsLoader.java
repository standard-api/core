package ai.stapi.schema.adHocLoaders;

import java.util.List;

public abstract class AbstractJavaModelDefinitionsLoader<T>
    implements SpecificAdHocModelDefinitionsLoader {


  protected final String scope;
  protected final String tag;
  protected final String serializationType;

  protected AbstractJavaModelDefinitionsLoader(
      String scope,
      String tag,
      String serializationType
  ) {
    this.scope = scope;
    this.tag = tag;
    this.serializationType = serializationType;
  }

  protected abstract List<T> load();

  @Override
  public <R> List<R> load(String serializationType, Class<R> returnClass) {
    if (!this.getSerializationType().equals(serializationType)) {
      return List.of();
    }
    var loaded = this.load();
    return loaded.stream()
        .filter(loadedItem -> loadedItem.getClass().equals(returnClass))
        .map(returnClass::cast)
        .toList();
  }


  @Override
  public String getScope() {
    return scope;
  }

  @Override
  public String getTag() {
    return tag;
  }

  public String getSerializationType() {
    return serializationType;
  }
}
