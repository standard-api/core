package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import org.springframework.stereotype.Service;

@Service
public class LeafAttributeGraphMappingProvider extends AbstractAttributeGraphMappingProvider {

  public static final String GRAPH_DESCRIPTION_NODE_TYPE = "graph_description_leaf_attribute";

  @Override
  protected String getGraphDescriptionNodeType() {
    return GRAPH_DESCRIPTION_NODE_TYPE;
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(LeafAttributeDescription.SERIALIZATION_TYPE);
  }
}
