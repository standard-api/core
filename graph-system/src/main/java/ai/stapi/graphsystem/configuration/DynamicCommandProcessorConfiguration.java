package ai.stapi.graphsystem.configuration;

import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregategraphstatemodifier.*;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.GenericCommandEventGraphMappingProvider;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.specific.SpecificCommandEventGraphMappingProvider;
import ai.stapi.graphsystem.dynamiccommandprocessor.BasicDynamicCommandProcessor;
import ai.stapi.graphsystem.dynamiccommandprocessor.DynamicCommandProcessor;
import ai.stapi.graphsystem.dynamiccommandprocessor.GenericDynamicCommandProcessor;
import ai.stapi.graphsystem.dynamiccommandprocessor.SpecificDynamicCommandProcessor;
import ai.stapi.graphsystem.genericGraphEventFactory.GenericGraphEventFactory;
import ai.stapi.graphsystem.genericGraphEventFactory.specific.SpecificGraphEventFactory;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DynamicCommandProcessorConfiguration {
  
  @Bean
  @ConditionalOnMissingBean(DynamicCommandProcessor.class)
  public GenericDynamicCommandProcessor genericDynamicCommandProcessor(
      List<SpecificDynamicCommandProcessor> specificDynamicCommandProcessors
  ) {
    return new GenericDynamicCommandProcessor(specificDynamicCommandProcessors);
  }

  @Bean
  @ConditionalOnBean(GenericDynamicCommandProcessor.class)
  public BasicDynamicCommandProcessor basicDynamicCommandProcessor(
      GenericObjectGraphMapper objectGraphMapper,
      GenericGraphEventFactory genericEventFactory,
      GenericCommandEventGraphMappingProvider commandEventGraphMappingProvider
  ) {
    return new BasicDynamicCommandProcessor(
        objectGraphMapper,
        genericEventFactory,
        commandEventGraphMappingProvider
    );
  }

  @Bean
  @ConditionalOnBean(BasicDynamicCommandProcessor.class)
  public GenericGraphEventFactory genericGraphEventFactory(
      List<SpecificGraphEventFactory> specificGraphEventFactories
  ) {
    return new GenericGraphEventFactory(specificGraphEventFactories);
  }
  
  @Bean
  @ConditionalOnBean(BasicDynamicCommandProcessor.class)
  public GenericCommandEventGraphMappingProvider genericCommandEventGraphMappingProvider(
      List<SpecificCommandEventGraphMappingProvider> specificCommandEventGraphMappingProviders
  ) {
    return new GenericCommandEventGraphMappingProvider(specificCommandEventGraphMappingProviders);
  }
  
  @Bean
  @ConditionalOnBean(GenericDynamicCommandProcessor.class)
  public DynamicAggregateCommandProcessor dynamicAggregateCommandProcessor(
      AggregateDefinitionProvider aggregateDefinitionProvider,
      GenericAggregateGraphStateModificator genericAggregateGraphStateModificator
  ) {
    return new DynamicAggregateCommandProcessor(
        aggregateDefinitionProvider,
        genericAggregateGraphStateModificator
    );
  }
  
  @Bean
  @ConditionalOnBean(DynamicAggregateCommandProcessor.class)
  public GenericAggregateGraphStateModificator genericAggregateGraphStateModificator(
      List<AggregateGraphStateModificator> aggregateGraphStateModificators,
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper
  ) {
    return new GenericAggregateGraphStateModificator(
        aggregateGraphStateModificators,
        operationDefinitionStructureTypeMapper
    );
  }
  
  @Bean
  public EventFactoryModificationTraverser eventFactoryModificationTraverser() {
    return new EventFactoryModificationTraverser();
  }
  
  @Bean
  public EventModificatorOgmProvider eventModificatorOgmProvider(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider
  ) {
    return new EventModificatorOgmProvider(structureSchemaFinder, dynamicOgmProvider);
  }
  
  @Bean
  @ConditionalOnBean(GenericAggregateGraphStateModificator.class)
  public AddAggregateGraphStateModificator addAggregateGraphStateModificator(
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser,
      EventModificatorOgmProvider eventModificatorOgmProvider
  ) {
    return new AddAggregateGraphStateModificator(
        objectGraphMapper,
        eventFactoryModificationTraverser,
        eventModificatorOgmProvider
    );
  }

  @Bean
  @ConditionalOnBean(GenericAggregateGraphStateModificator.class)
  public UpsertAggregateGraphStateModificator upsertAggregateGraphStateModificator(
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser,
      EventModificatorOgmProvider eventModificatorOgmProvider
  ) {
    return new UpsertAggregateGraphStateModificator(
        objectGraphMapper,
        eventFactoryModificationTraverser,
        eventModificatorOgmProvider
    );
  }
}
