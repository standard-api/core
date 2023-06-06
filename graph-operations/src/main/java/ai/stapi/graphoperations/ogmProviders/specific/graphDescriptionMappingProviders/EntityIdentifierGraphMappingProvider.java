package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import org.springframework.stereotype.Service;

@Service
public class EntityIdentifierGraphMappingProvider implements SpecificGraphMappingProvider {

  protected String getGraphDescriptionNodeType() {
    return "object_declaration_entity_identifier";
  }

  @Override
  public ObjectGraphMapping provideGraphMapping(String serializationType
  ) {
    var definition = new ObjectGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(this.getGraphDescriptionNodeType()));
    return definition.build();
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(EntityIdentifier.SERIALIZATION_TYPE);
  }
}
