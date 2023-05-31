package ai.stapi.graph.versionedAttributes.exceptions;

import ai.stapi.graph.exceptions.GraphException;

public class VersionedAttributeCannotBeEmpty extends GraphException {

  public VersionedAttributeCannotBeEmpty() {
    super("Versioned attribute cannot be empty.");
  }
}
