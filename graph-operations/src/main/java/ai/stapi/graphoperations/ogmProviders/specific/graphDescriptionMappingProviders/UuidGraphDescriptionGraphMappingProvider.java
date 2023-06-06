package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import org.springframework.stereotype.Service;

@Service
public class UuidGraphDescriptionGraphMappingProvider
    extends AbstractGraphDescriptionGraphMappingProvider {

  @Override
  protected String getGraphDescriptionNodeType() {
    return "graph_description_uuid_identity";
  }

  @Override
  protected void setParametersFields(ObjectGraphMappingBuilder parameters) {

  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(UuidIdentityDescription.SERIALIZATION_TYPE);
  }
}
