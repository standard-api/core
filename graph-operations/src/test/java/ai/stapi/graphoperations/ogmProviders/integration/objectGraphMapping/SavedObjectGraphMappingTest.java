package ai.stapi.graphoperations.ogmProviders.integration.objectGraphMapping;

import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.InterfaceGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ListGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.MapGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.serialization.SerializableObject;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SavedObjectGraphMappingTest extends IntegrationTestCase {

  @Autowired
  private GenericObjectGraphMapper graphMapper;

  @Autowired
  private GenericGraphMappingProvider mappingProvider;

  @Test
  void itCanSaveLeafOGM() {
    var leaf = new LeafObjectGraphMapping(
        new GraphDescriptionBuilder()
            .addNodeDescription("house")
            .addLeafAttribute("address")
            .addStringAttributeValue()
            .build()
    );
    var mapping = this.whenObjectGraphMappingProvided(leaf);
    this.thenObjectGraphMappingApproved(mapping, leaf);
  }

  @Test
  void itCanSaveLeafOGMWithoutGraphDescription() {
    var leaf = new LeafObjectGraphMapping(new NullGraphDescription());
    var mapping = this.whenObjectGraphMappingProvided(leaf);
    this.thenObjectGraphMappingApproved(mapping, leaf);
  }

  @Test
  void itCanSaveObjectWithGraphDescription() {
    var objectDefinitionBuilder = new ObjectGraphMappingBuilder();
    objectDefinitionBuilder.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("house"));
    objectDefinitionBuilder.addField("address")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("address")
                .addStringAttributeValue()
        );
    var objectDefinition = objectDefinitionBuilder.build();
    var mapping = this.whenObjectGraphMappingProvided(objectDefinition);
    this.thenObjectGraphMappingApproved(mapping, objectDefinition);
  }

  @Test
  void itCanSaveListWithGraphDescription() {
    var definition = new ListGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder()
            .addNodeDescription("person")
            .addOutgoingEdge("has_mom")
    );
    definition.addLeafChildDefinition()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("persons_mom"));
    var mapping = this.whenObjectGraphMappingProvided(definition.build());
    this.thenObjectGraphMappingApproved(mapping, definition.build());
  }

  @Test
  void itCanSaveMapWithGraphDescription() {
    var definition = new MapGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("person")
    );
    definition.addLeafKeyMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    definition.addLeafValueMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("age")
                .addIntegerAttributeValue()
        );
    var mapping = this.whenObjectGraphMappingProvided(definition.build());
    this.thenObjectGraphMappingApproved(mapping, definition.build());
  }

  @Test
  void itCanSaveInterfaceWithGraphDescription() {
    var definition = new InterfaceGraphMappingBuilder();
    definition.setUuid(Declaration.INTERFACE_UUID);
    var mapping = this.whenObjectGraphMappingProvided(definition.build());
    this.thenObjectGraphMappingApproved(mapping, definition.build());
  }

  private ObjectGraphMapping whenObjectGraphMappingProvided(SerializableObject serializableObject) {
    return this.mappingProvider.provideGraphMapping(serializableObject);
  }

  private void thenObjectGraphMappingApproved(
      ObjectGraphMapping graphMapping,
      ObjectGraphMapping testedOgm
  ) {
    var result = this.graphMapper.mapToGraph(graphMapping, testedOgm);
    this.thenGraphApproved(result.getGraph());
  }
}
