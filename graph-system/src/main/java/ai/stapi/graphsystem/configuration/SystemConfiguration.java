package ai.stapi.graphsystem.configuration;

import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelFixtureGenerator;
import ai.stapi.schema.adHocLoaders.FileLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SystemConfiguration {

  @Bean
  public ScopeOptions systemScopeOptions() {
    return new ScopeOptions(
        SystemModelDefinitionsLoader.SCOPE,
        SystemModelDefinitionsLoader.TAG
    );
  }
  
  @Bean
  public SystemModelDefinitionsLoader systemModelDefinitionsLoader(
      FileLoader fileLoader
  ) {
    return new SystemModelDefinitionsLoader(fileLoader);
  }
  
  @Bean
  public SystemModelFixtureGenerator systemModelFixtureGenerator(
      FileLoader fileLoader
  ) {
    return new SystemModelFixtureGenerator(fileLoader);
  }
}
