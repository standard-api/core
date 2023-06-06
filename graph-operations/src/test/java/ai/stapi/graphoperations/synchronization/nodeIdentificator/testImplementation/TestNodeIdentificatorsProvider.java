package ai.stapi.graphoperations.synchronization.nodeIdentificator.testImplementation;

import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificator;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificatorsProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TestNodeIdentificatorsProvider implements NodeIdentificatorsProvider {
    
    private final Map<String, List<NodeIdentificator>> identificators;

    public TestNodeIdentificatorsProvider() {
        this.identificators = new HashMap<>();
    }
    
    public void add(String nodeType, NodeIdentificator nodeIdentificator) {
        this.identificators.computeIfAbsent(nodeType, key -> new ArrayList<>()).add(nodeIdentificator);   
    }
    
    public void addAll(String nodeType, NodeIdentificator... nodeIdentificators) {
        this.identificators.computeIfAbsent(
            nodeType, 
            key -> new ArrayList<>()
        ).addAll(Arrays.stream(nodeIdentificators).toList());
    }
    
    public void clear() {
        this.identificators.clear();
    }

    @Override
    public List<NodeIdentificator> provide(String nodeType) {
        return this.identificators.getOrDefault(nodeType, List.of());
    }

    @Override
    public boolean supports(String nodeType) {
        return this.identificators.containsKey(nodeType);
    }
}
