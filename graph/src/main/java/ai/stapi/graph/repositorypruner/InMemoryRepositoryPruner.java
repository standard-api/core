package ai.stapi.graph.repositorypruner;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;

public class InMemoryRepositoryPruner implements RepositoryPruner {
    
    private final InMemoryGraphRepository inMemoryGraphRepository;

    public InMemoryRepositoryPruner(InMemoryGraphRepository inMemoryGraphRepository) {
        this.inMemoryGraphRepository = inMemoryGraphRepository;
    }

    @Override
    public void prune() {
        this.inMemoryGraphRepository.prune();
    }
}
