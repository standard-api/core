package ai.stapi.graphsystem.commandEventGraphMappingProvider;

import ai.stapi.graphsystem.commandEventGraphMappingProvider.exception.GraphMappingProviderException;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.specific.SpecificCommandEventGraphMappingProvider;
import ai.stapi.graphsystem.messaging.event.AggregateGraphUpdatedEvent;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GenericCommandEventGraphMappingProvider {

  private final List<SpecificCommandEventGraphMappingProvider> mappingProviders;

  public GenericCommandEventGraphMappingProvider(
      List<SpecificCommandEventGraphMappingProvider> mappingProviders
  ) {
    this.mappingProviders = mappingProviders;
  }

  public Map<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> provideGraphMappings(
      AbstractCommand<? extends UniqueIdentifier> command
  ) {
    return this.resolveAllProviders(command);
  }

  public boolean containsFor(AbstractCommand<? extends UniqueIdentifier> command) {
    return this.mappingProviders.stream().anyMatch(
        specificObjectGraphMapper -> specificObjectGraphMapper.supports(
            command.getSerializationType()
        )
    );
  }

  private HashMap<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping> resolveAllProviders(
      AbstractCommand<? extends UniqueIdentifier> command
  ) {
    var collectedProviderResults =
        new HashMap<Class<? extends AggregateGraphUpdatedEvent<? extends UniqueIdentifier>>, ObjectGraphMapping>();
    var providers = this.getSupportingProviders(command.getSerializationType());
    providers.stream()
        .map(provider -> provider.provideGraphMapping(command))
        .forEach(collectedProviderResults::putAll);
    return collectedProviderResults;
  }

  private List<SpecificCommandEventGraphMappingProvider> getSupportingProviders(
      String deserializationType
  ) {
    var listOfSupportingProviders = this.mappingProviders.stream()
        .filter(
            specificObjectGraphMapper -> specificObjectGraphMapper.supports(deserializationType))
        .toList();
    if (listOfSupportingProviders.isEmpty()) {
      throw GraphMappingProviderException.becauseThereIsNoSupportingSpecificGraphMappingProvider(
          deserializationType);
    }
    return listOfSupportingProviders;
  }
}
