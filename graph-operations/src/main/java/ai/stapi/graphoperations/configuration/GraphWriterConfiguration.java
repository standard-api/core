package ai.stapi.graphoperations.configuration;

import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graphoperations.graphWriter.GenericGraphWriter;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.AttributeGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.AttributeValueSpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.ConstantSpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.EdgeGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.NodeGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.RemovalEdgeGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.RemovalNodeGraphWriter;
import ai.stapi.graphoperations.graphWriter.specific.UuidGraphWriter;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class GraphWriterConfiguration {
  
  @Bean
  public GenericGraphWriter genericGraphWriter(
      List<SpecificGraphWriter> specificGraphWriters,
      GenericAttributeFactory genericAttributeFactory
  ) {
    return new GenericGraphWriter(specificGraphWriters, genericAttributeFactory);
  }
  
  @Bean
  public AttributeGraphWriter attributeGraphWriter() {
    return new AttributeGraphWriter();
  }
  
  @Bean
  public AttributeValueSpecificGraphWriter attributeValueSpecificGraphWriter() {
    return new AttributeValueSpecificGraphWriter();
  }
  
  @Bean
  public ConstantSpecificGraphWriter constantSpecificGraphWriter() {
    return new ConstantSpecificGraphWriter();
  }
  
  @Bean
  public EdgeGraphWriter edgeGraphWriter() {
    return new EdgeGraphWriter();
  }
  
  @Bean
  public NodeGraphWriter nodeGraphWriter() {
    return new NodeGraphWriter();
  }
  
  @Bean
  public RemovalEdgeGraphWriter removalEdgeGraphWriter() {
    return new RemovalEdgeGraphWriter();
  }
  
  @Bean
  public RemovalNodeGraphWriter removalNodeGraphWriter() {
    return new RemovalNodeGraphWriter();
  }
  
  @Bean
  public UuidGraphWriter uuidGraphWriter() {
    return new UuidGraphWriter();
  }
}
