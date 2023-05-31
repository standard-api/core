package ai.stapi.graph.versionedAttributes.exceptions;

public class CannotAddNewVersionOfAttribute extends VersionedAttributesException {

  protected CannotAddNewVersionOfAttribute(String message) {
    super("Cannot add new version of attribute, " + message);
  }

  public static CannotAddNewVersionOfAttribute becauseNewVersionHasDifferentNames(String firstName,
      String secondName) {
    return new CannotAddNewVersionOfAttribute(
        String.format(
            "because name \"%s\" is not same as \"%s\".",
            firstName,
            secondName
        )
    );
  }
}
