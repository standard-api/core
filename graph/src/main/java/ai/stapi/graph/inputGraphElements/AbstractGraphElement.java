package ai.stapi.graph.inputGraphElements;

import ai.stapi.graph.attribute.AbstractAttributeContainer;
import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.versionedAttributes.VersionedAttributeGroup;
import ai.stapi.identity.UniqueIdentifier;

public abstract class AbstractGraphElement extends AbstractAttributeContainer
    implements GraphElement {

  protected final UniqueIdentifier id;
  protected final String type;

  protected AbstractGraphElement(UniqueIdentifier id, String type) {
    this.id = id;
    this.type = type;
  }

  protected AbstractGraphElement(
      UniqueIdentifier id,
      String type,
      VersionedAttributeGroup versionedAttributes
  ) {
    super(versionedAttributes);
    this.id = id;
    this.type = type;
  }

  protected AbstractGraphElement(UniqueIdentifier id, String type, Attribute<?>... attributes) {
    super(attributes);
    this.id = id;
    this.type = type;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public UniqueIdentifier getId() {
    return this.id;
  }
}
