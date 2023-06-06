package ai.stapi.graphoperations.graphbuilder.specific.positive;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttributeBuilder {

  private String attributeName;
  private String structureType;
  private List<AttributeValueFactoryInput> attributeValues;

  public AttributeBuilder() {
    this.attributeValues = new ArrayList<>();
  }

  public AttributeBuilder setAttributeName(String attributeName) {
    this.attributeName = attributeName;
    return this;
  }

  public AttributeBuilder addAttributeValue(AttributeValueFactoryInput attributeValue) {
    this.attributeValues.add(attributeValue);
    return this;
  }


  public AttributeBuilder setAttributeStructureType(String structureType) {
    this.structureType = structureType;
    return this;
  }

  public boolean isComplete() {
    return this.attributeName != null && this.attributeValues != null
        && this.structureType != null;
  }

  public Attribute<?> build(GenericAttributeFactory attributeFactory) {
    return attributeFactory.create(
        this.attributeName,
        this.structureType,
        this.attributeValues,
        new HashMap<>()
    );
  }
}
