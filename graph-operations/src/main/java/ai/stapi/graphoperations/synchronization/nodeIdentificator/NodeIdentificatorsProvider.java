package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import java.util.List;

public interface NodeIdentificatorsProvider {
    
    int MAX_PRIORITY = 1000;
    int MIN_PRIORITY = -1000;
    int DEFAULT_PRIORITY = 0;
    List<NodeIdentificator> provide(String nodeType);
    
    boolean supports(String nodeType);
    
    default int priority() {
        return DEFAULT_PRIORITY;
    }
}
