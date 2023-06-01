package ai.stapi.graph.traversableGraphElements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.NodeLoader;
import ai.stapi.graph.NodeRepository;
import ai.stapi.graph.NullNodeLoader;
import ai.stapi.graph.RepositoryNodeLoader;
import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Objects;

public class TraversableEdge extends AbstractAttributeContainer implements TraversableGraphElement,
    Comparable<TraversableEdge> {

  private final UniqueIdentifier edgeId;
  private final String edgeType;
  private final UniqueIdentifier nodeFromId;
  private final UniqueIdentifier nodeToId;
  private final String nodeFromType;
  private final String nodeToType;
  private final NodeLoader nodeLoader;

  public static TraversableEdge fromInput(
      InputEdge inputEdge,
      NodeRepository nodeRepository
  ) {
    return new TraversableEdge(
        inputEdge.getId(),
        inputEdge.getType(),
        inputEdge.getNodeFromId(),
        inputEdge.getNodeFromType(),
        inputEdge.getNodeToId(),
        inputEdge.getNodeToType(),
        inputEdge.getVersionedAttributes(),
        new RepositoryNodeLoader(nodeRepository)
    );
  }

  public TraversableEdge(InputNode inputNodeFrom, String edgeType, InputNode inputNodeTo) {
    this.edgeId = UniversallyUniqueIdentifier.randomUUID();
    this.edgeType = edgeType;
    this.nodeFromId = inputNodeFrom.getId();
    this.nodeFromType = inputNodeFrom.getType();
    this.nodeToId = inputNodeTo.getId();
    this.nodeToType = inputNodeTo.getType();
    this.nodeLoader = new NullNodeLoader();
  }

  public TraversableEdge(
      UniqueIdentifier edgeId,
      TraversableNode traversableNodeFrom,
      String edgeType,
      TraversableNode inputNodeTo,
      VersionedAttributeGroup attributeGroup,
      NodeLoader nodeLoader
  ) {
    super(attributeGroup);
    this.edgeId = edgeId;
    this.edgeType = edgeType;
    this.nodeFromId = traversableNodeFrom.getId();
    this.nodeFromType = traversableNodeFrom.getType();
    this.nodeToId = inputNodeTo.getId();
    this.nodeToType = inputNodeTo.getType();
    this.nodeLoader = nodeLoader;
  }

  public TraversableEdge(
      UniqueIdentifier edgeId,
      String edgeType,
      UniqueIdentifier traversableNodeFromId,
      String traversableNodeFromType,
      UniqueIdentifier inputNodeToId,
      String inputNodeToType,
      VersionedAttributeGroup attributeGroup,
      NodeLoader nodeLoader
  ) {
    super(attributeGroup);
    this.edgeId = edgeId;
    this.edgeType = edgeType;
    this.nodeFromId = traversableNodeFromId;
    this.nodeFromType = traversableNodeFromType;
    this.nodeToId = inputNodeToId;
    this.nodeToType = inputNodeToType;
    this.nodeLoader = nodeLoader;
  }

  @Override
  public TraversableEdge add(Attribute<?> attribute) {
    return (TraversableEdge) super.add(attribute);
  }

  public UniqueIdentifier getId() {
    return edgeId;
  }

  public String getType() {
    return edgeType;
  }

  public UniqueIdentifier getNodeFromId() {
    return nodeFromId;
  }

  public UniqueIdentifier getNodeToId() {
    return nodeToId;
  }

  public String getNodeFromType() {
    return this.nodeFromType;
  }

  public String getNodeToType() {
    return this.nodeToType;
  }

  @Override
  protected AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes) {
    return new TraversableEdge(
        this.getId(),
        this.getType(),
        this.getNodeFromId(),
        this.getNodeFromType(),
        this.getNodeToId(),
        this.getNodeToType(),
        newAttributes,
        this.nodeLoader
    );
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TraversableEdge edge)) {
      return false;
    }
    return
        this.getType().equals(edge.getType()) &&
            this.getNodeFrom().getIdlessHashCode() == edge.getNodeFrom().getIdlessHashCode() &&
            this.getNodeTo().getIdlessHashCode() == edge.getNodeTo().getIdlessHashCode() &&
            this.getVersionedAttributes().equals(edge.getVersionedAttributes());
  }

  @Override
  public int hashCode() {
    int result = getType().hashCode();
    result = 31 * result + getNodeFrom().getIdlessHashCode();
    result = 31 * result + getNodeTo().getIdlessHashCode();
    result = 31 * result + getVersionedAttributes().hashCode();
    return result;
  }

  public TraversableNode getNodeFrom() {
    return this.nodeLoader.loadNode(
        this.getNodeFromId(),
        this.getNodeFromType()
    );
  }

  public TraversableNode getNodeTo() {
    return this.nodeLoader.loadNode(
        this.getNodeToId(),
        this.getNodeToType()
    );
  }

  @Override
  protected int getHashCodeWithoutAttributes() {
    return Objects.hash(this.getId()) + this.getIdlessHashCodeWithoutAttributes();
  }

  @Override
  protected int getIdlessHashCodeWithoutAttributes() {
    return Objects.hash(
        this.getType(),
        this.getNodeFrom().getIdlessHashCode(),
        this.getNodeTo().getIdlessHashCode()
    );
  }

  public int compareTo(TraversableEdge other) {
    return this.edgeId.compareTo(other.edgeId);
  }
}
