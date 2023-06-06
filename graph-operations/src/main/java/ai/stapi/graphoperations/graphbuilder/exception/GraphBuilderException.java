package ai.stapi.graphoperations.graphbuilder.exception;

import ai.stapi.graphoperations.graphbuilder.specific.positive.NodeBuilder;
import ai.stapi.graphoperations.graphbuilder.specific.positive.GraphElementBuilder;

public class GraphBuilderException extends RuntimeException {

  private GraphBuilderException(String message) {
    super(message);
  }

  public static GraphBuilderException becauseAttributeValueTypeIsNotSupported(Object value) {
    return new GraphBuilderException(
        "Attribute value of type '" + value.toString() + "' is not supported.");
  }

  public static GraphBuilderException becauseAttributeBuilderIsNotComplete() {
    return new GraphBuilderException("Attribute builder is not complete.");
  }

  public static GraphBuilderException becauseNodeIsMissingIdOrType() {
    return new GraphBuilderException("Node is missing id or type.");
  }

  public static GraphBuilderException becauseEdgeIsNotCompleted() {
    return new GraphBuilderException(
        "Edge was not complete. It is missing either id, type or direction.");
  }

  public static GraphBuilderException becauseNodeFromOnEdgeIsNotComplete() {
    return new GraphBuilderException("Node FROM on edge is not complete.");
  }

  public static GraphBuilderException becauseNodeToOnEdgeIsNotComplete() {
    return new GraphBuilderException("Node TO on edge is not complete.");
  }

  public static GraphBuilderException becauseGraphCanNotHaveEdgeAsFirstElement() {
    return new GraphBuilderException("Graph can not have edge as first element.");
  }

  public static GraphBuilderException becauseGraphCanNotHaveEdgeAsLastElement() {
    return new GraphBuilderException("Graph can not have edge as last element.");
  }

  public static GraphBuilderException becauseOfUnexpectedEdgeNeighbor(
      GraphElementBuilder elementBuilder) {
    return new GraphBuilderException(
        "Because there was unexpected edge neighbor: '"
            + (elementBuilder == null ? "null'."
            : elementBuilder.getClass().getSimpleName() + "'."));
  }

  public static GraphBuilderException becauseThereCanNotBeTwoNodesAfterEachOther(
      NodeBuilder previousNode) {
    return new GraphBuilderException(
        "There can not be two nodes after each other. Previous node type: '"
            + previousNode.getType() + "'."
    );
  }

  public static GraphBuilderException becauseThereCanNotBeTwoEdgesAfterEachOther() {
    return new GraphBuilderException("There can not be two edges after each other.");
  }

  public static GraphBuilderException becauseEdgeHasToBePrecededByNode() {
    return new GraphBuilderException("Edge has to be preceded by node.");
  }

  public static GraphBuilderException becauseThereAreNoElementsAbleToContainAttribute() {
    return new GraphBuilderException("There are no elements able to contain attributes.");
  }

  public static GraphBuilderException becauseThereAreNoAttributesOnElement() {
    return new GraphBuilderException("There are no attributes on element.");
  }

  public static GraphBuilderException becauseThereAreNoElements() {
    return new GraphBuilderException("There are no elements inside builder.");
  }
}
