package ai.stapi.graph.inMemoryGraph;

import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;

public class EdgeBuilder {

  private UniqueIdentifier edgeId = UniversallyUniqueIdentifier.randomUUID();
  private String edgeType;
  private UniqueIdentifier nodeFromId;
  private String nodeFromType;
  private UniqueIdentifier nodeToId;
  private String nodeToType;
  private VersionedAttributeGroup versionedAttributes;

  public static EdgeBuilder withAny() {
    return new EdgeBuilder()
        .setEdgeId(UniversallyUniqueIdentifier.randomUUID())
        .setEdgeType("any_edge_type")
        .setNodeFromId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeToId(UniversallyUniqueIdentifier.randomUUID())
        .setNodeFromType("any_node_from_type")
        .setNodeToType("any_node_to_type")
        .setVersionedAttributes(new ImmutableVersionedAttributeGroup());
  }

  public EdgeBuilder setNodeFromId(UniqueIdentifier nodeFromId) {
    this.nodeFromId = nodeFromId;
    return this;
  }

  public EdgeBuilder setEdgeType(String edgeType) {
    this.edgeType = edgeType;
    return this;
  }

  public EdgeBuilder setNodeToId(UniqueIdentifier nodeToId) {
    this.nodeToId = nodeToId;
    return this;
  }

  public EdgeBuilder setNodeFromType(String nodeFromType) {
    this.nodeFromType = nodeFromType;
    return this;
  }

  public EdgeBuilder setNodeToType(String nodeToType) {
    this.nodeToType = nodeToType;
    return this;
  }

  public EdgeBuilder setEdgeId(UniqueIdentifier edgeId) {
    this.edgeId = edgeId;
    return this;
  }

  public EdgeBuilder setVersionedAttributes(VersionedAttributeGroup versionedAttributes) {
    this.versionedAttributes = versionedAttributes;
    return this;
  }

  public Edge create() {
    return new Edge(
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