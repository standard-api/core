package ai.stapi.graph.versionedAttributes.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public abstract class VersionedAttributesException extends GraphException {

  public VersionedAttributesException(String message) {
    super(message);
  }
}
