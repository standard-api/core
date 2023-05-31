package ai.stapi.graph.inputGraphElements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphEdgesCannotBeMerged;
import ai.stapi.graph.traversableGraphElements.TraversableEdge;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Objects;

public class InputEdge extends AbstractGraphElement {

  private final UniqueIdentifier nodeFromId;
  private final UniqueIdentifier nodeToId;
  private final String nodeFromType;
  private final String nodeToType;

  public InputEdge(
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

  public InputEdge(
      UniqueIdentifier edgeId,
      InputNode inputNodeFrom,
      String edgeType,
      InputNode inputNodeTo
  ) {
    super(edgeId, edgeType);
    this.nodeFromId = inputNodeFrom.getId();
    this.nodeFromType = inputNodeFrom.getType();
    this.nodeToId = inputNodeTo.getId();
    this.nodeToType = inputNodeTo.getType();
  }

  public InputEdge(
      UniqueIdentifier edgeId,
      String edgeType,
      InputNode inputNodeFrom,
      InputNode inputNodeTo,
      VersionedAttributeGroup attributeGroup
  ) {
    super(edgeId, edgeType, attributeGroup);
    this.nodeFromId = inputNodeFrom.getId();
    this.nodeFromType = inputNodeFrom.getType();
    this.nodeToId = inputNodeTo.getId();
    this.nodeToType = inputNodeTo.getType();
  }

  public InputEdge(
      UniqueIdentifier edgeId,
      InputNode inputNodeFrom,
      String edgeType,
      InputNode inputNodeTo,
      Attribute<?>... attributes
  ) {
    super(edgeId, edgeType, attributes);
    this.nodeFromId = inputNodeFrom.getId();
    this.nodeFromType = inputNodeFrom.getType();
    this.nodeToId = inputNodeTo.getId();
    this.nodeToType = inputNodeTo.getType();
  }

  public InputEdge(TraversableEdge traversableEdge) {
    this(
        traversableEdge.getId(),
        traversableEdge.getType(),
        new InputNode(traversableEdge.getNodeFrom()),
        new InputNode(traversableEdge.getNodeTo()),
        traversableEdge.getVersionedAttributes()
    );
  }

  public InputEdge(InputNode inputNodeFrom, String edgeType, InputNode inputNodeTo) {
    this(
        UniversallyUniqueIdentifier.randomUUID(),
        inputNodeFrom,
        edgeType,
        inputNodeTo
    );
  }

  public InputEdge(
      InputNode inputNodeFrom,
      String edgeType,
      InputNode inputNodeTo,
      VersionedAttributeGroup attributeGroup
  ) {
    this(UniversallyUniqueIdentifier.randomUUID(), edgeType, inputNodeFrom, inputNodeTo,
        attributeGroup);
  }

  public InputEdge(
      InputNode inputNodeFrom,
      String edgeType,
      InputNode inputNodeTo,
      Attribute<?>... attributes
  ) {
    this(UniversallyUniqueIdentifier.randomUUID(), inputNodeFrom, edgeType, inputNodeTo,
        attributes);
  }

  public InputEdge(
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
    return new InputEdge(
        this.getId(),
        this.getType(),
        this.getNodeFromId(),
        this.getNodeToId(),
        this.getNodeFromType(),
        this.getNodeToType(),
        newAttributes
    );
  }

  public <T extends Attribute<?>> InputEdge addToEdge(T attribute) {
    return (InputEdge) this.add(attribute);
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

  public InputEdge getCopy() {
    return this;
  }

  public InputEdge mergeOverwrite(InputEdge otherEdge) {
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
    return (InputEdge) this.mergeAttributesWithAttributesOf(otherEdge);
  }
}
