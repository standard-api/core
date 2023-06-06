package ai.stapi.graphoperations.fixtures.testsystem;

import ai.stapi.schema.adHocLoaders.AbstractFileModelDefinitionsLoader;
import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.stereotype.Service;

@Service
public class TestSystemModelDefinitionsLoader extends AbstractFileModelDefinitionsLoader {

  public static final String SCOPE = "test-system";
  public static final String TAG = ScopeOptions.TEST_TAG;

  public TestSystemModelDefinitionsLoader(
      FileLoader fileLoader
  ) {
    super(fileLoader, SCOPE, TAG);
  }
}
