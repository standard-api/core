package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
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
        new GraphDescriptionBuilder().addNodeDescription("entity")
    );
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

  @Test
  public void itCanGetCompositeGraphDescriptionFromOGMWithObjectOGM() {
    var givenOgmBuilder = new ObjectGraphMappingBuilder();
    givenOgmBuilder.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("Entity"));
    givenOgmBuilder.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );

    var innerObjectBuilder = givenOgmBuilder.addField("innerObject")
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addOutgoingEdge("edge")
                .addNodeDescription("InnerEntity")
        );
    
    innerObjectBuilder.addField("innerName")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("innerName")
                .addStringAttributeValue()
        );

    var givenOgm = givenOgmBuilder.build();
    var actual = GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(givenOgm);
    this.thenObjectApproved(actual);
  }

  @Test
  public void itCanGetCompositeGraphDescriptionFromOGMWithMap() {
    var givenOgmBuilder = new ObjectGraphMappingBuilder();
    givenOgmBuilder.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("entity")
    );
    givenOgmBuilder.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    var mapMapping = givenOgmBuilder.addField("children")
        .addMapAsObjectFieldMapping();
    mapMapping.addLeafKeyMapping();
    mapMapping.addLeafValueMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("child"));
    
    var givenOgm = givenOgmBuilder.build();
    var actual = GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(givenOgm);
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanGetCompositeGraphDescriptionFromOGMWithList() {
    var givenOgmBuilder = new ObjectGraphMappingBuilder();
    givenOgmBuilder.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription("entity"));
    givenOgmBuilder.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    givenOgmBuilder.addField("children")
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("child"));

    var givenOgm = givenOgmBuilder.build();
    var actual = GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(givenOgm);
    this.thenObjectApproved(actual);
  }

  private ObjectToJSonStringOptions getOptions() {
    return new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT,
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS
    );
  }
}