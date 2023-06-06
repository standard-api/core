package ai.stapi.graphoperations.objectLanguage;

import ai.stapi.graphoperations.declaration.AbstractDeclaration;

abstract public class AbstractObjectDeclaration extends AbstractDeclaration
    implements ObjectDeclaration {

  protected AbstractObjectDeclaration() {

  }

  protected AbstractObjectDeclaration(String serializationType) {
    super(serializationType);
  }

  @Override
  public String getDeclarationType() {
    return DECLARATION_TYPE;
  }
}
