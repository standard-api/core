package ai.stapi.schema.adHocLoaders;

import ai.stapi.schema.scopeProvider.ScopeOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericAdHocModelDefinitionsLoader {

  private List<SpecificAdHocModelDefinitionsLoader> specificAdHocModelDefinitionsLoaders;

  public GenericAdHocModelDefinitionsLoader(
      List<SpecificAdHocModelDefinitionsLoader> specificAdHocModelDefinitionsLoaders
  ) {
    this.specificAdHocModelDefinitionsLoaders = specificAdHocModelDefinitionsLoaders;
  }

  public List<HashMap> load(
      ScopeOptions scopeOptions,
      String serializationType
  ) {
    return this.load(
        scopeOptions,
        serializationType,
        HashMap.class
    );
  }

  public <T> List<T> load(
      ScopeOptions scopeOptions,
      String serializationType,
      Class<T> returnClass
  ) {
    var list = new ArrayList<T>();
    specificAdHocModelDefinitionsLoaders.stream().filter(scopedStructureDefinitionLoader ->
        scopeOptions.getTags().contains(scopedStructureDefinitionLoader.getTag())
            && scopeOptions.getScopes().contains(scopedStructureDefinitionLoader.getScope())
    ).forEach(
        scopedStructureDefinitionLoader -> list.addAll(scopedStructureDefinitionLoader.load(
            serializationType,
            returnClass
        ))
    );
    return list;
  }
}
