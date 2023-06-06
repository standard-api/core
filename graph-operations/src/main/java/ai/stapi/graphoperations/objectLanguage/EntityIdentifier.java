package ai.stapi.graphoperations.objectLanguage;

public class EntityIdentifier extends AbstractObjectDeclaration {

  public static final String SERIALIZATION_TYPE = "6a1a15dd-4aba-4c20-b883-2836d3e4cbd6";

  public EntityIdentifier() {
    super(SERIALIZATION_TYPE);
  }

  @Override
  public String getSerializationType() {
    return SERIALIZATION_TYPE;
  }

}
