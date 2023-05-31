package ai.stapi.graph.inputGraphElements;

import ai.stapi.graph.AttributeContainer;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.inMemoryGraph.exceptions.GraphNodesCannotBeMerged;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Objects;

public class InputNode extends AbstractGraphElement {


  public InputNode(UniqueIdentifier id, String nodeType) {
    super(id, nodeType);
  }

  public InputNode(
      UniqueIdentifier id,
      String type,
      VersionedAttributeGroup versionedAttributes
  ) {
    super(id, type, versionedAttributes);
  }

  public InputNode(UniqueIdentifier id, String nodeType, Attribute<?>... attributes) {
    super(id, nodeType, attributes);
  }

  public InputNode(String nodeType) {
    this(UniversallyUniqueIdentifier.randomUUID(), nodeType);
  }

  public InputNode(TraversableNode node) {
    this(node.getId(), node.getType(), node.getVersionedAttributes());
  }

  public InputNode(InputNode node) {
    this(node.getId(), node.getType(), node.getVersionedAttributes());
  }

  public InputNode(String nodeType, Attribute<?>... attributes) {
    this(UniversallyUniqueIdentifier.randomUUID(), nodeType, attributes);
  }

  @Override
  protected AttributeContainer withNewAttributes(VersionedAttributeGroup newAttributes) {
    return new InputNode(
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

  public <T extends Attribute<?>> InputNode addToNode(T attribute) {
    return (InputNode) this.add(attribute);
  }

  public InputNode mergeOverwrite(InputNode otherNode) {
    if (!this.getId().equals(otherNode.getId())) {
      throw GraphNodesCannotBeMerged.becauseTheyHaveDifferentIds();
    }
    if (!this.getType().equals(otherNode.getType())) {
      throw GraphNodesCannotBeMerged.becauseTheyHaveDifferentTypes(
          this.getType(),
          otherNode.getType()
      );
    }
    return (InputNode) this.mergeAttributesWithAttributesOf(otherNode);
  }

}
