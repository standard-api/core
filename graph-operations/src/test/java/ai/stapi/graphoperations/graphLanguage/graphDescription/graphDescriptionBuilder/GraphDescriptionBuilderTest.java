package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.StringAttributeValueDescription;
import ai.stapi.test.integration.IntegrationTestCase;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GraphDescriptionBuilderTest extends IntegrationTestCase {

  @Test
  public void itCanCreateNodeDescription() {
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("example_node");
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateNodeEdgeNodeDescription() {
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("first_node")
        .addOutgoingEdge("example_edge")
        .addNodeDescription("second_node");
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateNodeWithAttribute() {
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("first_node")
        .addLeafAttribute("example_attribute")
        .addStringAttributeValue()
        .addConstantDescription("example_value");
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateUuidWithValue() {
    var id = UUID.fromString("1910dd45-1531-4e9b-a220-83e7954de339");
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("first_node")
        .addUuidDescription()
        .addConstantDescription(id);
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateBuilderCompositeFromGraphDescription() {
    var description = new NodeDescription(
        new NodeDescriptionParameters("example_type"),
        new LeafAttributeDescription(
            "example_attribute",
            new StringAttributeValueDescription(
                new ConstantDescription(new ConstantDescriptionParameters("my_value"))
            )
        )
    );
    var builder = new GraphDescriptionBuilder();
    var compositeBuilder = builder.convertToGraphDescriptionBuilderComposite(description);
    this.thenObjectApproved(compositeBuilder.build());
  }

  @Test
  public void itCanCreateGraphDescriptionWithMultipleBranches() {
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("first_branch_node");
    var secondBranch = builder.createNewBranch();
    secondBranch
        .addLeafAttribute("second_branch_attribute")
        .addStringAttributeValue()
        .addConstantDescription("my_value");
    var thirdBranch = builder.createNewBranch();
    thirdBranch.addOutgoingEdge("third_branch_edge")
        .addNodeDescription("third_branch_node");
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateRemovalGraphDescription() {
    var builder = new GraphDescriptionBuilder();
    builder.addRemovalNodeDescription("example_node")
        .addRemovalNodeDescription("another_example_node");
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanCreateRemovalGraphDescriptionWithAnyElements() {
    var builder = new GraphDescriptionBuilder();
    builder.addRemovalNodeDescription("example_node")
        .addRemovalEdgeDescription("example_edge")
        .addRemovalNodeDescription("another_example_node")
        .addUuidDescription();
    this.thenObjectApproved(builder.build());
  }

  @Test
  public void itCanFilterOnlyPositiveGraphDescriptions() {
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("positive_node")
        .addOutgoingEdge("positive_edge")
        .addNodeDescription("another_positive_node")
        .addRemovalNodeDescription("removal_node")
        .addRemovalEdgeDescription("removal_edge");
    var result = builder.getOnlyPositiveGraphDescriptions();
    this.thenObjectApproved(result);
  }

  @Test
  public void itCanFilterOnlyPositiveGraphDescriptionsWithUuidDefinitions() {
    var builder = new GraphDescriptionBuilder();
    var rootBranch = builder
        .addNodeDescription("positive_node")
        .addOutgoingEdge("positive_edge")
        .addNodeDescription("another_positive_node");
    rootBranch.addRemovalNodeDescription("removal_node")
        .addUuidDescription();
    rootBranch.addRemovalEdgeDescription("removal_edge")
        .addUuidDescription();
    var result = builder.getOnlyPositiveGraphDescriptions();
    this.thenObjectApproved(result);
  }
}