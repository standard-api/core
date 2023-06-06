package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescription;
import org.springframework.stereotype.Service;

@Service
public class RemovalEdgeDescriptionGraphMappingProviderImpl
    extends AbstractEdgeDescriptionGraphMappingProvider {

  @Override
  protected String getGraphDescriptionNodeType() {
    return "graph_description_removal_edge";
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(RemovalEdgeDescription.SERIALIZATION_TYPE);
  }
}
