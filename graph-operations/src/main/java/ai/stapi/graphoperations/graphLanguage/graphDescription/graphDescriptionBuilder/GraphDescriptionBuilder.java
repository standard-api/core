package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.exception.GraphDescriptionBuilderException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.AbstractAttributeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.AbstractAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.AbstractPositiveDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.Base64BinaryAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.BooleanAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.CanonicalAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.CodeAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.ConstantSpecificDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.DateAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.DateTimeAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.DecimalAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.EdgeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.IdAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.InstantAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.IntegerAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.InterfaceDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.LeafAttributeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.ListAttributeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.MarkdownAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.NodeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.NullSpecificDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.OidAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.PositiveIntegerAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.ReferenceDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.SetAttributeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.StringAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.TimeAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.UnsignedIntegerAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.UriAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.UrlAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.UuidAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.UuidSpecificDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive.XhtmlAttributeValueDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.query.AttributeQueryDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal.AbstractRemovalDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal.RemovalEdgeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.removal.RemovalNodeDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import ai.stapi.utils.Classifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphDescriptionBuilder {

    private final List<SpecificGraphDescriptionBuilder> immutableSupportingSpecificBuilders;
    private SpecificGraphDescriptionBuilder descriptionBuilder;
    private List<GraphDescriptionBuilder> childBranches = new ArrayList<>();
    private GraphDescriptionBuilder parent;

    public GraphDescriptionBuilder() {
        this.immutableSupportingSpecificBuilders = List.of(
            new UuidSpecificDescriptionBuilder(),
            new NodeDescriptionBuilder(),
            new EdgeDescriptionBuilder(),
            new StringAttributeValueDescriptionBuilder(),
            new IntegerAttributeValueDescriptionBuilder(),
            new BooleanAttributeValueDescriptionBuilder(),
            new InstantAttributeValueDescriptionBuilder(),
            new TimeAttributeValueDescriptionBuilder(),
            new DateAttributeValueDescriptionBuilder(),
            new DateTimeAttributeValueDescriptionBuilder(),
            new Base64BinaryAttributeValueDescriptionBuilder(),
            new UrlAttributeValueDescriptionBuilder(),
            new CodeAttributeValueDescriptionBuilder(),
            new UriAttributeValueDescriptionBuilder(),
            new CanonicalAttributeValueDescriptionBuilder(),
            new MarkdownAttributeValueDescriptionBuilder(),
            new IdAttributeValueDescriptionBuilder(),
            new OidAttributeValueDescriptionBuilder(),
            new UuidAttributeValueDescriptionBuilder(),
            new UnsignedIntegerAttributeValueDescriptionBuilder(),
            new PositiveIntegerAttributeValueDescriptionBuilder(),
            new ConstantSpecificDescriptionBuilder(),
            new NullSpecificDescriptionBuilder(),
            new RemovalNodeDescriptionBuilder(),
            new RemovalEdgeDescriptionBuilder(),
            new InterfaceDescriptionBuilder(),
            new DecimalAttributeValueDescriptionBuilder(),
            new XhtmlAttributeValueDescriptionBuilder(),
            new ReferenceDescriptionBuilder(),
            new LeafAttributeDescriptionBuilder(),
            new ListAttributeDescriptionBuilder(),
            new SetAttributeDescriptionBuilder(),
            new AttributeQueryDescriptionBuilder()
        );
    }

    private GraphDescriptionBuilder(GraphDescriptionBuilder parent) {
        this();
        this.parent = parent;
    }

    public static Stream<GraphDescription> getGraphDescriptionAsStream(
        GraphDescription graphDescription
    ) {
        var allGraphDescriptions = new ArrayList<GraphDescription>();
        GraphDescriptionBuilder.addAllGraphDescriptionsInComposite(
            graphDescription,
            allGraphDescriptions
        );
        return allGraphDescriptions.stream();
    }

    public static boolean isGraphDescriptionSinglePath(GraphDescription graphDescription) {
        var childGraphDescriptions = graphDescription.getChildGraphDescriptions();
        if (childGraphDescriptions.size() > 1) {
            return false;
        }
        if (childGraphDescriptions.size() == 1) {
            return GraphDescriptionBuilder.isGraphDescriptionSinglePath(childGraphDescriptions.get(0));
        }
        return true;
    }

    public static boolean isGraphDescriptionEndingWithAttributeDescription(
        GraphDescription graphDescription
    ) {
        var children = graphDescription.getChildGraphDescriptions();
        if (children.isEmpty() && graphDescription instanceof AbstractAttributeDescription) {
            return true;
        }
        if (children.isEmpty()) {
            return false;
        }
        return children.stream().allMatch(GraphDescriptionBuilder::isGraphDescriptionEndingWithAttributeDescription);
    }

    public static boolean isGraphDescriptionEndingWithAttributeOrUuidDescription(
        GraphDescription graphDescription
    ) {
        var children = graphDescription.getChildGraphDescriptions();
        if (children.isEmpty() && isInstanceOfUuidIdentityOrAttributeDescription(graphDescription)) {
            return true;
        }
        if (children.isEmpty()) {
            return false;
        }
        return children.stream().allMatch(
            GraphDescriptionBuilder::isGraphDescriptionEndingWithAttributeOrUuidDescription
        );
    }

    private static void addAllGraphDescriptionsInComposite(
        GraphDescription graphDescription,
        List<GraphDescription> listOfGraphDescriptions
    ) {
        listOfGraphDescriptions.add(graphDescription);
        graphDescription.getChildGraphDescriptions()
            .forEach(
                child -> GraphDescriptionBuilder.addAllGraphDescriptionsInComposite(
                    child,
                    listOfGraphDescriptions
                )
            );
    }

    public GraphDescription copyWithNewChildren(
        GraphDescription graphDescription,
        List<GraphDescription> newChildren
    ) {
        var supportingBuilder = this.getRepresentingBuilder(graphDescription);
        return supportingBuilder.copyWithNewChildren(graphDescription, newChildren);
    }

    public GraphDescription addToDeepestDescription(
        GraphDescription mainDescription,
        List<GraphDescription> childDescriptions
    ) {
        if (!GraphDescriptionBuilder.isGraphDescriptionSinglePath(mainDescription)) {
            throw GraphDescriptionBuilderException.becauseToAddToDeepestGraphDescriptionItMustBeSinglePath(
                mainDescription
            );
        }
        return this.privateAddToDeepestDescription(mainDescription, childDescriptions);
    }

    private GraphDescription privateAddToDeepestDescription(
        GraphDescription mainDescription,
        List<GraphDescription> childDescriptions
    ) {
        var supportingBuilder = this.getRepresentingBuilder(mainDescription);
        if (mainDescription.getChildGraphDescriptions().isEmpty()) {
            return supportingBuilder.copyWithNewChildren(mainDescription, childDescriptions);
        }

        return supportingBuilder.copyWithNewChildren(
            mainDescription,
            List.of(
                this.privateAddToDeepestDescription(
                    mainDescription.getChildGraphDescriptions().get(0),
                    childDescriptions
                )
            )
        );
    }


    public GraphDescription copyWithNewChildren(
        GraphDescription graphDescription,
        GraphDescription... newChildren
    ) {
        var supportingBuilder = this.getRepresentingBuilder(graphDescription);
        return supportingBuilder.copyWithNewChildren(graphDescription,
            Arrays.stream(newChildren).collect(Collectors.toCollection(ArrayList::new)));
    }

    public GraphDescriptionBuilder addNodeDescription(String nodeType) {
        var builder = new NodeDescriptionBuilder().setNodeType(nodeType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addOutgoingEdge(String edgeType) {
        var builder = new EdgeDescriptionBuilder()
            .setDirection(EdgeDirection.OUTGOING)
            .setEdgeType(edgeType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addIngoingEdge(String edgeType) {
        var builder = new EdgeDescriptionBuilder()
            .setDirection(EdgeDirection.INGOING)
            .setEdgeType(edgeType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addAttributeByType(
        String attributeStructureType,
        String attributeName
    ) {
        var builder = this.immutableSupportingSpecificBuilders.stream()
            .filter(AbstractAttributeDescriptionBuilder.class::isInstance)
            .map(AbstractAttributeDescriptionBuilder.class::cast)
            .filter(bldr -> bldr.getSupportedStructureTypeId().equals(attributeStructureType))
            .findAny()
            .orElseThrow(() -> new RuntimeException(
                "No supporting builder for attribute structure type '" + attributeStructureType + "'."))
            .getCopy()
            .setAttributeName(attributeName);

        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addAttributeValueByType(
        String attributeDataType
    ) {
        var builder = this.immutableSupportingSpecificBuilders.stream()
            .filter(AbstractAttributeValueDescriptionBuilder.class::isInstance)
            .map(AbstractAttributeValueDescriptionBuilder.class::cast)
            .filter(bldr -> bldr.getSupportedDataTypeId().equals(attributeDataType))
            .findAny()
            .orElseThrow(() -> new RuntimeException(
                "No supporting builder for attribute value data type '" + attributeDataType + "'."))
            .getCopy();

        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addQueryAttribute(String attributeName) {
        var builder = new AttributeQueryDescriptionBuilder();
        builder.setAttributeName(attributeName);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addLeafAttribute(String attributeName) {
        var builder = new LeafAttributeDescriptionBuilder();
        builder.setAttributeName(attributeName);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addListAttribute(String attributeName) {
        var builder = new ListAttributeDescriptionBuilder();
        builder.setAttributeName(attributeName);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addSetAttribute(String attributeName) {
        var builder = new SetAttributeDescriptionBuilder();
        builder.setAttributeName(attributeName);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addStringAttributeValue() {
        var builder = new StringAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder setParent(GraphDescriptionBuilder parent) {
        this.parent = parent;
        return this;
    }

    public GraphDescriptionBuilder addUriAttributeValue() {
        var builder = new UriAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addMarkdownAttributeValue() {
        var builder = new MarkdownAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addCodeAttributeValue() {
        var builder = new CodeAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addDecimalAttributeValue() {
        var builder = new DecimalAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addBooleanAttributeValue() {
        var builder = new BooleanAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addIntegerAttributeValue() {
        var builder = new IntegerAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addUnsignedIntegerAttributeValue() {
        var builder = new UnsignedIntegerAttributeValueDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addInterfaceDescription(String interfaceId) {
        var builder = new InterfaceDescriptionBuilder()
            .setInterfaceId(interfaceId);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addUuidDescription() {
        var builder = new UuidSpecificDescriptionBuilder();
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addConstantDescription(Object value) {
        if (!Classifier.isPrimitiveOrString(value) && !(value instanceof List)) {
            throw GraphDescriptionBuilderException.becauseProvidedValueIsNotPrimitiveType(value);
        }

        var builder = new ConstantSpecificDescriptionBuilder()
            .setValue(value);
        this.addGraphDescriptionBuilder(builder);
        return this;
    }

    public GraphDescriptionBuilder addRemovalNodeDescription(String nodeType) {
        var builder = new RemovalNodeDescriptionBuilder()
            .setNodeType(nodeType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addRemovalEdgeDescription(String edgeType) {
        var builder = new RemovalEdgeDescriptionBuilder()
            .setEdgeType(edgeType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder addReferenceDescription(String structureSerializationType) {
        var builder = new ReferenceDescriptionBuilder()
            .setStructureSerializationType(structureSerializationType);
        return this.addGraphDescriptionBuilder(builder);
    }

    public GraphDescriptionBuilder createNewBranch() {
        var newBranch = new GraphDescriptionBuilder(this);
        this.childBranches.add(newBranch);
        return newBranch;
    }

    public GraphDescriptionBuilder addNewBranch(GraphDescriptionBuilder child) {
        child.setParent(this);
        this.childBranches.add(child);
        return child;
    }

    public GraphDescriptionBuilder addBuilderCopyOfGraphDescriptionWithNoChildrenToBuilder(
        GraphDescription graphDescription
    ) {
        var builder = this.getRepresentingBuilder(graphDescription);
        builder.setValues(graphDescription);
        return this.addGraphDescriptionBuilder(builder);
    }

    public SpecificGraphDescriptionBuilder convertToGraphDescriptionBuilderComposite(
        GraphDescription graphDescription
    ) {
        var builder = this.getRepresentingBuilder(graphDescription);
        builder.setValues(graphDescription);
        if (graphDescription.getChildGraphDescriptions().size() > 0) {
            var children = graphDescription.getChildGraphDescriptions().stream()
                .map(this::convertToGraphDescriptionBuilderComposite)
                .collect(Collectors.toCollection(ArrayList::new));
            builder.setChildren(children);
        }
        return builder;
    }

    public PositiveGraphDescription getOnlyPositiveGraphDescriptions() {
        var builderCopy = this.copyBuilder();
        this.removeSpecificBuilderChildReferencesToRemovalBuilders(builderCopy);
        return (PositiveGraphDescription) builderCopy.build();
    }

    public List<RemovalGraphDescription> getOnlyRemovalGraphDescriptions() {
        List<AbstractRemovalDescriptionBuilder> removalBuilders = new ArrayList<>();
        this.getAllRemovalDescriptions(this, removalBuilders);
        return removalBuilders.stream()
            .map(AbstractRemovalDescriptionBuilder::build)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public GraphDescriptionBuilder copyBuilder() {
        return this.copyBuilder(new GraphDescriptionBuilder());
    }

    public GraphDescription build() {
        var currentBuilder = this;
        while (currentBuilder.getParent() != null) {
            currentBuilder = currentBuilder.getParent();
        }
        if (currentBuilder.descriptionBuilder == null) {
            throw GraphDescriptionBuilderException.becauseThereAreNoBuilders();
        }
        return currentBuilder.descriptionBuilder.build();
    }

    public SpecificGraphDescriptionBuilder getLastDescriptionBuilder() {
        if (this.descriptionBuilder != null) {
            return this.descriptionBuilder;
        }
        if (this.parent == null) {
            return null;
        }
        return this.parent.getLastDescriptionBuilder();
    }

    public SpecificGraphDescriptionBuilder getLastDescriptionBuilderOfType(
        Class<? extends SpecificGraphDescriptionBuilder> builderType
    ) {
        if (this.descriptionBuilder != null
            && this.descriptionBuilder.getClass().equals(builderType)) {
            return this.descriptionBuilder;
        }
        if (this.parent == null) {
            return null;
        }
        return this.parent.getLastDescriptionBuilderOfType(builderType);
    }

    public GraphDescriptionBuilder getLastBranchWithGraphElementBuilder() {
        var current = this;
        while (!(current.getDescriptionBuilder() instanceof NodeDescriptionBuilder)
            && !(current.getDescriptionBuilder() instanceof EdgeDescriptionBuilder)
            && !(current.getDescriptionBuilder() instanceof RemovalEdgeDescriptionBuilder)
            && !(current.getDescriptionBuilder() instanceof RemovalNodeDescriptionBuilder)
        ) {
            current = current.getParent();
            if (current == null) {
                throw GraphDescriptionBuilderException.becauseThereAreNoGraphElementsBuilders();
            }
        }
        return current;
    }

    public GraphDescriptionBuilder getLastBranchWithGraphBuilderOfType(
        Class<? extends SpecificGraphDescriptionBuilder> builderType) {
        if (this.descriptionBuilder.getClass().equals(builderType)) {
            return this;
        }
        if (parent == null) {
            throw GraphDescriptionBuilderException.becauseThereAreNoGraphBuildersWithGivenType(
                builderType);
        }
        return this.parent.getLastBranchWithGraphBuilderOfType(builderType);
    }

    private void removeSpecificBuilderChildReferencesToRemovalBuilders(
        GraphDescriptionBuilder builder) {
        var localSpecificBuilder = builder.getDescriptionBuilder();
        var positiveChildrenList = localSpecificBuilder.getChildren().stream()
            .filter(child -> !(child instanceof AbstractRemovalDescriptionBuilder))
            .collect(Collectors.toCollection(ArrayList::new));
        localSpecificBuilder.setChildren(positiveChildrenList);
        if (localSpecificBuilder instanceof AbstractRemovalDescriptionBuilder) {
            if (builder.getParent() == null) {
                throw GraphDescriptionBuilderException.becauseFirstGraphDescriptionIsForRemoval(
                    localSpecificBuilder);
            }
            builder.getChildBranches()
                .forEach(this::removeSpecificBuilderChildReferencesToRemovalBuilders);
            return;
        }
        if (builder.getParent() != null) {
            var parentSpecificBuilder = builder.getParent().getDescriptionBuilder();
            if (parentSpecificBuilder instanceof AbstractRemovalDescriptionBuilder
                && localSpecificBuilder instanceof AbstractPositiveDescriptionBuilder
                && !(localSpecificBuilder instanceof UuidSpecificDescriptionBuilder)) {
                builder.getLastBranchWithGraphBuilderOfType(AbstractPositiveDescriptionBuilder.class)
                    .getDescriptionBuilder().addChild(localSpecificBuilder);
            }
        }
        builder.getChildBranches().forEach(this::removeSpecificBuilderChildReferencesToRemovalBuilders);
    }

    private GraphDescriptionBuilder copyBuilder(GraphDescriptionBuilder parentBuilder) {
        var graphDescription = this.descriptionBuilder.build();
        var newParent =
            parentBuilder.addBuilderCopyOfGraphDescriptionWithNoChildrenToBuilder(graphDescription);
        this.childBranches.forEach(
            child -> child.copyBuilder(newParent)
        );
        return parentBuilder;
    }

    private SpecificGraphDescriptionBuilder getRepresentingBuilder(
        GraphDescription graphDescription) {
        var representingBuilder = this.immutableSupportingSpecificBuilders.stream()
            .filter(builder -> builder.represents(graphDescription))
            .collect(Collectors.toCollection(ArrayList::new));
        switch (representingBuilder.size()) {
            case 0 -> throw GraphDescriptionBuilderException.becauseDescriptionTypeIsNotSupported(
                graphDescription);
            case 1 -> {
                return representingBuilder.get(0).getCopy();
            }
            default -> throw GraphDescriptionBuilderException.becauseDescriptionTypeIsSupportedByMultipleBuilders(
                graphDescription);
        }
    }

    private void getAllRemovalDescriptions(GraphDescriptionBuilder builder,
                                           List<AbstractRemovalDescriptionBuilder> builders) {
        if (builder.descriptionBuilder instanceof AbstractRemovalDescriptionBuilder removalDescriptionBuilder) {
            builders.add(removalDescriptionBuilder);
        }
        builder.getChildBranches().forEach(
            child -> this.getAllRemovalDescriptions(child, builders)
        );
    }

    private GraphDescriptionBuilder addGraphDescriptionBuilder(
        SpecificGraphDescriptionBuilder builder
    ) {
        var lastBuilder = this.getLastDescriptionBuilder();
        if (lastBuilder != null) {
            lastBuilder.addChild(builder);
        }
        if (this.descriptionBuilder == null) {
            this.descriptionBuilder = builder;
            return this;
        } else {
            var newBranch = this.createNewBranch();
            newBranch.descriptionBuilder = builder;
            return newBranch;
        }
    }

    public List<GraphDescriptionBuilder> getChildBranches() {
        return childBranches;
    }

    public GraphDescriptionBuilder getParent() {
        return parent;
    }

    public SpecificGraphDescriptionBuilder getDescriptionBuilder() {
        return descriptionBuilder;
    }

    private static boolean isInstanceOfUuidIdentityOrAttributeDescription(GraphDescription graphDescription) {
        if (graphDescription instanceof AbstractAttributeDescription) {
            return true;
        }
        return graphDescription instanceof UuidIdentityDescription;
    }
}
