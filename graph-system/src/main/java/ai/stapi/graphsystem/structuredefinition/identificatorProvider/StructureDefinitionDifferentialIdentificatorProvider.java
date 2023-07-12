package ai.stapi.graphsystem.structuredefinition.identificatorProvider;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificator;
import ai.stapi.graphoperations.synchronization.nodeIdentificator.NodeIdentificatorsProvider;
import java.util.List;

public class StructureDefinitionDifferentialIdentificatorProvider implements NodeIdentificatorsProvider {

  @Override
  public List<NodeIdentificator> provide(String nodeType) {
    return List.of(
        new NodeIdentificator(
            new IngoingEdgeDescription(
                new EdgeDescriptionParameters("differential"),
                new NodeDescription(
                    "StructureDefinition",
                    new UuidIdentityDescription()
                )
            )
        )
    );
  }

  @Override
  public boolean supports(String nodeType) {
    return nodeType.equals("StructureDefinitionDifferential");
  }
}
