package ai.stapi.graphoperations.graphbuilder.specific.positive;

import ai.stapi.graphoperations.graphbuilder.exception.GraphBuilderException;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class NodeBuilder implements GraphElementBuilder {

  private final List<AttributeBuilder> attributes = new ArrayList<>();
  private UniqueIdentifier id;
  private String type;

  @Override
  public NodeBuilder setId(UniqueIdentifier id) {
    this.id = id;
    return this;
  }

  public NodeBuilder setId(String id) {
    this.id = new UniqueIdentifier(id);
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
    return this.id != null && this.type != null;
  }

  @Override
  public Node build(GenericAttributeFactory attributeFactory) {
    if (!this.isComplete()) {
      throw GraphBuilderException.becauseNodeIsMissingIdOrType();
    }
    var node = new Node(this.id, this.type);
    for (AttributeBuilder builder : this.attributes) {
      Attribute<?> attribute = builder.build(attributeFactory);
      node = node.add(attribute);
    }
    return node;
  }

  @Override
  public Graph buildToGraph(GenericAttributeFactory attributeFactory) {
    return new Graph(this.build(attributeFactory));
  }

  public String getType() {
    return type;
  }

  @Override
  public NodeBuilder setType(String type) {
    this.type = type;
    return this;
  }
}
