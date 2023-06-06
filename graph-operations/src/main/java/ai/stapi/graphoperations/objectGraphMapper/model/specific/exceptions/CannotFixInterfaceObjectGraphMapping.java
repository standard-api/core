package ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions;

public class CannotFixInterfaceObjectGraphMapping extends RuntimeException {

  public CannotFixInterfaceObjectGraphMapping(String serializationType, Throwable cause) {
    super(
        String.format(
            "Cannot fix Interface OGM, because provided Object contains serializationType '%s'," +
                " which does not have Structure Schema.",
            serializationType
        ),
        cause
    );
  }
}
