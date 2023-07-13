package ai.stapi.graphoperations.objectGraphMaper;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ListAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations.ExampleCreateCommand;
import ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations.ExampleEntityId;
import ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations.House;
import ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations.Person;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.exceptions.ObjectGraphMapperException;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.test.integration.IntegrationTestCase;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class ObjectGraphMapperTest extends IntegrationTestCase {

  @Autowired
  private GenericObjectGraphMapper objectGraphMapper;

  @Test
  void itThrowErrorWhenProvidedObjectIsNull() {
    var definition = new ObjectObjectGraphMapping(
        new NodeDescription(new NodeDescriptionParameters("irrelevant")),
        Map.of()
    );
    Executable throwable = () -> this.objectGraphMapper.mapToGraph(
        definition,
        (Object) null
    );
    this.thenExceptionMessageApproved(
        ObjectGraphMapperException.class,
        throwable
    );
  }

  @Test
  void itThrowsErrorWhenFirstObjectGraphMappingIsLeaf() {
    var definition =
        new LeafObjectGraphMapping(
            new AttributeQueryDescription("irrelevant")
        );
    Executable throwable = () -> this.objectGraphMapper.mapToGraph(
        definition,
        new Object()
    );
    this.thenExceptionMessageApproved(
        ObjectGraphMapperException.class,
        throwable
    );
  }

  @Test
  void itThrowsErrorWhenFirstObjectGraphMappingIsNull() {
    Executable throwable = () -> this.objectGraphMapper.mapToGraph(
        null,
        new Object()
    );
    this.thenExceptionMessageApproved(
        ObjectGraphMapperException.class,
        throwable
    );
  }

  @Test
  void itCanCreateNodeWithSpecificUuidAndTypeByEntityIdentifierAndWithAttribute() {
    var commandId = UUID.randomUUID();
    var exampleEntityId = new ExampleEntityId(commandId);
    var command = new ExampleCreateCommand(
        exampleEntityId,
        "example_create_command_name"
    );
    var commandDefinition = new ObjectGraphMappingBuilder();
    commandDefinition.addField("fallbackName")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    var commandNodeType = "command_node";
    commandDefinition.addField("exampleEntityId")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(commandNodeType))
        .addField("id")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());

    var graph = this.objectGraphMapper.mapToGraph(
        commandDefinition.build(),
        command
    ).getGraph();
    var node = graph.getNode(exampleEntityId, commandNodeType);
    this.thenGraphApproved(graph);
  }

  @Test
  void itThrowsErrorWhenObjectGraphMappingFieldDoesNotExists() {
    var personId = UUID.randomUUID();
    Person person = new Person(personId, "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("person_node"));
    personDefinition.addField("randomField")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("irrelevant")
                .addStringAttributeValue()
        );
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(personDefinition.build(), person);
    this.thenExceptionMessageApprovedWithHiddenUuids(SpecificObjectGraphMapperException.class,
        throwable);
  }

  @Test
  void itShouldMapWhatItCanWhenSomeFieldIsMissingInValueObjectAndLenientFlagWasProvided() {
    var personId = UUID.randomUUID();
    Person person = new Person(personId, "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("person_node"));
    personDefinition.addField("randomField")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("irrelevant")
                .addStringAttributeValue()
        );
    personDefinition.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );

    var actual = this.objectGraphMapper.mapToGraph(
        personDefinition.build(),
        person,
        MissingFieldResolvingStrategy.LENIENT
    );
    this.thenGraphApproved(actual.getGraph());
  }

  @Test
  void itThrowsErrorWhenObjectGraphMappingDescribesWrongObject() {
    var personId = UUID.randomUUID();
    Person person = new Person(personId, "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("person_node"));
    personDefinition.addField("name")
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("irrelevant")
                .addStringAttributeValue()
        );
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(personDefinition.build(), person);
    this.thenExceptionMessageApproved(SpecificObjectGraphMapperException.class, throwable);
  }

  @Test
  void itThrowsErrorWhenIdentifyingObjectDoesNotHaveIdentifyingField() {
    var commandId = UUID.randomUUID();
    var command = new ExampleCreateCommand(
        new ExampleEntityId(commandId),
        "example_create_command_name"
    );
    var commandDefinition = new ObjectGraphMappingBuilder();
    commandDefinition.addField("fallbackName")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    commandDefinition.addField("exampleEntityId")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("command_node"))
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(commandDefinition.build(), command);
    this.thenExceptionMessageApproved(SpecificObjectGraphMapperException.class, throwable);
  }

  @Test
  void itThrowsErrorWhenObjectHasMultipleIdentifyingFields() {
    var commandId = UUID.randomUUID();
    var command = new ExampleCreateCommand(
        new ExampleEntityId(commandId),
        "example_create_command_name"
    );
    var commandDefinition = new ObjectGraphMappingBuilder();
    commandDefinition.addField("fallbackName")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    commandDefinition.addField("exampleEntityId")
        .setRelation(new EntityIdentifier())
        .addObjectAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("command_node"))
        .addField("id")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(commandDefinition.build(), command);
    this.thenExceptionMessageApproved(SpecificObjectGraphMapperException.class, throwable);
  }

  @Test
  void itCanMapObjectMappingWithCertainId() {
    var personId = UUID.randomUUID();
    var personNodeType = "person_node";
    var person = new Person(personId, "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(personNodeType));
    personDefinition.addField("id").addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    var result = this.objectGraphMapper.mapToGraph(personDefinition.build(), person).getGraph();
    result.getNode(new UniversallyUniqueIdentifier(personId), personNodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itCanMapObjectWithMultipleLeafs() {
    var personId = UUID.randomUUID();
    var personNodeType = "person_node";
    var person = new Person(personId, "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(personNodeType));
    personDefinition.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    personDefinition.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    var result = this.objectGraphMapper.mapToGraph(personDefinition.build(), person).getGraph();
    var node = result.getNode(new UniversallyUniqueIdentifier(personId), personNodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itCanMapObjectDefinitionWithMultipleLeafs() {
    Person person = new Person(UUID.randomUUID(), "Mirek", "Dobrota", "Cech");
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("person_node"));
    personDefinition.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    personDefinition.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    personDefinition.addField("surname")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("surname")
                .addStringAttributeValue()
        );
    personDefinition.addField("nationality")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("nationality")
                .addStringAttributeValue()
        );
    var result = this.objectGraphMapper.mapToGraph(personDefinition.build(), person);
    this.thenGraphApproved(result.getGraph());
  }

  @Test
  void itCanMapObjectDefinitionWithList() {
    var resident1 = new Person(UUID.randomUUID(), "Mirek", "Dobrota", "Cech");
    var resident2 = new Person(UUID.randomUUID(), "Lukas", "Voracek", "Cech");
    var resident3 = new Person(UUID.randomUUID(), "Jack", "Sparrow", "Pirat");
    var house = new House(
        UUID.randomUUID(),
        List.of(
            resident1,
            resident2,
            resident3
        ),
        "Vorackova 30",
        List.of(
            "kuchyne",
            "loznice",
            "koupelna"
        )
    );
    var houseNodeType = "house";
    var personNodeType = "resident";
    var houseMap = new ObjectGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(houseNodeType)
    );
    houseMap.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    houseMap.addField("address")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("address")
                .addStringAttributeValue()
        );
    houseMap.addField("listOfRooms")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_room"))
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addNodeDescription("room")
                .addLeafAttribute("room_name")
                .addStringAttributeValue()
        );
    var personMap = houseMap.addField("residents")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_resident"))
        .addListAsObjectFieldMapping()
        .addObjectChildDefinition();
    personMap.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(personNodeType));
    personMap.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    personMap.addField("name")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("name")
                .addStringAttributeValue()
        );
    personMap.addField("surname")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("surname")
                .addStringAttributeValue()
        );
    personMap.addField("nationality")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("nationality")
                .addStringAttributeValue()
        );
    var result = this.objectGraphMapper.mapToGraph(houseMap.build(), house).getGraph();
    var node = result.getNode(new UniversallyUniqueIdentifier(resident1.getId()), personNodeType);
    node = result.getNode(new UniversallyUniqueIdentifier(resident2.getId()), personNodeType);
    node = result.getNode(new UniversallyUniqueIdentifier(resident3.getId()), personNodeType);
    node = result.getNode(new UniversallyUniqueIdentifier(house.getId()), houseNodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itCanMapObjectDefinitionWithRemovalList() {
    Person resident1 = new Person(UUID.randomUUID(), "Mirek", "Dobrota", "Cech");
    Person resident2 = new Person(UUID.randomUUID(), "Lukas", "Voracek", "Cech");
    Person resident3 = new Person(UUID.randomUUID(), "Jack", "Sparrow", "Pirat");
    House house = new House(
        UUID.randomUUID()
        , List.of(
        resident1,
        resident2,
        resident3
    ),
        "Vorackova 30",
        List.of(
            "kuchyne",
            "loznice",
            "koupelna"
        )
    );
    var houseMap = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("house"));
    houseMap.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    houseMap.addField("address")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("address")
                .addStringAttributeValue()
        );
    houseMap.addField("listOfRooms")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_room"))
        .addListAsObjectFieldMapping()
        .addLeafChildDefinition()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addNodeDescription("room")
                .addLeafAttribute("room_name")
                .addStringAttributeValue()
        );
    var personMap = houseMap.addField("residents")
        .addListAsObjectFieldMapping()
        .addObjectChildDefinition();
    personMap.addField("id")
        .setRelation(new EntityIdentifier())
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new GraphDescriptionBuilder()
            .addRemovalNodeDescription("resident")
            .addUuidDescription()
        );
    var result = this.objectGraphMapper.mapToGraph(houseMap.build(), house);
    Assertions.assertTrue(result.getElementForRemoval().stream().anyMatch(
        element -> element.getGraphElementId().getId().equals(resident1.getId().toString())));
    Assertions.assertTrue(result.getElementForRemoval().stream().anyMatch(
        element -> element.getGraphElementId().getId().equals(resident2.getId().toString())));
    Assertions.assertTrue(result.getElementForRemoval().stream().anyMatch(
        element -> element.getGraphElementId().getId().equals(resident3.getId().toString())));
    this.thenGraphApproved(result.getGraph());
  }

  @Test
  void itThrowsErrorWhenMappingDefinitionDoesntCorrespondWithActualObject() {
    var entityId = new ExampleEntityId("d47a46a9-ba14-44d2-a425-78a4c2c95de1");
    var commandObject = new ExampleCreateCommand(
        entityId,
        "exampleName"
    );
    var commandDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("command_node"));
    commandDefinition.addField("fallbackName")
        .addObjectAsObjectFieldMapping()
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(commandDefinition.build(), commandObject);
    this.thenExceptionMessageApproved(SpecificObjectGraphMapperException.class, throwable);
  }

  @Test
  void itThrowErrorWhenObjectUnderInterfaceIsNotSerializable() {
    var personId = UUID.fromString("e523b902-6b19-4b14-ac68-d7bdecd09b66");
    var person = new Person(
        personId,
        "Jarda",
        "Hruska",
        "Ceska"
    );
    var personDefinition = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("person_node"));
    personDefinition.addField("id")
        .addLeafAsObjectFieldMapping().setGraphDescription(new UuidIdentityDescription());
    personDefinition.addField("name")
        .addInterfaceAsObjectFieldMapping()
        .setUuid(UUID.randomUUID());
    Executable throwable =
        () -> this.objectGraphMapper.mapToGraph(personDefinition.build(), person);
    this.thenExceptionMessageApproved(SpecificObjectGraphMapperException.class, throwable);
  }

  @Test
  void itCanMapObjectDefinitionWithListAsListAttribute() {
    House house = new House(
        UUID.randomUUID(),
        List.of(),
        "Vorackova 30",
        List.of(
            "kuchyne",
            "loznice",
            "koupelna"
        )
    );
    var houseType = "house";
    var houseMap = new ObjectGraphMappingBuilder()
        .setGraphDescription(new GraphDescriptionBuilder().addNodeDescription(houseType));
    houseMap.addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());
    houseMap.addField("address")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(
            new GraphDescriptionBuilder()
                .addLeafAttribute("address")
                .addStringAttributeValue()
        );
    houseMap.addField("listOfRooms")
        .addListAsObjectFieldMapping()
        .setGraphDescription(new ListAttributeDescription("room_name"))
        .addLeafChildDefinition()
        .setGraphDescription(
            new GraphDescriptionBuilder().addStringAttributeValue()
        );
    var result = this.objectGraphMapper.mapToGraph(houseMap.build(), house).getGraph();
    var node = result.getNode(new UniversallyUniqueIdentifier(house.getId()), houseType);
    this.thenGraphApproved(result);
  }

