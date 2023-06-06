package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import org.springframework.stereotype.Service;

@Service
public class OutgoingEdgeDescriptionGraphMappingProvider
    extends AbstractEdgeDescriptionGraphMappingProvider {

  @Override
  protected String getGraphDescriptionNodeType() {
    return "graph_description_outgoing_edge";
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(OutgoingEdgeDescription.SERIALIZATION_TYPE);
  }
}
