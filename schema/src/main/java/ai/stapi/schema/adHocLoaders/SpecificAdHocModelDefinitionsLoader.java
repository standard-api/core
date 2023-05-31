package ai.stapi.schema.adHocLoaders;

import java.util.HashMap;
import java.util.List;

public interface SpecificAdHocModelDefinitionsLoader {

  // Scope stands for the slice of application (module, package, class)  that is responsible for the structure definition.
  String getScope();

  // Tag stands for the layer of application (domain, test) that is responsible for the structure definition.
  String getTag();

  default List<HashMap> load(String serializationType) {
    return this.load(serializationType, HashMap.class);
  }

  <T> List<T> load(String serializationType, Class<T> returnClass);
}
