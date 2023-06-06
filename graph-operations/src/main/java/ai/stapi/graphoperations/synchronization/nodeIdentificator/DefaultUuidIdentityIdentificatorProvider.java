package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultUuidIdentityIdentificatorProvider implements NodeIdentificatorsProvider {
    
    @Override
    public List<NodeIdentificator> provide(String nodeType) {
        return List.of(
            new NodeIdentificator(new UuidIdentityDescription())
        );
    }

    @Override
    public boolean supports(String nodeType) {
        return true;
    }

    @Override
    public int priority() {
        return NodeIdentificatorsProvider.MAX_PRIORITY;
    }
}
