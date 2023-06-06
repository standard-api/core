package ai.stapi.graphoperations.graphLanguage;

import ai.stapi.graphoperations.declaration.AbstractDeclaration;

abstract public class AbstractGraphDeclaration extends AbstractDeclaration
    implements GraphDeclaration {

  public static final String DECLARATION_TYPE = "GraphDeclaration";

  protected AbstractGraphDeclaration() {
  }

  protected AbstractGraphDeclaration(String serializationType) {
    super(serializationType);
  }

  @Override
  public String getDeclarationType() {
    return DECLARATION_TYPE;
  }
}
