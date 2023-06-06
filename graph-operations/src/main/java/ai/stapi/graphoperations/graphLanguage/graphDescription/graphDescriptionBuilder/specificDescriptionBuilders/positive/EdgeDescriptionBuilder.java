package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.positive;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.exception.GraphDescriptionBuilderException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders.SpecificGraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EdgeDescriptionBuilder extends AbstractPositiveDescriptionBuilder {

  private EdgeDirection direction;
  private String edgeType;

  @Override
  public AbstractEdgeDescription build() {
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::build)
        .collect(Collectors.toCollection(ArrayList::new));
    if (direction.equals(EdgeDirection.INGOING)) {
      return new IngoingEdgeDescription(new EdgeDescriptionParameters(edgeType), children);
    } else if (direction.equals(EdgeDirection.OUTGOING)) {
      return new OutgoingEdgeDescription(new EdgeDescriptionParameters(edgeType), children);
    } else {
      throw GraphDescriptionBuilderException.becauseEdgeDirectionIsNotSupported(direction);
    }
  }

  @Override
  public boolean represents(GraphDescription graphDescription) {
    return graphDescription instanceof AbstractEdgeDescription;
  }

  @Override
  public EdgeDescriptionBuilder setValues(GraphDescription graphDescription) {
    var parameters = (EdgeDescriptionParameters) graphDescription.getParameters();
    this.edgeType = parameters.getEdgeType();
    if (graphDescription instanceof OutgoingEdgeDescription) {
      this.direction = EdgeDirection.OUTGOING;
    } else if (graphDescription instanceof IngoingEdgeDescription) {
      this.direction = EdgeDirection.INGOING;
    } else {
      throw GraphDescriptionBuilderException.becauseDescriptionTypeIsNotSupported(graphDescription);
    }
    return this;
  }

  @Override
  public AbstractEdgeDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  ) {
    if (graphDescription instanceof OutgoingEdgeDescription outgoingEdgeDescription) {
      return new OutgoingEdgeDescription(
          (EdgeDescriptionParameters) outgoingEdgeDescription.getParameters(), newChildren);
    }
    if (graphDescription instanceof IngoingEdgeDescription ingoingEdgeDescription) {
      return new IngoingEdgeDescription(
          (EdgeDescriptionParameters) ingoingEdgeDescription.getParameters(), newChildren);
    }
    if (graphDescription instanceof OutgoingEdgeQueryDescription outgoingEdgeQueryDescription) {
      return new OutgoingEdgeQueryDescription(
          (EdgeDescriptionParameters) outgoingEdgeQueryDescription.getParameters(),
          outgoingEdgeQueryDescription.getSearchQueryParameters(), newChildren
      );
    }
    if (graphDescription instanceof IngoingEdgeQueryDescription ingoingEdgeQueryDescription) {
      return new IngoingEdgeQueryDescription(
          (EdgeDescriptionParameters) ingoingEdgeQueryDescription.getParameters(),
          ingoingEdgeQueryDescription.getSearchQueryParameters(), newChildren
      );
    }
    throw GraphDescriptionBuilderException.becauseDescriptionTypeIsNotSupported(graphDescription);
  }

  @Override
  public EdgeDescriptionBuilder getCopy() {
    var builder = new EdgeDescriptionBuilder();
    var children = this.getChildren().stream()
        .map(SpecificGraphDescriptionBuilder::getCopy)
        .collect(Collectors.toCollection(ArrayList::new));
    builder.setDirection(this.direction)
        .setEdgeType(this.edgeType)
        .setChildren(children);
    return builder;
  }

  public EdgeDirection getDirection() {
    return direction;
  }

  public EdgeDescriptionBuilder setDirection(EdgeDirection direction) {
    this.direction = direction;
    return this;
  }

  public String getEdgeType() {
    return edgeType;
  }

  public EdgeDescriptionBuilder setEdgeType(String edgeType) {
    this.edgeType = edgeType;
    return this;
  }
}
