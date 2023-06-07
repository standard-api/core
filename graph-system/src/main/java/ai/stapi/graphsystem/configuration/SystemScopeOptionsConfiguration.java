package ai.stapi.graphsystem.configuration;

import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemScopeOptionsConfiguration {

  @Bean
  public ScopeOptions systemScopeOptions() {
    return new ScopeOptions(
        SystemModelDefinitionsLoader.SCOPE,
        SystemModelDefinitionsLoader.TAG
    );
  }
}
