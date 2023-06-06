package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IntegerAttributeValueDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.springframework.stereotype.Service;

@Service
public class IntegerAttributeGraphMappingProvider extends AbstractGraphDescriptionGraphMappingProvider {

  public static final String GRAPH_DESCRIPTION_NODE_TYPE = "graph_description_attribute_integer";

  @Override
  protected String getGraphDescriptionNodeType() {
    return GRAPH_DESCRIPTION_NODE_TYPE;
  }

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {
    
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(IntegerAttributeValueDescription.SERIALIZATION_TYPE);
  }
}
