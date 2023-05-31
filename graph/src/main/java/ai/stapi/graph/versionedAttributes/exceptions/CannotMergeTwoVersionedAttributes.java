package ai.stapi.graph.versionedAttributes.exceptions;

import ai.stapi.graph.versionedAttributes.VersionedAttribute;

public class CannotMergeTwoVersionedAttributes extends VersionedAttributesException {

  protected CannotMergeTwoVersionedAttributes(String message) {
    super("Cannot merge two versioned attributes, " + message);
  }

  public static CannotMergeTwoVersionedAttributes becauseTheyHaveDifferentNames(
      VersionedAttribute<?> oldAttribute,
      VersionedAttribute<?> newAttribute
  ) {
    return new CannotMergeTwoVersionedAttributes(
        """
            because they have different names.
            Original - Name: '%s', Current Value: '%s'
            New - Name: '%s', Current Value: '%s'
            """.formatted(
            oldAttribute.getName(),
            oldAttribute.getCurrent().getValue(),
            newAttribute.getName(),
            newAttribute.getCurrent().getValue()
        )
    );
  }
}
