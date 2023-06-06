package ai.stapi.graphoperations.graphbuilder;

import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graphoperations.graphbuilder.specific.positive.AttributeBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeBuilderWithSettableNodes;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import ai.stapi.graphoperations.graphbuilder.specific.positive.GraphElementBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.positive.NodeBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.removal.GraphElementForRemovalBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.removal.RemovalEdgeBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.removal.RemovalNodeBuilder;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

public class GraphBuilder {

  private final List<GraphElementForRemovalBuilder> graphElementsForRemoval = new ArrayList<>();
  private final List<GraphBuilder> childGraphBranches = new ArrayList<>();
  private GraphBuilder parentGraph;
  private List<GraphElementBuilder> graphElements = new ArrayList<>();

  public GraphBuilder(GraphBuilder parent) {
    this.parentGraph = parent;
  }

  public GraphBuilder() {
  }

  public NodeBuilder addNode() {
    var previousElement = this.getPreviousElementBuilder();
    if (previousElement instanceof NodeBuilder nodeBuilder) {
      throw GraphBuilderException.becauseThereCanNotBeTwoNodesAfterEachOther(nodeBuilder);
    }
    var nodeBuilder = new NodeBuilder();
    if (previousElement instanceof EdgeBuilderWithSettableNodes edgeBuilder) {
      if (edgeBuilder.getEdgeDirection().equals(EdgeDirection.OUTGOING)) {
        edgeBuilder.setNodeTo(nodeBuilder);
      } else if (edgeBuilder.getEdgeDirection().equals(EdgeDirection.INGOING)) {
        edgeBuilder.setNodeFrom(nodeBuilder);
      }
    }
    this.graphElements.add(nodeBuilder);
    return nodeBuilder;
  }

  public EdgeBuilder addEdge() {
    var previousElement = this.getPreviousElementBuilder();
    if (previousElement instanceof EdgeBuilder) {
      throw GraphBuilderException.becauseThereCanNotBeTwoEdgesAfterEachOther();
    }
    if (previousElement == null) {
      throw GraphBuilderException.becauseGraphCanNotHaveEdgeAsFirstElement();
    }
    var edgeBuilder = new EdgeBuilderWithSettableNodes();
    if (previousElement instanceof NodeBuilder nodeBuilder) {
      edgeBuilder.setPrecedingNode(nodeBuilder);
    } else {
      throw GraphBuilderException.becauseEdgeHasToBePrecededByNode();
    }
    this.graphElements.add(edgeBuilder);
    return edgeBuilder;
  }

  public RemovalNodeBuilder addNodeForRemoval() {
    var builder = new RemovalNodeBuilder();
    this.graphElementsForRemoval.add(builder);
    return builder;
  }

  public RemovalEdgeBuilder addEdgeForRemoval() {
    var builder = new RemovalEdgeBuilder();
    this.graphElementsForRemoval.add(builder);
    return builder;
  }

  public AttributeBuilder addAttributeToLastElement() {
    var element = this.getPreviousElementBuilder();
    if (element == null) {
      throw GraphBuilderException.becauseThereAreNoElementsAbleToContainAttribute();
    }
    return element.addAttribute();
  }

  public void setIdToLastElement(UniqueIdentifier id) {
    var element = this.getPreviousElementBuilder();
    if (element == null) {
      throw GraphBuilderException.becauseThereAreNoElements();
    }
    element.setId(id);
  }

  @Nullable
  public GraphElementBuilder getLastGraphElement() {
    if (this.graphElements.isEmpty()) {
      return null;
    }
    return this.graphElements.get(this.graphElements.size() - 1);
  }

  @Nullable
  public GraphElementBuilder getFirstGraphElement() {
    if (this.graphElements.isEmpty()) {
      return null;
    }
    return this.graphElements.get(0);
  }

  public GraphBuilder createNewBranch() {
    var childBuilder = new GraphBuilder(this);
    this.childGraphBranches.add(childBuilder);
    return childBuilder;
  }

  public void dropIncompleteEdges() {
    this.childGraphBranches.forEach(GraphBuilder::dropIncompleteEdges);
    this.graphElements = this.graphElements.stream().filter(elementBuilder -> {
      if (elementBuilder instanceof EdgeBuilder edgeBuilder) {
        return edgeBuilder.isComplete();
      } else {
        return true;
      }
    }).toList();
  }

  public Graph build(GenericAttributeFactory attributeFactory) {
    var childGraph =
        this.childGraphBranches.stream().map(builder -> builder.build(attributeFactory)).reduce(
            Graph::merge).orElse(new Graph());
    var rootGraph = this.graphElements.stream()
        .map(graphElementBuilder -> graphElementBuilder.buildToGraph(attributeFactory))
        .reduce(Graph::merge).orElse(new Graph());
    return rootGraph.merge(childGraph);
  }

  public List<GraphElementForRemoval> buildElementsForRemoval() {
    var localElements =
        this.graphElementsForRemoval.stream().map(GraphElementForRemovalBuilder::build)
            .collect(Collectors.toList());
    var childElements = this.childGraphBranches.stream().map(GraphBuilder::buildElementsForRemoval)
        .flatMap(List::stream).toList();
    localElements.addAll(childElements);
    return localElements;
  }

  @Nullable
  private GraphElementBuilder getPreviousElementBuilder() {
    var previousElementBuilder = this.getLastGraphElement();
    if (previousElementBuilder != null) {
      return previousElementBuilder;
    }
    var parent = this.getFirstParentGraphWithElements();
    if (parent == null) {
      return null;
    }
    return parent.getLastGraphElement();
  }

  @Nullable
  private GraphBuilder getFirstParentGraphWithElements() {
    var parent = this.parentGraph;
    while (parent != null) {
      if (!parent.graphElements.isEmpty()) {
        return parent;
      }
      parent = parent.parentGraph;
    }
    return null;
  }
}
