package ai.stapi.graph.attribute.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class AttributeNotFoundException extends GraphException {

  public AttributeNotFoundException(String attributeName) {
    super(String.format("Attribute \"%s\" not found.", attributeName));
  }
}
