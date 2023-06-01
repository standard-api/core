package ai.stapi.graph.graphElementForRemoval;

import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.identity.UniqueIdentifier;

public class NodeForRemoval implements GraphElementForRemoval {

  public static final String ELEMENT_BASE_TYPE = "NODE";
  private final UniqueIdentifier nodeId;
  private final String nodeType;

  public NodeForRemoval(UniqueIdentifier nodeId, String nodeType) {
    this.nodeId = nodeId;
    this.nodeType = nodeType;
  }

  public NodeForRemoval(TraversableNode node) {
    this.nodeId = node.getId();
    this.nodeType = node.getType();
  }

  public NodeForRemoval(Node node) {
    this.nodeId = node.getId();
    this.nodeType = node.getType();
  }

  @Override
  public UniqueIdentifier getGraphElementId() {
    return this.nodeId;
  }

  @Override
  public String getGraphElementType() {
    return this.nodeType;
  }

  @Override
  public String getGraphElementBaseType() {
    return ELEMENT_BASE_TYPE;
  }
}
