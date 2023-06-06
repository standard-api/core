package ai.stapi.graphoperations.fixtures.model;

import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.adHocLoaders.AbstractFileModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.stereotype.Service;

@Service
public class GraphLoaderTestDefinitionsLoader extends AbstractFileModelDefinitionsLoader {

  public static final String SCOPE = "GraphLoaderTest";
  public static final String TAG = ScopeOptions.TEST_TAG;

  public GraphLoaderTestDefinitionsLoader(
      FileLoader fileLoader
  ) {
    super(fileLoader, SCOPE, TAG);
  }
}
