package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;

class GenericOGMBuilderTest extends IntegrationTestCase {

  @Test
  public void itCanCopyObjectGraphMapping() {
    var originalObjectMapping = new ObjectGraphMappingBuilder();
    originalObjectMapping.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("entity"));
    originalObjectMapping.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    var genericBuilder = new GenericOGMBuilder();
    var builder = genericBuilder.copyGraphMappingAsBuilder(originalObjectMapping.build());
    this.thenObjectApproved(builder.build(), this.getOptions());
  }

  @Test
  public void itCanCopyObjectGraphMappingWithMap() {
    var originalObjectMapping = new ObjectGraphMappingBuilder();
    originalObjectMapping.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("entity"));
    originalObjectMapping.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    var mapMapping = originalObjectMapping.addField("children")
        .addMapAsObjectFieldMapping();
    mapMapping.addLeafKeyMapping();
    mapMapping.addLeafValueMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("child"));
    var genericBuilder = new GenericOGMBuilder();
    var builder = genericBuilder.copyGraphMappingAsBuilder(originalObjectMapping.build());
    this.thenObjectApproved(builder.build(), this.getOptions());
  }

  @Test
  void itCanCopyObjectGraphMappingWithList() {
    var originalObjectMapping = new ObjectGraphMappingBuilder();
    originalObjectMapping.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("entity"));
    originalObjectMapping.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    originalObjectMapping.addField("children")
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("child"));
    var genericBuilder = new GenericOGMBuilder();
    var builder = genericBuilder.copyGraphMappingAsBuilder(originalObjectMapping.build());
    this.thenObjectApproved(builder.build(), this.getOptions());
  }

  private ObjectToJSonStringOptions getOptions() {
    return new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT,
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS
    );
  }
}