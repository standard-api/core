package ai.stapi.graph.attribute.exceptions;

import ai.stapi.graph.exceptions.GraphException;
import java.util.Set;

public class CompositeAttributeCannotBeCreated extends GraphException {

  private CompositeAttributeCannotBeCreated(String becauseMessage) {
    super("Composite attribute cannot be created, because" + becauseMessage);
  }

  public static CompositeAttributeCannotBeCreated becauseNoValuesProvided(String providedName) {
    return new CompositeAttributeCannotBeCreated(
        "no values provided in constructor. " +
            "If you want to create one with no values, you have to at least specify Data Type" +
            "\nProvided attribute name: " + providedName
    );
  }

  public static CompositeAttributeCannotBeCreated becauseProvidedValuesHasDifferentDataTypes(
      String name,
      Set<String> uniqueTypes
  ) {
    return new CompositeAttributeCannotBeCreated(
        "provided values in constructor are of different Data Types. " +
            "\nProvided attribute name: " + name +
            "\nFound Value Data Types: " + uniqueTypes
    );
  }
}
