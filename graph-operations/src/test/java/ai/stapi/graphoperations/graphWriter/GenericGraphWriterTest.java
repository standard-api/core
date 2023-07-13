package ai.stapi.graphoperations.graphWriter;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.StringAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphWriter.ExampleImplementations.ExampleAmbiguousGraphDescription;
import ai.stapi.graphoperations.graphWriter.ExampleImplementations.ExampleNotSupportedGraphDescription;
import ai.stapi.graphoperations.graphWriter.exceptions.GenericGraphWriterException;
import ai.stapi.graphoperations.graphWriter.exceptions.SpecificGraphWriterException;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class GenericGraphWriterTest extends IntegrationTestCase {

  @Autowired
  private GenericGraphWriter graphWriter;

  @Test
  void itThrowsErrorWhenGraphDescriptionIsNotSupported() {
    var description = new NodeDescription(
        new NodeDescriptionParameters("irrelevant"),
        new ExampleNotSupportedGraphDescription()
    );
    Executable throwable =
        () -> this.graphWriter.createGraph(UniversallyUniqueIdentifier.randomUUID(), description);
    this.thenExceptionMessageApproved(
        GenericGraphWriterException.class,
        throwable
    );
  }

  @Test
  void itThrowsErrorWhenGraphDescriptionIsSupportedByMultipleResolvers() {
    var description = new NodeDescription(
        new NodeDescriptionParameters("irrelevant"),
        new ExampleAmbiguousGraphDescription()
    );
    Executable throwable =
        () -> this.graphWriter.createGraph(UniversallyUniqueIdentifier.randomUUID(), description);
    this.thenExceptionMessageApproved(
        GenericGraphWriterException.class,
        throwable
    );
  }

  @Test
  void itCanCreateNodeWithGivenId() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var nodeType = "example_node";
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription(nodeType);
    var result = this.graphWriter.createGraph(nodeId, builder.getOnlyPositiveGraphDescriptions());
    var node = result.getNode(nodeId, nodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itThrowErrorWhenGraphDescriptionStartsWithEdge() {
    var builder = new GraphDescriptionBuilder();
    builder.addOutgoingEdge("edgeType");
    Executable throwable =
        () -> this.graphWriter.createGraph(UniversallyUniqueIdentifier.randomUUID(),
            builder.getOnlyPositiveGraphDescriptions());
    this.thenExceptionMessageApproved(GenericGraphWriterException.class, throwable);
  }

  @Test
  void itCanCreateNodeWithGivenIdAndAttributeValue() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var nodeType = "example_node";
    var description = new NodeDescription(
        new NodeDescriptionParameters(nodeType),
        new UuidIdentityDescription(
            new ConstantDescription(new ConstantDescriptionParameters(nodeId))
        ),
        new LeafAttributeDescription(
            "example_attribute_name",
            new StringAttributeValueDescription(
                new ConstantDescription(new ConstantDescriptionParameters("example_value"))
            )
        )
    );
    var result = this.graphWriter.createGraph(nodeId, description);
    var node = result.getNode(nodeId, nodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itCanCreateMultipleNodesAndAttributeAtTheEnd() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var nodeType = "first_node";
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription(nodeType)
        .addOutgoingEdge("first_edge")
        .addNodeDescription("second_node")
        .addLeafAttribute("example_attribute")
        .addStringAttributeValue()
        .addConstantDescription("example_value");
    var result = this.graphWriter.createGraph(nodeId, builder.getOnlyPositiveGraphDescriptions());
    var node = result.getNode(nodeId, nodeType);
    this.thenGraphApproved(result);
  }

  @Test
  void itThrowsErrorWhenCreatingEmptyAttribute() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("first_node")
        .addOutgoingEdge("first_edge")
        .addNodeDescription("second_node")
        .addLeafAttribute("example_attribute")
        .addStringAttributeValue();
    Executable throwable =
        () -> this.graphWriter.createGraph(nodeId, builder.getOnlyPositiveGraphDescriptions());
    this.thenExceptionMessageApproved(SpecificGraphWriterException.class, throwable);
  }

  @Test
  void itDoesNotWriteIncompleteEdgesToGraph() {
    var graphDescription = new GraphDescriptionBuilder()
        .addNodeDescription("house")
        .addOutgoingEdge("has_resident")
        .build();
    var result = this.graphWriter.createGraph(
        (PositiveGraphDescription) graphDescription
    );
    this.thenGraphApproved(result);
  }

}
