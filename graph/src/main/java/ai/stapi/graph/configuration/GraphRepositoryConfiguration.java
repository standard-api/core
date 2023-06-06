package ai.stapi.graph.configuration;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.repositorypruner.InMemoryRepositoryPruner;
import ai.stapi.graph.repositorypruner.RepositoryPruner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class GraphRepositoryConfiguration {
  
  @Bean
  @ConditionalOnMissingBean({NodeRepository.class, EdgeRepository.class})
  public InMemoryGraphRepository inMemoryGraphRepository() {
    return new InMemoryGraphRepository();
  }
  
  @Bean
  @ConditionalOnMissingBean
  public NodeRepository inMemoryNodeRepository(
      InMemoryGraphRepository inMemoryGraphRepository
  ) {
    return inMemoryGraphRepository;
  }

  @Bean
  @ConditionalOnMissingBean
  public EdgeRepository inMemoryEdgeRepository(
      InMemoryGraphRepository inMemoryGraphRepository
  ) {
    return inMemoryGraphRepository;
  }
  
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(InMemoryGraphRepository.class)
  public RepositoryPruner inMemoryRepositoryPruner(
      InMemoryGraphRepository inMemoryGraphRepository
  ) {
    return new InMemoryRepositoryPruner(inMemoryGraphRepository);
  }
}
