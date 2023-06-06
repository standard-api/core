package ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.model;

import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.adHocLoaders.AbstractFileModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.stereotype.Service;

@Service
public class DynamicOgmProviderTestLoader extends AbstractFileModelDefinitionsLoader {

  public static final String SCOPE = "DynamicOgmProviderTest";

  protected DynamicOgmProviderTestLoader(
      FileLoader fileLoader
  ) {
    super(fileLoader, SCOPE, ScopeOptions.TEST_TAG);
  }
}
