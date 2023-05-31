package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;

public class InputEdgeBuilder {

  private UniqueIdentifier edgeId = UniversallyUniqueIdentifier.randomUUID();
  private String edgeType;
  private UniqueIdentifier nodeFromId;
  private String nodeFromType;
  private UniqueIdentifier nodeToId;
  private String nodeToType;
  private VersionedAttributeGroup versionedAttributes;

  public static InputEdgeBuilder withAny() {
    return new InputEdgeBuilder()
        .setEdgeId(UniversallyUniqueIdentifier.randomUUID())
        .setEdgeType("any_edge_type")
        .setNodeFromId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeToId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeFromType("any_node_from_type")
        .setNodeToType("any_node_to_type")
        .setVersionedAttributes(new ImmutableVersionedAttributeGroup());
  }

  public InputEdgeBuilder setNodeFromId(UniqueIdentifier nodeFromId) {
    this.nodeFromId = nodeFromId;
    return this;
  }

  public InputEdgeBuilder setEdgeType(String edgeType) {
    this.edgeType = edgeType;
    return this;
  }

  public InputEdgeBuilder setNodeToId(UniqueIdentifier nodeToId) {
    this.nodeToId = nodeToId;
    return this;
  }

  public InputEdgeBuilder setNodeFromType(String nodeFromType) {
    this.nodeFromType = nodeFromType;
    return this;
  }

  public InputEdgeBuilder setNodeToType(String nodeToType) {
    this.nodeToType = nodeToType;
    return this;
  }

  public InputEdgeBuilder setEdgeId(UniqueIdentifier edgeId) {
    this.edgeId = edgeId;
    return this;
  }

  public InputEdgeBuilder setVersionedAttributes(VersionedAttributeGroup versionedAttributes) {
    this.versionedAttributes = versionedAttributes;
    return this;
  }

  public InputEdge createInputEdge() {
    return new InputEdge(
        edgeId,
        edgeType,
        nodeFromId,
        nodeToId,
        nodeFromType,
        nodeToType,
        this.versionedAttributes
    );
  }
}