package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import org.springframework.stereotype.Service;

@Service
public class IncomingEdgeDescriptionGraphMappingProvider
    extends AbstractEdgeDescriptionGraphMappingProvider {

  @Override
  protected String getGraphDescriptionNodeType() {
    return "graph_description_ingoing_edge";
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(IngoingEdgeDescription.SERIALIZATION_TYPE);
  }
}
