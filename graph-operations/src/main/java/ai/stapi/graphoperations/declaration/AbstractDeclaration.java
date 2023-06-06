package ai.stapi.graphoperations.declaration;

import ai.stapi.serialization.AbstractSerializableObject;

public abstract class AbstractDeclaration extends AbstractSerializableObject
    implements Declaration {

  protected AbstractDeclaration(String serializationType) {
    super(serializationType);
  }

  protected AbstractDeclaration() {
  }

  @Override
  public abstract String getDeclarationType();
}
