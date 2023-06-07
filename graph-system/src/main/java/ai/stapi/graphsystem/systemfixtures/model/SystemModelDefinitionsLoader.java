package ai.stapi.graphsystem.systemfixtures.model;

import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.adHocLoaders.AbstractFileModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.stereotype.Service;

@Service
public class SystemModelDefinitionsLoader extends AbstractFileModelDefinitionsLoader {

  public static final String SCOPE = "system";
  public static final String TAG = ScopeOptions.DOMAIN_TAG;

  public SystemModelDefinitionsLoader(
      FileLoader fileLoader
  ) {
    super(fileLoader, SCOPE, TAG);
  }
}
