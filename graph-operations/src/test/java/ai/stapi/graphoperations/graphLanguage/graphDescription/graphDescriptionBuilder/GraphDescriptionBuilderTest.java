package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.exception.GraphDescriptionBuilderException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.*;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.UUID;

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

    @Test
    void itWillThrowIfAddingToDeepestDescriptionButItIsNotASinglePath() {
        var givenMainDescription = new NodeDescription(
            new NodeDescriptionParameters("Main"),
            new OutgoingEdgeDescription(new EdgeDescriptionParameters("edge1")),
            new OutgoingEdgeDescription(new EdgeDescriptionParameters("edge2"))
        );
        var givenChildDescriptions = List.<GraphDescription>of(
            new NodeDescription(new NodeDescriptionParameters("ChildNode1")),
            new NodeDescription(new NodeDescriptionParameters("ChildNode2")),
            new NodeDescription(new NodeDescriptionParameters("ChildNode3"))
        );
        Executable throwable = () -> new GraphDescriptionBuilder().addToDeepestDescription(
            givenMainDescription,
            givenChildDescriptions
        );
        this.thenExceptionMessageApproved(GraphDescriptionBuilderException.class, throwable);
    }

    @Test
    void itWillAddChildDescriptionsToTheEndOfCompositeSinglePathDescription() {
        var givenMainDescription = new NodeDescription(
            new NodeDescriptionParameters("Main"),
            new OutgoingEdgeDescription(
                new EdgeDescriptionParameters("edge"),
                new NodeDescription(
                    new NodeDescriptionParameters("Other"),
                    new OutgoingEdgeDescription(
                        new EdgeDescriptionParameters("deeperEdge")
                    )
                )
            )
        );
        var givenChildDescriptions = List.<GraphDescription>of(
            new NodeDescription(new NodeDescriptionParameters("ChildNode1")),
            new NodeDescription(new NodeDescriptionParameters("ChildNode2")),
            new NodeDescription(new NodeDescriptionParameters("ChildNode3"))
        );
        var actual = new GraphDescriptionBuilder().addToDeepestDescription(
            givenMainDescription,
            givenChildDescriptions
        );
        this.thenObjectApproved(actual);
    }
}