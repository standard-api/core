package ai.stapi.graphoperations.graphbuilder.specific.positive;

import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.GraphElement;
import ai.stapi.identity.UniqueIdentifier;

public interface GraphElementBuilder {

  GraphElementBuilder setId(UniqueIdentifier id);

  GraphElementBuilder setType(String type);

  AttributeBuilder addAttribute();

  AttributeBuilder getLastAttribute();

  boolean isComplete();

  GraphElement build(GenericAttributeFactory attributeFactory);

  Graph buildToGraph(GenericAttributeFactory attributeFactory);
}
