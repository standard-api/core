package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception;

import ai.stapi.graph.attribute.Attribute;

public class AttributeMappingPartResolverException extends RuntimeException {

  private AttributeMappingPartResolverException(String message) {
    super(message);
  }

  public static AttributeMappingPartResolverException becauseFoundAttributeTypeIsNotSupported(
      Attribute<?> attribute,
      String dataType
  ) {
    return new AttributeMappingPartResolverException(
        "Found attribute '"
            + attribute.getName()
            + "' of type '"
            + dataType
            + "' is not supported by this resolver."
    );
  }
}
