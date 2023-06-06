package ai.stapi.graphoperations.graphbuilder.specific.positive;

import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class EdgeBuilderWithSettableNodes implements EdgeBuilder {

  private final List<AttributeBuilder> attributes = new ArrayList<>();
  private NodeBuilder precedingNode;
  private NodeBuilder nodeFrom;
  private NodeBuilder nodeTo;
  private EdgeDirection edgeDirection;
  private UniqueIdentifier id;
  private String type;

  @Override
  public EdgeBuilder setId(UniqueIdentifier id) {
    this.id = id;
    return this;
  }

  @Override
  public EdgeBuilder setType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public AttributeBuilder addAttribute() {
    var attributeBuilder = new AttributeBuilder();
    attributes.add(attributeBuilder);
    return attributeBuilder;
  }

  @Override
  public AttributeBuilder getLastAttribute() {
    if (this.attributes.size() == 0) {
      throw GraphBuilderException.becauseThereAreNoAttributesOnElement();
    }
    return this.attributes.get(attributes.size() - 1);
  }

  @Override
  public boolean isComplete() {
    return this.id != null
        && this.type != null
        && this.edgeDirection != null
        && this.nodeTo != null
        && this.nodeFrom != null
        && this.nodeFrom.isComplete()
        && this.nodeTo.isComplete();
  }

  @Override
  public Edge build(GenericAttributeFactory attributeFactory) {
    if (this.nodeTo == null) {
      throw GraphBuilderException.becauseGraphCanNotHaveEdgeAsLastElement();
    }
    if (!this.isComplete()) {
      throw GraphBuilderException.becauseEdgeIsNotCompleted();
    }
    if (!this.nodeFrom.isComplete()) {
      throw GraphBuilderException.becauseNodeFromOnEdgeIsNotComplete();
    }
    if (!this.nodeTo.isComplete()) {
      throw GraphBuilderException.becauseNodeToOnEdgeIsNotComplete();
    }
    var nodeFrom = this.nodeFrom.build(attributeFactory);
    var nodeTo = this.nodeTo.build(attributeFactory);
    var edge = new Edge(this.id, nodeFrom, this.type, nodeTo);
    for (AttributeBuilder attributeBuilder : this.attributes) {
      Attribute<?> build = attributeBuilder.build(attributeFactory);
      edge = edge.add(build);
    }
    return edge;
  }

  @Override
  public Graph buildToGraph(GenericAttributeFactory attributeFactory) {
    Edge edge = this.build(attributeFactory);
    Node nodeFrom = this.nodeFrom.build(attributeFactory);
    Node nodeTo = this.nodeTo.build(attributeFactory);
    return new Graph(
        nodeFrom,
        nodeTo,
        edge
    );
  }

  public EdgeBuilderWithSettableNodes setPrecedingNode(NodeBuilder nodeBuilder) {
    this.precedingNode = nodeBuilder;
    return this;
  }

  public EdgeBuilderWithSettableNodes setNodeFrom(NodeBuilder nodeFrom) {
    this.nodeFrom = nodeFrom;
    return this;
  }

  public EdgeBuilderWithSettableNodes setNodeTo(NodeBuilder nodeTo) {
    this.nodeTo = nodeTo;
    return this;
  }

  @Override
  public EdgeBuilder setEdgeDirection(EdgeDirection edgeDirection) {
    if (precedingNode == null) {
      throw GraphBuilderException.becauseEdgeHasToBePrecededByNode();
    }
    this.edgeDirection = edgeDirection;
    if (edgeDirection.equals(EdgeDirection.INGOING)) {
      this.nodeTo = this.precedingNode;
    } else if (edgeDirection.equals(EdgeDirection.OUTGOING)) {
      this.nodeFrom = this.precedingNode;
    }
    return this;
  }

  @Override
  public EdgeDirection getEdgeDirection() {
    return edgeDirection;
  }
}
