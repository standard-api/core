package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

public class GenericNodeIdentificatorsProvider {
    private final List<NodeIdentificatorsProvider> providers;

    public GenericNodeIdentificatorsProvider(List<NodeIdentificatorsProvider> providers) {
        this.providers = providers;
    }
    
    public List<NodeIdentificator> provide(String nodeType) {
        return this.providers.stream()
            .filter(provider -> provider.supports(nodeType))
            .sorted(Comparator.comparingInt(NodeIdentificatorsProvider::priority).reversed())
            .flatMap(provider -> provider.provide(nodeType).stream())
            .toList();
    }
}
