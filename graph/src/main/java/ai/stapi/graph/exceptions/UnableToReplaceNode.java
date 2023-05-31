package ai.stapi.graph.exceptions;

public class UnableToReplaceNode extends GraphException {

  public UnableToReplaceNode(String message) {
    super(message);
  }


  public static UnableToReplaceNode becauseNotAllowedToReplaceNodeForAnotherType(
      String currentNodeType,
      Object replacingNodeType
  ) {
    return new UnableToReplaceNode(
        "because it is not allowed to replace node for another type." +
            "\nCurrent node type: " + currentNodeType +
            "\nReplacing node type: " + replacingNodeType
    );
  }
}
