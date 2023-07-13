package ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.specific.SpecificCommandEventGraphMappingProvider;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.identity.UniqueIdentifier;
import java.util.HashMap;
import java.util.Map;

public class ImportStructureDefinitionOgmProvider implements SpecificCommandEventGraphMappingProvider {

  private final GenericGraphMappingProvider genericGraphMappingProvider;

  public ImportStructureDefinitionOgmProvider(
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    this.genericGraphMappingProvider = genericGraphMappingProvider;
  }

  @Override
  public Map<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> provideGraphMapping(
      AbstractCommand<? extends UniqueIdentifier> command) {
    var map =
        new HashMap<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping>();
    this.addCreatedConceptEventToMap(map);
    return map;
  }

  public void addCreatedConceptEventToMap(
      Map<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> map) {
    var definition = new ObjectGraphMappingBuilder();
    definition
        .addField("targetIdentifier")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder().addNodeDescription("StructureDefinition")
        ).addField("id")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());

    var ogm = this.genericGraphMappingProvider.provideGraphMapping(
        "StructureDefinition"
    );
    var builder = new GenericOGMBuilder().copyGraphMappingAsBuilder(ogm);
    builder.setNewGraphDescription(new NullGraphDescription());
    definition
        .addField("structureDefinitionSource")
        .setChild(builder);

    map.put(StructureDefinitionImported.class, definition.build());
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ImportStructureDefinition.SERIALIZATION_TYPE);
  }
}
