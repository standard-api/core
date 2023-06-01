package ai.stapi.graph.graphelements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphNodesCannotBeMerged;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Objects;

public class Node extends AbstractGraphElement {


  public Node(UniqueIdentifier id, String nodeType) {
    super(id, nodeType);
  }

  public Node(
      UniqueIdentifier id,
      String type,
      VersionedAttributeGroup versionedAttributes
  ) {
    super(id, type, versionedAttributes);
  }

  public Node(UniqueIdentifier id, String nodeType, Attribute<?>... attributes) {
    super(id, nodeType, attributes);
  }

  public Node(String nodeType) {
    this(UniversallyUniqueIdentifier.randomUUID(), nodeType);
  }

  public Node(TraversableNode node) {
    this(node.getId(), node.getType(), node.getVersionedAttributes());
  }

  public Node(Node node) {
    this(node.getId(), node.getType(), node.getVersionedAttributes());
  }

  public Node(String nodeType, Attribute<?>... attributes) {
    this(UniversallyUniqueIdentifier.randomUUID(), nodeType, attributes);
  }

  @Override
  protected AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes) {
    return new Node(
        this.getId(),
        this.getType(),
        newAttributes
    );
  }

  @Override
  protected int getHashCodeWithoutAttributes() {
    return Objects.hash(this.getId()) + this.getIdlessHashCodeWithoutAttributes();
  }

  @Override
  protected int getIdlessHashCodeWithoutAttributes() {
    return Objects.hash(this.getType());
  }

  @Override
  public Node add(Attribute<?> attribute) {
    return (Node) super.add(attribute);
  }

  public Node mergeOverwrite(Node otherNode) {
    if (!this.getId().equals(otherNode.getId())) {
      throw GraphNodesCannotBeMerged.becauseTheyHaveDifferentIds();
    }
    if (!this.getType().equals(otherNode.getType())) {
      throw GraphNodesCannotBeMerged.becauseTheyHaveDifferentTypes(
          this.getType(),
          otherNode.getType()
      );
    }
    return (Node) this.mergeAttributesWithAttributesOf(otherNode);
  }

}
