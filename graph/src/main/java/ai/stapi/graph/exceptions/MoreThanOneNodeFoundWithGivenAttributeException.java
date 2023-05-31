package ai.stapi.graph.exceptions;

public class MoreThanOneNodeFoundWithGivenAttributeException extends GraphException {

  public MoreThanOneNodeFoundWithGivenAttributeException(String name, Object value) {
    super(
        String.format(
            "More than one node found with \"%s\" attribute name and value: \"%s\"",
            name,
            value
        )
    );
  }
}
