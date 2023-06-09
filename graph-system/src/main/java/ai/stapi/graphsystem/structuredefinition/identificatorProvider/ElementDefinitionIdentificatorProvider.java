package ai.stapi.graphsystem.structuredefinition.identificatorProvider;

import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificator;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificatorsProvider;
import java.util.List;

public class ElementDefinitionIdentificatorProvider implements NodeIdentificatorsProvider {

  @Override
  public List<NodeIdentificator> provide(String nodeType) {
    return List.of(
        new NodeIdentificator("path")
    );
  }

  @Override
  public boolean supports(String nodeType) {
    return nodeType.equals("ElementDefinition");
  }
}
