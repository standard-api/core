package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;

public class AbstractLeafFilterOptionParameters implements LeafFilterOptionParameters {

  private final PositiveGraphDescription attributeNamePath;

  public AbstractLeafFilterOptionParameters(PositiveGraphDescription attributeNamePath) {
    this.attributeNamePath = attributeNamePath;
  }
  
  public AbstractLeafFilterOptionParameters(String attributeName) {
    this.attributeNamePath = new AttributeQueryDescription(attributeName);
  }

  @Override
  public PositiveGraphDescription getAttributeNamePath() {
    return this.attributeNamePath;
  }

  @Override
  public boolean isLeaf() {
    return this.getAttributeNamePath() instanceof UuidIdentityDescription
        || this.getAttributeNamePath() instanceof AbstractAttributeDescription;
  }

  @Override
  public boolean isDescribingAttribute() {
    var lastGraphDescription = this.getLastGraphDescription();
    return lastGraphDescription instanceof AbstractAttributeDescription;
  }

  @Override
  public String getAttributeName() {
    var lastGraphDescription = this.getLastGraphDescription();
    var last = (AttributeDescriptionParameters) lastGraphDescription.getParameters();
    return last.getAttributeName();
  }

  private GraphDescription getLastGraphDescription() {
    var flat = GraphDescriptionBuilder
        .getGraphDescriptionAsStream(this.getAttributeNamePath())
        .toList();

    return flat.get(flat.size() - 1);
  }
}
