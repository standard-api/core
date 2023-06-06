package ai.stapi.graphoperations.ogmProviders.integration.graphDescription;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.serialization.SerializableObject;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SavedGraphDescriptionGraphTest extends IntegrationTestCase {

  @Autowired
  private GenericObjectGraphMapper graphMapper;

  @Autowired
  private GenericGraphMappingProvider mappingProvider;

  @Test
  public void itCanGetMappingForNodeDescription() {
    var description = new NodeDescription(new NodeDescriptionParameters("example_node"));
    var mapping = this.whenObjectGraphMappingProvided(description);
    this.thenObjectGraphMappingApproved(mapping, description);
  }

  @Test
  public void itCanGetMappingForNodeDescriptionWithAttributeChildGraphDescription() {
    var graphDescription = new GraphDescriptionBuilder()
        .addNodeDescription("example_node")
        .addLeafAttribute("example_attribute")
        .addStringAttributeValue()
        .build();
    var mapping = this.whenObjectGraphMappingProvided(graphDescription);
    this.thenObjectGraphMappingApproved(mapping, graphDescription);
  }

  @Test
  public void itCanGetMappingForNodeDescriptionWithMultipleChildGraphDescription() {
    var graphDescriptionBuilder = new GraphDescriptionBuilder()
        .addNodeDescription("house");
    graphDescriptionBuilder
        .addLeafAttribute("isOccupied")
        .addBooleanAttributeValue();
    
    graphDescriptionBuilder
        .addLeafAttribute("numberOfResidents")
        .addIntegerAttributeValue();
    var graphDescription = graphDescriptionBuilder.build();
    var mapping = this.whenObjectGraphMappingProvided(graphDescription);
    this.thenObjectGraphMappingApproved(mapping, graphDescription);
  }

  @Test
  public void itCanGetMappingForNodeDescriptionWithEdgeChildGraphDescriptions() {
    var graphDescriptionBuilder = new GraphDescriptionBuilder()
        .addNodeDescription("house")
        .addOutgoingEdge("contains_resident")
        .addNodeDescription("resident");
    var graphDescription = graphDescriptionBuilder.build();
    var mapping = this.whenObjectGraphMappingProvided(graphDescription);
    this.thenObjectGraphMappingApproved(mapping, graphDescription);
  }

  private ObjectGraphMapping whenObjectGraphMappingProvided(SerializableObject serializableObject) {
    return this.mappingProvider.provideGraphMapping(serializableObject);
  }

  private void thenObjectGraphMappingApproved(ObjectGraphMapping graphMapping,
      GraphDescription graphDescription) {
    var result = this.graphMapper.mapToGraph(graphMapping, graphDescription);
    this.thenGraphApproved(result.getGraph());
  }
}