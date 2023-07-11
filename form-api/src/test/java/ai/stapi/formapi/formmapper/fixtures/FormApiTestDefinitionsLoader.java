package ai.stapi.formapi.formmapper.fixtures;

import ai.stapi.schema.adHocLoaders.AbstractFileModelDefinitionsLoader;
import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.stereotype.Service;

@Service
public class FormApiTestDefinitionsLoader extends AbstractFileModelDefinitionsLoader {

  public static final String SCOPE = "FormApi";
  public static final String TAG = ScopeOptions.TEST_TAG;

  public FormApiTestDefinitionsLoader(
      FileLoader fileLoader
  ) {
    super(fileLoader, SCOPE, TAG);
  }
}
