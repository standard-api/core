package ai.stapi.schema.configuration;

import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.scopeProvider.ScopeProvider;
import ai.stapi.schema.scopeProvider.SpringScopeProvider;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ScopeProviderConfiguration {
  
  @Bean
  @ConditionalOnMissingBean
  public ScopeProvider springScopeProvider(List<ScopeOptions> scopeOptions) {
    return new SpringScopeProvider(scopeOptions);
  }
  
  @Bean
  public ScopeCacher scopeCacher(ScopeProvider scopeProvider) {
    return new ScopeCacher(scopeProvider);
  }
}
