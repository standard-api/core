package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer;

import ai.stapi.graphoperations.exampleImplementations.ExampleDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListAttribute;
import ai.stapi.graphoperations.exampleImplementations.ExampleDtoWithListOfStrings;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDto;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithInterface;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithListOfInterfaces;
import ai.stapi.graphoperations.exampleImplementations.ExampleNestedDtoWithMapOfInterfaces;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleDtoWithListAttributeSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithInterfaceSpecificGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithListOfInterfacesGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exampleImplementations.exampleOgmProviders.ExampleNestedDtoWithMapOfInterfacesGraphMappingProvider;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graph.Graph;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception.GenericGraphOgmDeserializerException;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.OgmGraphElementTypes;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.test.integration.IntegrationTestCase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class GenericGraphToObjectDeserializerTest extends IntegrationTestCase {

  @Autowired
  private GenericGraphToObjectDeserializer deserializer;

  @Autowired
  private GenericObjectGraphMapper objectGraphMapper;

  @Autowired
  private GenericGraphMappingProvider mappingProvider;

  @Test
  void itThrowsErrorWhenNodeTypeIsNotSupportedForDeserialization() {
    var graph = new Graph(new Node("random_type")).traversable();
    Executable throwable = () -> deserializer.deserialize(
        graph.loadAllNodes("random_type").get(0),
        AbstractSerializableObject.class,
        graph
    );
    this.thenExceptionMessageApproved(GenericGraphOgmDeserializerException.class, throwable);
  }

  @Test
  void itCanDeserializeSimpleDto() {
    var graph = this.objectGraphMapper.mapToGraph(
        new ExampleDto("Ivan Mladek")
    ).getGraph().traversable();
    var result = this.deserializer.deserialize(
        graph.loadAllNodes("example_dto_node").get(0),
        ExampleDto.class,
        graph
    );
    this.thenObjectApproved(result, this.getThisOptions());
  }

  @Test
  void itCanDeserializeDtoWithNestedDto() {
    var graph = this.objectGraphMapper.mapToGraph(
        new ExampleNestedDto(
            "Master name",
            new ExampleDto("Ivan Mladek")
        )
    ).getGraph().traversable();
    var result = this.deserializer.deserialize(
        graph.loadAllNodes(ExampleNestedDtoSpecificGraphMappingProvider.NODE_TYPE).get(0),
        ExampleNestedDto.class,
        graph
    );
    this.thenObjectApproved(result, this.getThisOptions());
  }

  @Test
  void itCanDeserializeDtoWithListAttributes() {
    var graph = this.objectGraphMapper.mapToGraph(
        new ExampleDtoWithListAttribute(List.of(
            "Tag 1 ", "Tag 2", "Tag 3"
        )
        )
    ).getGraph().traversable();
    var result = this.deserializer.deserialize(
        graph.loadAllNodes(ExampleDtoWithListAttributeSpecificGraphMappingProvider.NODE_TYPE).get(0),
        ExampleDtoWithListAttribute.class,
        graph
    );
    this.thenObjectApproved(result, this.getThisOptions());
  }

  @Test
  void itCanDeserializeDtoWithInterface() {
    var testedDto = new ExampleNestedDtoWithInterface(
        "Interface dto",
        new ExampleDtoWithListOfStrings(
            List.of("Marek", "Zdenka", "Petr")
        )
    );
    var graph = this.objectGraphMapper.mapToGraph(testedDto).getGraph().traversable();
    var result = this.deserializer.deserialize(
        graph.loadAllNodes(ExampleNestedDtoWithInterfaceSpecificGraphMappingProvider.NODE_TYPE)
            .get(0),
        ExampleNestedDtoWithInterface.class,
        graph
    );
    var nestedDto = (ExampleDtoWithListOfStrings) result.getChildDto();
    Assertions.assertEquals(3, nestedDto.getNames().size());
    Assertions.assertEquals(ExampleDtoWithListOfStrings.SERIALIZATION_TYPE,
        nestedDto.getSerializationType());
    Assertions.assertEquals(ExampleNestedDtoWithInterface.SERIALIZATION_TYPE,
        result.getSerializationType());
  }

  @Test
  void itCanDeserializeDtoWithListOfInterfaces() {
    var nestedDto1 = new ExampleDto("Pushkin");
    var nestedDto2 = new ExampleDto("Metrostav");
    var nestedDto3 = new ExampleDto("CocaCola");
    var testedDto = new ExampleNestedDtoWithListOfInterfaces(
        "dto with list of interfaces",
        List.of(nestedDto1, nestedDto2, nestedDto3)
    );
    var graph = this.objectGraphMapper.mapToGraph(testedDto).getGraph().traversable();
    var deserializedDto = this.deserializer.deserialize(
        graph.loadAllNodes(ExampleNestedDtoWithListOfInterfacesGraphMappingProvider.NODE_TYPE)
            .get(0),
        ExampleNestedDtoWithListOfInterfaces.class,
        graph
    );
    Assertions.assertEquals(3, deserializedDto.getChildDtos().size());
    Assertions.assertEquals(ExampleNestedDtoWithListOfInterfaces.SERIALIZATION_TYPE,
        deserializedDto.getSerializationType());
  }

  @Test
  void itCanDeserializeDtoWithMapOfInterfaces() {
    var nestedDto1 = new ExampleDto("Pushkin");
    var nestedDto2 = new ExampleDto("Metrostav");
    var nestedDto3 = new ExampleDto("CocaCola");
    var testedDto = new ExampleNestedDtoWithMapOfInterfaces(
        "dto with list of interfaces",
        Map.ofEntries(
            Map.entry(nestedDto1.getName(), nestedDto1),
            Map.entry(nestedDto2.getName(), nestedDto2),
            Map.entry(nestedDto3.getName(), nestedDto3)
        )
    );
    var graph = this.objectGraphMapper.mapToGraph(testedDto).getGraph().traversable();
    var deserializedDto = this.deserializer.deserialize(
        graph.loadAllNodes(ExampleNestedDtoWithMapOfInterfacesGraphMappingProvider.NODE_TYPE)
            .get(0),
        ExampleNestedDtoWithMapOfInterfaces.class,
        graph
    );
    Assertions.assertEquals(3, deserializedDto.getChildDtos().size());
    Assertions.assertEquals(ExampleNestedDtoWithMapOfInterfaces.SERIALIZATION_TYPE,
        deserializedDto.getSerializationType());
    Assertions.assertEquals(ExampleDto.SERIALIZATION_TYPE,
        deserializedDto.getChildDtos().get(nestedDto1.getName()).getSerializationType());
  }

  @Test
  void itCanDeserializeSimpleOgm() {
    var graph = this.getSavedOgmSimpleExample();
    var rootNode = this.getOgmRootNode(graph);
    var result = this.deserializer.deserialize(
        rootNode,
        ObjectObjectGraphMapping.class,
        graph
    );

    this.thenObjectApproved(result, this.getThisOptions());
  }

  @Test
  void itCanDeserializeComplexOgm() {
    var graph = this.getSavedOgmMoreComplexExample();
    var rootNode = this.getOgmRootNode(graph);
    var result = this.deserializer.deserialize(
        rootNode,
        ObjectObjectGraphMapping.class,
        graph
    );
    this.thenObjectApproved(result);
  }

  @Test
  void itCanDeserializeComplexOgmWithIdentityIdentifier() {
    var graph = this.getSavedOgmMoreComplexWithEntityIdentifierExample();
    var rootNode = this.getOgmRootNode(graph);
    var result = this.deserializer.deserialize(
        rootNode,
        ObjectObjectGraphMapping.class,
        graph
    );
    this.thenObjectApproved(result, this.getThisOptions());
  }

  private TraversableNode getOgmRootNode(InMemoryGraphRepository contextualGraph) {
    var rootNode = contextualGraph.loadAllNodes(OgmGraphElementTypes.OGM_OBJECT_NODE).stream()
        .filter(node -> node.getEdges().stream()
            .noneMatch(edge -> edge.getNodeToId().equals(node.getId())))
        .toList();
    return rootNode.get(0);
  }

  private InMemoryGraphRepository getSavedOgmSimpleExample() {
    var builder = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("example_node")
        );
    var mapping =
        this.mappingProvider.provideGraphMapping(ObjectObjectGraphMapping.SERIALIZATION_TYPE);
    return this.objectGraphMapper.mapToGraph(
        mapping,
        builder.build()
    ).getGraph().traversable();
  }

  private InMemoryGraphRepository getSavedOgmMoreComplexExample() {
    var builder = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("example_node"));
    builder.addField("randomFieldName")
        .addObjectAsObjectFieldMapping()
        .addField("nestedObjectField")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("nestedAttribute")
                .addStringAttributeValue()
        );
    var mapping =
        this.mappingProvider.provideGraphMapping(ObjectObjectGraphMapping.SERIALIZATION_TYPE);
    return this.objectGraphMapper.mapToGraph(
        mapping,
        builder.build()
    ).getGraph().traversable();
  }

  private InMemoryGraphRepository getSavedOgmMoreComplexWithEntityIdentifierExample() {
    var builder = new ObjectGraphMappingBuilder();
    builder.addField("entityIdentifier")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("dto_identity"))
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    var mapping =
        this.mappingProvider.provideGraphMapping(ObjectObjectGraphMapping.SERIALIZATION_TYPE);
    return this.objectGraphMapper.mapToGraph(
        mapping,
        builder.build()
    ).getGraph().traversable();
  }

  private ObjectToJSonStringOptions getThisOptions() {
    return new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT,
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS
    );
  }


}