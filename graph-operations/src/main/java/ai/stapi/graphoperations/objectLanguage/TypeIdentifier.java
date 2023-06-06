package ai.stapi.graphoperations.objectLanguage;

public class TypeIdentifier extends AbstractObjectDeclaration {

  public static final String SERIALIZATION_TYPE = "c32ab0d2-03f5-4095-a873-3745704f81bf";

  @Override
  public String getSerializationType() {
    return SERIALIZATION_TYPE;
  }

  @Override
  public String getDeclarationType() {
    return DECLARATION_TYPE;
  }
}
