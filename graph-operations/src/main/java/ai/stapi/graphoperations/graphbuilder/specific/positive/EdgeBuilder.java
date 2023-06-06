package ai.stapi.graphoperations.graphbuilder.specific.positive;

public interface EdgeBuilder extends GraphElementBuilder {

  EdgeDirection getEdgeDirection();

  EdgeBuilder setEdgeDirection(EdgeDirection edgeDirection);
}
