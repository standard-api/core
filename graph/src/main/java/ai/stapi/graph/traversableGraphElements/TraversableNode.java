package ai.stapi.graph.traversableGraphElements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.EdgeLoader;
import ai.stapi.graph.EdgeRepository;
import ai.stapi.graph.Graph;
import ai.stapi.graph.NullEdgeLoader;
import ai.stapi.graph.RepositoryEdgeLoader;
import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class TraversableNode extends AbstractAttributeContainer implements TraversableGraphElement {

  private final UniqueIdentifier id;
  private final String nodeType;
  private final EdgeLoader edgeLoader;

  public static TraversableNode fromInput(
      InputNode inputNode,
      EdgeRepository edgeRepository
  ) {
    return new TraversableNode(
        inputNode.getId(),
        inputNode.getType(),
        inputNode.getVersionedAttributes(),
        new RepositoryEdgeLoader(edgeRepository)
    );
  }

  public TraversableNode(String nodeType) {
    this(
        UniversallyUniqueIdentifier.randomUUID(),
        nodeType
    );
  }

  public TraversableNode(UniqueIdentifier id, String nodeType) {
    this.id = id;
    this.nodeType = nodeType;
    this.edgeLoader = new NullEdgeLoader();
  }

  public TraversableNode(
      UniqueIdentifier uniqueIdentifier,
      String nodeType,
      VersionedAttributeGroup attributeGroup,
      EdgeLoader edgeLoader
  ) {
    super(attributeGroup);
    this.id = uniqueIdentifier;
    this.nodeType = nodeType;
    this.edgeLoader = edgeLoader;
  }

  @Override
  public TraversableNode add(Attribute<?> attribute) {
    return (TraversableNode) super.add(attribute);
  }

  public UniqueIdentifier getId() {
    return this.id;
  }

  public String getType() {
    return this.nodeType;
  }

  public List<TraversableEdge> getEdges(String edgeType) {
    return this.edgeLoader.loadEdges(
        this.id,
        this.nodeType,
        edgeType
    );
  }

  public List<TraversableEdge> getOutgoingEdges(String edgeType) {
    return this.edgeLoader.loadEdges(
            this.id,
            this.nodeType,
            edgeType
        ).stream()
        .filter(edge -> edge.getNodeFromId().equals(this.id))
        .toList();
  }

  public List<TraversableEdge> getIngoingEdges(String edgeType) {
    return this.edgeLoader.loadEdges(
            this.id,
            this.nodeType,
            edgeType
        ).stream()
        .filter(edge -> edge.getNodeToId().equals(this.id))
        .toList();
  }

  public List<TraversableEdge> getEdges() {
    return this.edgeLoader.loadEdges(
        this.id,
        this.nodeType
    );
  }

  @Override
  protected AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes) {
    return new TraversableNode(
        this.getId(),
        this.getType(),
        newAttributes,
        this.edgeLoader
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TraversableNode node)) {
      return false;
    }
    return getId().equals(node.getId())
        && getType().equals(node.getType())
        && getEdges().equals(node.getEdges());
    //TODO && getUsableIdentificators().equals(node.getUsableIdentificators());
  }

  @Override
  public int hashCode() {
    int result = getId().hashCode();
    result = 31 * result + getType().hashCode();
    result = 31 * result + getEdges().hashCode();
    return result;
  }

  public int getIdlessHashCodeWithEdges() {
    String type = this.getType();
    VersionedAttributeGroup versionedAttributes = this.getVersionedAttributes();
    int hashCodeForEdgesOnNode = this.getHashCodeForEdgesOnNode();
    return Objects.hash(
        type,
        versionedAttributes,
        hashCodeForEdgesOnNode
    );
  }

  private int getHashCodeForEdgesOnNode() {
    return this.edgeLoader.getIdlessHashCodeForEdgesOnNode(this.getId(), this.getType());
  }

  @Override
  protected int getHashCodeWithoutAttributes() {
    return Objects.hash(this.getId()) + this.getIdlessHashCodeWithoutAttributes();
  }

  @Override
  protected int getIdlessHashCodeWithoutAttributes() {
    return Objects.hash(this.getType());
  }

  public String getSortingNameWithHashCodeFallback() {
    var maybeBestName = this.guessBestName();
    if (maybeBestName != null) {
      return maybeBestName;
    }
    return String.valueOf(this.getIdlessHashCodeWithEdges());
  }

  public String getSortingNameWithNodeTypeFallback() {
    var idName = this.id.toString();
    try {
      UUID.fromString(idName);
    } catch (IllegalArgumentException e) {
      return idName;
    }
    var maybeBestName = this.guessBestName();
    if (maybeBestName != null) {
      return maybeBestName;
    }
    return this.getType();
  }

  public Graph getNeighborhoodGraph(int maxLimit) {
    if (maxLimit == 0) {
      return new Graph(new InputNode(
          this.id,
          this.nodeType,
          this.getVersionedAttributes()
      ));
    }
    var nodesFrom = this.getEdges()
        .stream()
        .limit(maxLimit)
        .map(traversableEdge -> new InputNode(traversableEdge.getNodeFrom()));
    var nodesTo = this.getEdges()
        .stream()
        .limit(maxLimit)
        .map(traversableEdge -> new InputNode(traversableEdge.getNodeTo()));
    var edges = this.getEdges()
        .stream()
        .limit(maxLimit)
        .map(InputEdge::new);

    var nodesStream = Stream.concat(
        nodesFrom,
        nodesTo
    );
    var resultStream = Stream.concat(
        nodesStream,
        edges
    );

    return new Graph(
        resultStream.distinct().toArray(AttributeContainer[]::new)
    );
  }

}
