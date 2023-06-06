package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.declaration.AbstractDeclaration;

public abstract class AbstractObjectGraphMapping extends AbstractDeclaration
    implements ObjectGraphMapping {

  public static final String DECLARATION_TYPE = "ObjectGraphMapping";
  protected GraphDescription graphDescription;

  protected AbstractObjectGraphMapping() {

  }

  public AbstractObjectGraphMapping(
      GraphDescription graphDescription,
      String deserializationType
  ) {
    super(deserializationType);
    this.graphDescription = graphDescription;
  }

  public AbstractObjectGraphMapping(String deserializationType) {
    super(deserializationType);
  }

  @Override
  public GraphDescription getGraphDescription() {
    return graphDescription;
  }

  @Override
  public String getDeclarationType() {
    return DECLARATION_TYPE;
  }
}
