package ai.stapi.graphoperations.graphbuilder;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.attribute.attributeValue.BooleanAttributeValue;
import ai.stapi.graph.attribute.attributeValue.DecimalAttributeValue;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

public class GraphBuilderTest extends IntegrationTestCase {

  @Autowired
  private GenericAttributeFactory genericAttributeFactory;

  @Test
  public void itThrowsErrorWhenNodeIsMissingType() {
    var builder = new GraphBuilder();
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID());
    Executable throwable = () -> builder.build(this.genericAttributeFactory);
    this.thenExceptionMessageApproved(GraphBuilderException.class, throwable);
  }

  @Test
  public void itThrowsErrorWhenEdgeIsFirst() {
    var builder = new GraphBuilder();
    Executable throwable = () -> builder.addEdge().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("irrelevant");
    this.thenExceptionMessageApproved(GraphBuilderException.class, throwable);
  }

  @Test
  public void itThrowsErrorWhenEdgeIsLast() {
    var builder = new GraphBuilder();
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID()).setType("irrelevant");
    builder.addEdge().setEdgeDirection(EdgeDirection.OUTGOING)
        .setId(UniversallyUniqueIdentifier.randomUUID()).setType("irrelevant");
    Executable throwable = () -> builder.build(this.genericAttributeFactory);
    this.thenExceptionMessageApproved(GraphBuilderException.class, throwable);
  }

  @Test
  public void itThrowsErrorWhenNodeIsMissingId() {
    var builder = new GraphBuilder();
    builder.addNode().setType("example_type");
    Executable throwable = () -> builder.build(this.genericAttributeFactory);
    this.thenExceptionMessageApproved(GraphBuilderException.class, throwable);
  }

  @Test
  public void itCanCreateNodeWithSpecificIdAndType() {
    var id = UniversallyUniqueIdentifier.fromString("b7842bbd-09c5-4ffe-946f-38b80d1f42ad");
    var nodeType = "example_type";
    var builder = new GraphBuilder();
    builder.addNode()
        .setId(id)
        .setType(nodeType);
    var graph = builder.build(this.genericAttributeFactory);
    graph.getNode(id, nodeType);
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateEdgeWithSpecificIdAndType() {
    var id = UniversallyUniqueIdentifier.fromString("2a9f9fdc-7f6b-4cc7-bd86-daa6deaa6152");
    var builder = new GraphBuilder();
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID()).setType("irrelevant_type");
    builder.addEdge().setEdgeDirection(EdgeDirection.OUTGOING).setId(id).setType("example_edge");
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("another_irrelevant_type");
    var graph = builder.build(this.genericAttributeFactory);
    graph.getEdge(id, "example_edge");
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateMultipleEdgesWithSpecificIdAndType() {
    var firstId = UniversallyUniqueIdentifier.fromString("2a9f9fdc-7f6b-4cc7-bd86-daa6deaa6152");
    var secondId = UniversallyUniqueIdentifier.fromString("2d3897d1-baf4-4578-a45b-bb8c07267b6e");
    var builder = new GraphBuilder();
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID()).setType("first_node");
    builder.addEdge().setEdgeDirection(EdgeDirection.OUTGOING).setId(firstId).setType("first_edge");
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID()).setType("second_node");
    builder.addEdge().setEdgeDirection(EdgeDirection.INGOING).setId(secondId)
        .setType("second_edge");
    builder.addNode().setId(UniversallyUniqueIdentifier.randomUUID()).setType("third_node");
    var graph = builder.build(this.genericAttributeFactory);
    graph.getEdge(firstId, "first_edge");
    graph.getEdge(secondId, "second_edge");
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateNodeWithSpecificIdAndTypeAndAttribute() {
    var id = UniversallyUniqueIdentifier.fromString("b7842bbd-09c5-4ffe-946f-38b80d1f42ad");
    var nodeType = "example_type";
    var builder = new GraphBuilder();
    var nodeBuilder = builder.addNode()
        .setId(id)
        .setType(nodeType);
    nodeBuilder.addAttribute()
        .setAttributeName("string_attribute")
        .addAttributeValue(
            new AttributeValueFactoryInput(
                "string_value",
                StringAttributeValue.SERIALIZATION_TYPE
            )
        )
        .setAttributeStructureType(LeafAttribute.DATA_STRUCTURE_TYPE);
    nodeBuilder.addAttribute()
        .setAttributeName("integer_attribute")
        .addAttributeValue(
            new AttributeValueFactoryInput(
                1,
                IntegerAttributeValue.SERIALIZATION_TYPE
            )
        )
        .setAttributeStructureType(LeafAttribute.DATA_STRUCTURE_TYPE);
    nodeBuilder.addAttribute()
        .setAttributeName("boolean_attribute")
        .addAttributeValue(
            new AttributeValueFactoryInput(
                true,
                BooleanAttributeValue.SERIALIZATION_TYPE
            )
        )
        .setAttributeStructureType(LeafAttribute.DATA_STRUCTURE_TYPE);
    nodeBuilder.addAttribute()
        .setAttributeName("decimal_attribute")
        .addAttributeValue(
            new AttributeValueFactoryInput(
                5.6231,
                DecimalAttributeValue.SERIALIZATION_TYPE
            )
        )
        .setAttributeStructureType(LeafAttribute.DATA_STRUCTURE_TYPE);

    var graph = builder.build(this.genericAttributeFactory);
    graph.getNode(id, nodeType);
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateGraphWithBranches() {
    var firstBranch = new GraphBuilder();
    firstBranch.addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("first_branch_node");
    var secondBranch = firstBranch.createNewBranch();
    secondBranch.addEdge().setEdgeDirection(EdgeDirection.OUTGOING)
        .setId(UniversallyUniqueIdentifier.randomUUID()).setType("second_branch_edge");
    secondBranch.createNewBranch().addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("third_branch_node");
    var graph = firstBranch.build(this.genericAttributeFactory);
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateGraphWithMultipleBranches() {
    var firstBranch = new GraphBuilder();
    firstBranch.addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("first_branch_node");
    var secondBranch = firstBranch.createNewBranch();
    secondBranch.addEdge().setEdgeDirection(EdgeDirection.OUTGOING)
        .setId(UniversallyUniqueIdentifier.randomUUID()).setType("second_branch_edge");
    secondBranch.addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("second_branch_node");
    var thirdBranch = firstBranch.createNewBranch();
    thirdBranch.addEdge().setEdgeDirection(EdgeDirection.OUTGOING)
        .setId(UniversallyUniqueIdentifier.randomUUID()).setType("third_branch_edge");
    thirdBranch.addNode().setId(UniversallyUniqueIdentifier.randomUUID())
        .setType("third_branch_node");
    var graph = firstBranch.build(this.genericAttributeFactory);
    this.thenGraphApproved(graph);
  }

  @Test
  public void itCanCreateGraphWithElementsForRemoval() {
    var builder = new GraphBuilder();
    builder.addNodeForRemoval()
        .setType("node_type")
        .setId(UniversallyUniqueIdentifier.fromString("0d15149f-a0a8-42d1-a1bd-891355a637ae"));
    builder.addNodeForRemoval()
        .setType("another_type")
        .setId(UniversallyUniqueIdentifier.fromString("45e4088d-2ca1-410c-9a88-98be01d585a6"));
    var elements = builder.buildElementsForRemoval();
    elements.sort((e1, e2) -> e1.getGraphElementId().compareTo(e2.getGraphElementId()));
    this.thenObjectApproved(elements, this.getOptions());
  }

  private ObjectToJSonStringOptions getOptions() {
    return new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_CREATED_AT,
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS
    );
  }
}
