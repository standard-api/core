package ai.stapi.graph.versionedAttributes.exceptions;

import ai.stapi.graph.exceptions.GraphException;
import java.util.List;

public class VersionedAttributeCannotBeCreatedWithDifferentTypes extends GraphException {

  public VersionedAttributeCannotBeCreatedWithDifferentTypes(String expectedType,
      List<String> providedType) {
    super(
        String.format(
            "Versioned Attribute cannot be created with different types of attributes." +
                "Expected type: %s" +
                "Provided type: %s",
            expectedType,
            providedType
        )
    );
  }
}
