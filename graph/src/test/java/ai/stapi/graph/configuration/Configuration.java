package ai.stapi.graph.configuration;

import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration {
    
    @Bean
    public InMemoryGraphRepository inMemoryGraphRepository() {
        return new InMemoryGraphRepository();
    }
    
    @Bean
    public NodeRepository nodeRepository(
        InMemoryGraphRepository inMemoryGraphRepository
    ) {
        return inMemoryGraphRepository;
    }

    @Bean
    public EdgeRepository edgeRepository(
        InMemoryGraphRepository inMemoryGraphRepository
    ) {
        return inMemoryGraphRepository;
    }
}
