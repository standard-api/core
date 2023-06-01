package ai.stapi.graph.graphelements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Objects;

public class Edge extends AbstractGraphElement {

  private final UniqueIdentifier nodeFromId;
  private final UniqueIdentifier nodeToId;
  private final String nodeFromType;
  private final String nodeToType;

  public Edge(
      UniqueIdentifier id,
      String type,
      VersionedAttributeGroup versionedAttributes,
      NodeIdAndType nodeFromIdAndType,
      NodeIdAndType nodeToIdAndType
  ) {
    super(id, type, versionedAttributes);
    this.nodeFromId = nodeFromIdAndType.getId();
    this.nodeToId = nodeToIdAndType.getId();
    this.nodeFromType = nodeFromIdAndType.getType();
    this.nodeToType = nodeToIdAndType.getType();
  }

  public Edge(
      UniqueIdentifier edgeId,
      Node nodeFrom,
      String edgeType,
      Node nodeTo
  ) {
    super(edgeId, edgeType);
    this.nodeFromId = nodeFrom.getId();
    this.nodeFromType = nodeFrom.getType();
    this.nodeToId = nodeTo.getId();
    this.nodeToType = nodeTo.getType();
  }

  public Edge(
      UniqueIdentifier edgeId,
      String edgeType,
      Node nodeFrom,
      Node nodeTo,
      VersionedAttributeGroup attributeGroup
  ) {
    super(edgeId, edgeType, attributeGroup);
    this.nodeFromId = nodeFrom.getId();
    this.nodeFromType = nodeFrom.getType();
    this.nodeToId = nodeTo.getId();
    this.nodeToType = nodeTo.getType();
  }

  public Edge(
      UniqueIdentifier edgeId,
      Node nodeFrom,
      String edgeType,
      Node nodeTo,
      Attribute<?>... attributes
  ) {
    super(edgeId, edgeType, attributes);
    this.nodeFromId = nodeFrom.getId();
    this.nodeFromType = nodeFrom.getType();
    this.nodeToId = nodeTo.getId();
    this.nodeToType = nodeTo.getType();
  }

  public Edge(TraversableEdge traversableEdge) {
    this(
        traversableEdge.getId(),
        traversableEdge.getType(),
        new Node(traversableEdge.getNodeFrom()),
        new Node(traversableEdge.getNodeTo()),
        traversableEdge.getVersionedAttributes()
    );
  }

  public Edge(Node nodeFrom, String edgeType, Node nodeTo) {
    this(
        UniversallyUniqueIdentifier.randomUUID(),
        nodeFrom,
        edgeType,
        nodeTo
    );
  }

  public Edge(
      Node nodeFrom,
      String edgeType,
      Node nodeTo,
      VersionedAttributeGroup attributeGroup
  ) {
    this(UniversallyUniqueIdentifier.randomUUID(), edgeType, nodeFrom, nodeTo,
        attributeGroup);
  }

  public Edge(
      Node nodeFrom,
      String edgeType,
      Node nodeTo,
      Attribute<?>... attributes
  ) {
    this(UniversallyUniqueIdentifier.randomUUID(), nodeFrom, edgeType, nodeTo,
        attributes);
  }

  public Edge(
      UniqueIdentifier edgeId,
      String edgeType,
      UniqueIdentifier nodeFromId,
      UniqueIdentifier nodeToId,
      String nodeFromType,
      String nodeToType,
      VersionedAttributeGroup versionedAttributes
  ) {
    super(edgeId, edgeType, versionedAttributes);
    this.nodeFromId = nodeFromId;
    this.nodeToId = nodeToId;
    this.nodeFromType = nodeFromType;
    this.nodeToType = nodeToType;
  }

  public UniqueIdentifier getNodeFromId() {
    return this.nodeFromId;
  }

  public UniqueIdentifier getNodeToId() {
    return this.nodeToId;
  }

  public String getNodeFromType() {
    return this.nodeFromType;
  }

  public String getNodeToType() {
    return this.nodeToType;
  }

  @Override
  protected AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes) {
    return new Edge(
        this.getId(),
        this.getType(),
        this.getNodeFromId(),
        this.getNodeToId(),
        this.getNodeFromType(),
        this.getNodeToType(),
        newAttributes
    );
  }

  @Override
  public Edge add(Attribute<?> attribute) {
    return (Edge) super.add(attribute);
  }

  @Override
  protected int getHashCodeWithoutAttributes() {
    return Objects.hash(this.getId()) + this.getIdlessHashCodeWithoutAttributes();
  }

  @Override
  protected int getIdlessHashCodeWithoutAttributes() {
    return Objects.hash(this.getType());
  }

  public NodeIdAndType getNodeFromIdAndType() {
    return new NodeIdAndType(this.nodeFromId, this.nodeFromType);
  }

  public NodeIdAndType getNodeToIdAndType() {
    return new NodeIdAndType(this.nodeToId, this.nodeToType);
  }

  public Edge getCopy() {
    return this;
  }

  public Edge mergeOverwrite(Edge otherEdge) {
    if (!this.getId().equals(otherEdge.getId())) {
      throw GraphEdgesCannotBeMerged.becauseTheyHaveDifferentIds();
    }
    if (!this.getType().equals(otherEdge.getType())) {
      throw GraphEdgesCannotBeMerged.becauseTheyHaveDifferentTypes();
    }
    if (!(this.getNodeFromId().equals(otherEdge.getNodeFromId()))
        || !(this.getNodeToId().equals(otherEdge.getNodeToId()))) {
      throw GraphEdgesCannotBeMerged.becauseTheyHaveDifferentNodeIds();
    }
    return (Edge) this.mergeAttributesWithAttributesOf(otherEdge);
  }
}