//    @Test
//    public void itCanResolveDefinitionsWithInterfaces()
//    {
//        var conceptDefinition = new ObjectGraphMappingBuilder();
//        var conceptID = new ExampleEntityId("e523b902-6b19-4b14-ac68-d7bdecd09b66");
//        var command = new ExampleCreateConceptCommand(
//                conceptID,
//                "concept_name",
//                conceptDefinition.build()
//        );
//        var commandDefinition = new ObjectGraphMappingBuilder();
//        commandDefinition.setGraphDescription(new GraphDescriptionBuilder().addNodeDescription("command_node"));
//        commandDefinition.addField("targetIdentifier")
//                .setDeclaration(new EntityIdentifier())
//                .addObject()
//                .addField("id")
//                .setDeclaration(new EntityIdentifier())
//                .addLeaf()
//                .setGraphDescription(new UuidIdentityDescription());
//        commandDefinition.addField("fallbackName")
//                .addLeaf()
//                .setGraphDescription(new GraphDescriptionBuilder().addStringAttribute("name"));
//        commandDefinition.addField("conceptDefinition")
//                .setDeclaration(new GraphDescriptionBuilder().addOutgoingEdge("contains_definition"))
//                .addInterface()
//                .setUuid(UniversallyUniqueIdentifier.randomUUID());
//        var result = this.objectGraphMapper.mapToGraph(commandDefinition.build(), command);
//        this.thenGraphApproved(result.getGraph());
//    }
}
