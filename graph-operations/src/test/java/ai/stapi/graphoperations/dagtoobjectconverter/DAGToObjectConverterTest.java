package ai.stapi.graphoperations.dagtoobjectconverter;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.dagtoobjectconverter.exceptions.CannotConvertDAGToObject;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class DAGToObjectConverterTest extends SchemaIntegrationTestCase {
    
    @Autowired
    private DAGToObjectConverter dagToObjectConverter;

    @Test
    void itWillThrowExceptionWhenCyclicGraphProvided() {
        var a = new Node(new UniqueIdentifier("id-1"), "A");
        var b = new Node(new UniqueIdentifier("id-2"),"B");
        var givenCyclicGraph = new Graph(a, b, new Edge(a, "1", b), new Edge(b, "2", a));
        var givenNode = givenCyclicGraph.traversable().loadNode(
            a.getId(),
            a.getType()
        );
        Executable throwable = () -> this.dagToObjectConverter.convert(givenNode);
        this.thenExceptionMessageApproved(CannotConvertDAGToObject.class, throwable);
    }

    @Test
    void itWillThrowExceptionWhenBiggerCyclicGraphProvided() {
        var a = new Node(new UniqueIdentifier("id-1"),"A");
        var b = new Node(new UniqueIdentifier("id-2"),"B");
        var c = new Node(new UniqueIdentifier("id-3"),"C");
        var d = new Node(new UniqueIdentifier("id-4"),"D");
        var e = new Node(new UniqueIdentifier("id-5"),"E");
        var e1 = new Edge(a, "1", b);
        var e2 = new Edge(b, "2", c);
        var e3 = new Edge(c, "3", d);
        var e4 = new Edge(d, "4", e);
        var e5 = new Edge(e, "5", a);
        var givenCyclicGraph = new Graph(a, b, c, d, e, e1, e2, e3, e4, e5);
        var givenNode = givenCyclicGraph.traversable().loadNode(c.getId(), c.getType());
        Executable throwable = () -> this.dagToObjectConverter.convert(givenNode);
        this.thenExceptionMessageApproved(CannotConvertDAGToObject.class, throwable);
    }
    
    @Test
    void itCanConvertSimpleDAG() {
        var house = new Node(
            new UniqueIdentifier("id-1"),
            "House",
            new LeafAttribute<>("title", new StringAttributeValue("mohykan")),
            new LeafAttribute<>("height", new IntegerAttributeValue(500))
        );
        var door = new Node(
            new UniqueIdentifier("id-2"),
            "Door",
            new LeafAttribute<>("closable", new BooleanAttributeValue(false)),
            new LeafAttribute<>("width", new IntegerAttributeValue(20))
        );
        var door2 = new Node(
            new UniqueIdentifier("id-3"),
            "Door",
            new LeafAttribute<>("closable", new BooleanAttributeValue(true)),
            new LeafAttribute<>("width", new IntegerAttributeValue(15))
        );
        var handle = new Node(
            new UniqueIdentifier("id-4"),
            "Handle",
            new LeafAttribute<>("color", new StringAttributeValue("red"))
        );
        var edge1 = new Edge(house, "hasDoor", door);
        var edge2 = new Edge(house, "hasDoor", door2);
        var edge3 = new Edge(door, "hasHandle", handle);
        var givenGraph = new Graph(
            house,
            door,
            door2,
            handle,
            edge1,
            edge2,
            edge3
        );
        var givenNode = givenGraph.traversable().loadNode(house.getId(), house.getType());
        var actualObject = this.dagToObjectConverter.convert(givenNode);
        this.thenObjectApproved(actualObject);
    }
}