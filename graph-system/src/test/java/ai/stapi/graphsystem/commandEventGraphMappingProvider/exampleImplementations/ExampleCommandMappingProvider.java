package ai.stapi.graphsystem.commandEventGraphMappingProvider.exampleImplementations;

import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.specific.SpecificCommandEventGraphMappingProvider;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ExampleCommandMappingProvider implements SpecificCommandEventGraphMappingProvider {

  @Override
  public Map<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> provideGraphMapping(
      AbstractCommand<? extends UniqueIdentifier> command) {
    var mappingBuilder = new ObjectGraphMappingBuilder();
    mappingBuilder.addField("targetIdentifier")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("example_node"))
        .addField("id")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    mappingBuilder.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    mappingBuilder.addField("conceptDefinition")
        .addInterfaceAsObjectFieldMapping()
        .setUuid(UUID.randomUUID());
    return Map.of(IrrelevantExampleEvent.class, mappingBuilder.build());
  }

  @Override
  public boolean supports(String serializationType) {
    return serializationType.equals(ExampleCommand.SERIALIZATION_TYPE);
  }
}
