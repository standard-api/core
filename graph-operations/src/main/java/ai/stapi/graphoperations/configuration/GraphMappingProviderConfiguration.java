package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.BooleanAttributeGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.EntityIdentifierGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.IncomingEdgeDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.IntegerAttributeGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.InterfaceGraphDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.LeafAttributeGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.NodeDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.OutgoingEdgeDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.RemovalEdgeDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.RemovalNodeDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.StringAttributeGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.UuidGraphDescriptionGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.InterfaceOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.LeafOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.ListOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.MapOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.ObjectFieldOgmProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.ObjectOgmProvider;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class GraphMappingProviderConfiguration {
  
  @Bean
  public GenericGraphMappingProvider genericGraphMappingProvider(
      List<SpecificGraphMappingProvider> specificGraphMappingProviders
  ) {
    return new GenericGraphMappingProvider(specificGraphMappingProviders);
  }
  
  @Bean
  public DynamicOgmProvider dynamicOgmProvider(
      StructureSchemaProvider structureSchemaProvider
  ) {
    return new DynamicOgmProvider(structureSchemaProvider);
  }
  
  @Bean
  public BooleanAttributeGraphMappingProvider booleanAttributeGraphMappingProvider() {
    return new BooleanAttributeGraphMappingProvider();
  }

  @Bean
  public EntityIdentifierGraphMappingProvider entityIdentifierGraphMappingProvider() {
    return new EntityIdentifierGraphMappingProvider();
  }

  @Bean
  public IncomingEdgeDescriptionGraphMappingProvider incomingEdgeDescriptionGraphMappingProvider() {
    return new IncomingEdgeDescriptionGraphMappingProvider();
  }

  @Bean
  public IntegerAttributeGraphMappingProvider integerAttributeGraphMappingProvider() {
    return new IntegerAttributeGraphMappingProvider();
  }

  @Bean
  public InterfaceGraphDescriptionGraphMappingProvider interfaceGraphDescriptionGraphMappingProvider() {
    return new InterfaceGraphDescriptionGraphMappingProvider();
  }

  @Bean
  public LeafAttributeGraphMappingProvider leafAttributeGraphMappingProvider() {
    return new LeafAttributeGraphMappingProvider();
  }

  @Bean
  public NodeDescriptionGraphMappingProvider nodeDescriptionGraphMappingProvider() {
    return new NodeDescriptionGraphMappingProvider();
  }

  @Bean
  public OutgoingEdgeDescriptionGraphMappingProvider outgoingEdgeDescriptionGraphMappingProvider() {
    return new OutgoingEdgeDescriptionGraphMappingProvider();
  }

  @Bean
  public RemovalEdgeDescriptionGraphMappingProvider removalEdgeDescriptionGraphMappingProvider() {
    return new RemovalEdgeDescriptionGraphMappingProvider();
  }

  @Bean
  public RemovalNodeDescriptionGraphMappingProvider removalNodeDescriptionGraphMappingProvider() {
    return new RemovalNodeDescriptionGraphMappingProvider();
  }

  @Bean
  public StringAttributeGraphMappingProvider stringAttributeGraphMappingProvider() {
    return new StringAttributeGraphMappingProvider();
  }

  @Bean
  public UuidGraphDescriptionGraphMappingProvider uuidGraphDescriptionGraphMappingProvider() {
    return new UuidGraphDescriptionGraphMappingProvider();
  }
  
  @Bean
  public InterfaceOgmProvider interfaceOgmProvider() {
    return new InterfaceOgmProvider();
  }

  @Bean
  public LeafOgmProvider leafOgmProvider() {
    return new LeafOgmProvider();
  }

  @Bean
  public ListOgmProvider listOgmProvider() {
    return new ListOgmProvider();
  }

  @Bean
  public MapOgmProvider mapOgmProvider() {
    return new MapOgmProvider();
  }

  @Bean
  public ObjectFieldOgmProvider objectFieldOgmProvider() {
    return new ObjectFieldOgmProvider();
  }

  @Bean
  public ObjectOgmProvider objectOgmProvider() {
    return new ObjectOgmProvider();
  }
}
