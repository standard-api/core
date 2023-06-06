package ai.stapi.graphoperations.graphReader;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.graphReader.ExampleImplementation.ExampleAmbiguousGraphDescription;
import ai.stapi.graphoperations.graphReader.ExampleImplementation.ExampleNotSupportedGraphDescription;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graphoperations.graphReader.readResults.EdgeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.NodeReadResult;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class GraphReaderTest extends IntegrationTestCase {

  @Autowired
  private GraphReader graphReader;

  @Test
  void itThrowsErrorWhenFirstGraphElementIsNotFound() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var exampleGraph = this.getSimpleExampleGraph(nodeId).traversable();
    Executable throwable = () -> this.graphReader.read(
        UniversallyUniqueIdentifier.fromString("3db53fc1-4e24-406c-8a88-fbfc71e1b43c"),
        new NodeDescription(new NodeDescriptionParameters("house")),
        exampleGraph
    );
    this.thenExceptionMessageApproved(GraphReaderException.class, throwable);
  }

  @Test
  void itThrowsWhenThereIsNoSupportingResolverForMappingParts() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var exampleGraph = this.getSimpleExampleGraph(nodeId).traversable();
    Executable throwable = () -> this.graphReader.read(
        nodeId,
        new NodeDescription(new NodeDescriptionParameters("house"),
            new ExampleNotSupportedGraphDescription()),
        exampleGraph
    );
    this.thenExceptionMessageApproved(GraphReaderException.class, throwable);
  }

  @Test
  void itThrowsWhenThereIsMultipleSupportingResolversForMappingParts() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var exampleGraph = this.getSimpleExampleGraph(nodeId).traversable();
    Executable throwable = () -> this.graphReader.read(
        nodeId,
        new NodeDescription(new NodeDescriptionParameters("house"),
            new ExampleAmbiguousGraphDescription()),
        exampleGraph
    );
    this.thenExceptionMessageApproved(GraphReaderException.class, throwable);
  }

  @Test
  void itThrowsErrorWhenThereAreIncompatibleInstructionsAfterEachOther() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addNodeDescription("window");
    var exampleGraph = this.getSimpleExampleGraph(nodeId).traversable();
    Executable throwable = () -> this.graphReader.read(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        exampleGraph
    );
    this.thenExceptionMessageApproved(GraphDescriptionReadResolverException.class, throwable);
  }

  @Test
  void itCanReadNode() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addOutgoingEdge("has_doors")
        .addNodeDescription("doors")
        .addOutgoingEdge("has_doors_handle")
        .addNodeDescription("doors_handle");
    var graph = this.getSimpleExampleGraph(nodeId).traversable();
    var result = this.graphReader.read(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenApproveReadResult(result);
  }

  @Test
  void itCanReadNodes() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("country")
        .addOutgoingEdge("has_region")
        .addNodeDescription("region")
        .addOutgoingEdge("has_city")
        .addNodeDescription("city");
    var graph = this.getComplexExampleGraph(nodeId).traversable();
    var result = this.graphReader.read(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenApproveReadResult(result);
  }

  @Test
  void itCanReadNodeAttribute() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addLeafAttribute("material")
        .addStringAttributeValue();
    var graph = this.getSimpleExampleGraph(nodeId).traversable();
    var result = this.graphReader.<String>readValues(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenObjectApproved(result);
  }

  @Test
  void itCanReadNodesAttributes() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("country")
        .addOutgoingEdge("has_region")
        .addNodeDescription("region")
        .addLeafAttribute("name")
        .addStringAttributeValue();
    var graph = this.getComplexExampleGraph(nodeId).traversable();
    var result = this.graphReader.readValues(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenObjectApproved(result.stream().sorted().toList());
  }

  @Test
  void itWillReturnEmptyListWhenAttributeNotPresent() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addLeafAttribute("nonExistent")
        .addStringAttributeValue();
    var graph = this.getSimpleExampleGraph(nodeId).traversable();
    var result = this.graphReader.<String>readValues(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenObjectApproved(result);
  }
  
  @Test
  void itThrowsErrorWhenThereIsMultipleBranches() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    var nodeBranch = builder.addNodeDescription("country")
        .addOutgoingEdge("has_region")
        .addNodeDescription("region");
    nodeBranch.addOutgoingEdge("random_edge")
        .addLeafAttribute("name")
        .addStringAttributeValue();
    nodeBranch
        .addLeafAttribute("random_attribute")
        .addStringAttributeValue();
    var graph = this.getComplexExampleGraph(nodeId).traversable();
    Executable throwable = () -> this.graphReader.readValues(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenExceptionMessageApproved(GraphReaderException.class, throwable);
  }

  @Test
  void itCanReadEdge() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addOutgoingEdge("has_window");
    var graph = this.getSimpleExampleGraph(nodeId).traversable();
    var result = this.graphReader.read(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenApproveReadResult(result);
  }

  @Test
  void itCanReadEdges() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("country")
        .addOutgoingEdge("has_region")
        .addNodeDescription("region")
        .addOutgoingEdge("has_city");
    var graph = this.getComplexExampleGraph(nodeId).traversable();
    var result = this.graphReader.read(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenApproveReadResult(result);
  }

  @Test
  void itCanReadEdgeAttribute() {
    var nodeId = UniversallyUniqueIdentifier.randomUUID();
    var graph = this.getSimpleExampleGraph(nodeId).traversable();
    var builder = new GraphDescriptionBuilder();
    builder.addNodeDescription("house")
        .addOutgoingEdge("has_doors")
        .addLeafAttribute("position")
        .addStringAttributeValue();
    var result = this.graphReader.<String>readValues(
        nodeId,
        builder.getOnlyPositiveGraphDescriptions(),
        graph
    );
    this.thenObjectApproved(result);
  }

  private Graph getSimpleExampleGraph(UniqueIdentifier startNodeId) {
    var graph = new Graph();
    var houseNode = new Node(startNodeId, "house");
    houseNode =
        houseNode.add(new LeafAttribute<>("material", new StringAttributeValue("bricks")));
    var windowNode = new Node("window");
    windowNode =
        windowNode.add(new LeafAttribute<>("material", new StringAttributeValue("glass")));
    var doorsNode = new Node("doors");
    doorsNode =
        doorsNode.add(new LeafAttribute<>("material", new StringAttributeValue("wood")));
    var doorsHandleNode = new Node("doors_handle");
    doorsHandleNode = doorsHandleNode.add(
        new LeafAttribute<>("material", new StringAttributeValue("metal")));
    var doorEdge = new Edge(houseNode, "has_doors", doorsNode);
    doorEdge = doorEdge.add(new LeafAttribute<>("position", new StringAttributeValue("front")));
    graph = graph.withAll(houseNode, windowNode, doorsNode, doorsHandleNode);
    graph = graph.withAll(
        doorEdge,
        new Edge(houseNode, "has_window", windowNode),
        new Edge(doorsNode, "has_doors_handle", doorsHandleNode)
    );
    return graph;
  }

  private Graph getComplexExampleGraph(UniqueIdentifier startNodeId) {
    var graph = new Graph();
    var countryNode = new Node(startNodeId, "country");
    countryNode =
        countryNode.add(new LeafAttribute<>("name", new StringAttributeValue("Czechia")));
    var cityNode = new Node("city");
    cityNode = cityNode.add(new LeafAttribute<>("name", new StringAttributeValue("Prague")));
    var cityNode1 = new Node("city");
    cityNode1 = cityNode1.add(new LeafAttribute<>("name", new StringAttributeValue("Brno")));
    var cityNode2 = new Node("city");
    cityNode2 = cityNode2.add(new LeafAttribute<>("name", new StringAttributeValue("Plzen")));
    var regionNode = new Node("region");
    regionNode =
        regionNode.add(new LeafAttribute<>("name", new StringAttributeValue("Cechy")));
    var regionNode1 = new Node("region");
    regionNode1 =
        regionNode1.add(new LeafAttribute<>("name", new StringAttributeValue("Morava")));
    graph = graph.withAll(countryNode, regionNode, regionNode1, cityNode, cityNode1, cityNode2);
    graph = graph.withAll(
        new Edge(countryNode, "has_region", regionNode),
        new Edge(countryNode, "has_region", regionNode1),
        new Edge(regionNode, "has_city", cityNode),
        new Edge(regionNode1, "has_city", cityNode1),
        new Edge(regionNode, "has_city", cityNode2)
    );
    return graph;
  }

  protected void thenApproveReadResult(List<ReadResult> result) {

    if (result.get(0) instanceof NodeReadResult) {
      var list = result.stream()
          .map(readResult -> {
            var nodeResult = (NodeReadResult) readResult;
            return nodeResult.getNode();
          })
          .toList();
      this.thenNodesApproved(list);
    }

    if (result.get(0) instanceof EdgeReadResult) {
      var list = result.stream()
          .map(readResult -> {
            var edgeResult = (EdgeReadResult) readResult;
            return edgeResult.getEdge();
          })
          .toList();
      this.thenEdgesApproved(list);
    }
  }
}