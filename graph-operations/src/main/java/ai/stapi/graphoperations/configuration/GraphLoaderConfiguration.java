package ai.stapi.graphoperations.configuration;

import ai.stapi.configuration.SerializationConfiguration;
import ai.stapi.graph.configuration.GraphRepositoryConfiguration;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.graphLoaderOGMFactory.GraphLoaderOgmFactory;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryAscendingSortResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryDescendingSortResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGenericSearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoader;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryGraphLoaderProvider;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemoryOffsetPaginationResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.InMemorySearchResolvingContext;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.GenericInMemoryFilterOptionResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryAllMatchFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryAndFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryAnyMatchFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryContainsFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryEndsWithFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryEqualsFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryGreaterThanFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryGreaterThanOrEqualsFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryIsNullFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryLowerThanFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryLowerThanOrEqualFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryNoneMatchFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryNotEqualsFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryNotFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryNotNullFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryOrFilterResolver;
import ai.stapi.graphoperations.graphLoader.inmemory.filter.InMemoryStartsWithFilterResolver;
import ai.stapi.graphoperations.graphLoader.search.SearchOptionResolver;
import ai.stapi.graphoperations.graphLoader.search.filterOption.factory.FilterOptionFactory;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration
@AutoConfigureAfter({SerializationConfiguration.class, GraphRepositoryConfiguration.class})
public class GraphLoaderConfiguration {

  @Bean
  public GraphLoaderOgmFactory graphLoaderOgmFactory() {
    return new GraphLoaderOgmFactory();
  }
  
  @Bean
  public FilterOptionFactory filterOptionFactory() {
    return new FilterOptionFactory();
  }
  
  @Bean
  public InMemoryGraphLoaderProvider inMemoryGraphLoaderProvider(
      InMemoryGenericSearchOptionResolver searchOptionResolver,
      StructureSchemaFinder structureSchemaFinder,
      ObjectMapper objectMapper
  ) {
    return new InMemoryGraphLoaderProvider(searchOptionResolver, structureSchemaFinder, objectMapper);
  }
  
  @Bean
  @ConditionalOnBean(InMemoryGraphRepository.class)
  public InMemoryGraphLoader inMemoryGraphLoader(
      InMemoryGraphRepository inMemoryGraphRepository,
      InMemoryGenericSearchOptionResolver inMemoryGenericSearchOptionResolver,
      StructureSchemaFinder structureSchemaFinder,
      ObjectMapper objectMapper
  ) {
    return new InMemoryGraphLoader(
        inMemoryGraphRepository,
        inMemoryGenericSearchOptionResolver,
        structureSchemaFinder,
        objectMapper
    );
  }
  
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(InMemoryGraphLoader.class)
  public GraphLoader inMemoryGraphLoaderImplementation(
    InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return inMemoryGraphLoader;
  }
  
  @Bean
  public InMemoryGenericSearchOptionResolver inMemoryGenericSearchOptionResolver(
      List<SearchOptionResolver<InMemorySearchResolvingContext>> searchOptionResolvers
  ) {
    return new InMemoryGenericSearchOptionResolver(searchOptionResolvers);
  }
  
  @Bean
  public InMemoryAscendingSortResolver inMemoryAscendingSortResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryAscendingSortResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryDescendingSortResolver inMemoryDescendingSortResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryDescendingSortResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }
  
  @Bean
  public InMemoryOffsetPaginationResolver inMemoryOffsetPaginationResolver(
      StructureSchemaFinder structureSchemaFinder
  ) {
    return new InMemoryOffsetPaginationResolver(structureSchemaFinder);
  }
  
  @Bean
  public GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver(
      @Lazy List<InMemoryFilterResolver> inMemoryFilterResolvers
  ) {
    return new GenericInMemoryFilterOptionResolver(inMemoryFilterResolvers);
  }
  
  @Bean
  public InMemoryAndFilterResolver inMemoryAndFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver
  ) {
    return new InMemoryAndFilterResolver(structureSchemaFinder, genericInMemoryFilterOptionResolver);
  }

  @Bean
  public InMemoryOrFilterResolver inMemoryOrFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver
  ) {
    return new InMemoryOrFilterResolver(structureSchemaFinder, genericInMemoryFilterOptionResolver);
  }

  @Bean
  public InMemoryNotFilterResolver inMemoryNotFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver
  ) {
    return new InMemoryNotFilterResolver(structureSchemaFinder, genericInMemoryFilterOptionResolver);
  }
  
  @Bean
  public InMemoryAllMatchFilterResolver inMemoryAllMatchFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryAllMatchFilterResolver(
        structureSchemaFinder, 
        genericInMemoryFilterOptionResolver, 
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryAnyMatchFilterResolver inMemoryAnyMatchFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryAnyMatchFilterResolver(
        structureSchemaFinder,
        genericInMemoryFilterOptionResolver,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryNoneMatchFilterResolver inMemoryNoneMatchFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      GenericInMemoryFilterOptionResolver genericInMemoryFilterOptionResolver,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryNoneMatchFilterResolver(
        structureSchemaFinder,
        genericInMemoryFilterOptionResolver,
        inMemoryGraphLoader
    );
  }
  
  @Bean
  public InMemoryContainsFilterResolver inMemoryContainsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryContainsFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryEndsWithFilterResolver inMemoryEndsWithFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryEndsWithFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryEqualsFilterResolver inMemoryEqualsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryEqualsFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryGreaterThanFilterResolver inMemoryGreaterThanFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryGreaterThanFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryGreaterThanOrEqualsFilterResolver inMemoryGreaterThanOrEqualsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryGreaterThanOrEqualsFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryIsNullFilterResolver inMemoryIsNullFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryIsNullFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryLowerThanFilterResolver inMemoryLowerThanFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryLowerThanFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryLowerThanOrEqualFilterResolver inMemoryLowerThanOrEqualFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryLowerThanOrEqualFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryNotNullFilterResolver inMemoryNotNullFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryNotNullFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryNotEqualsFilterResolver inMemoryNotEqualsFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryNotEqualsFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }

  @Bean
  public InMemoryStartsWithFilterResolver inMemoryStartsWithFilterResolver(
      StructureSchemaFinder structureSchemaFinder,
      @Lazy InMemoryGraphLoader inMemoryGraphLoader
  ) {
    return new InMemoryStartsWithFilterResolver(
        structureSchemaFinder,
        inMemoryGraphLoader
    );
  }
}
