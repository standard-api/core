package ai.stapi.graphoperations.graphLoader.search.filterOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public interface LeafFilterOptionParameters extends FilterOptionParameters {

  PositiveGraphDescription getAttributeNamePath();

  boolean isLeaf();

  boolean isDescribingAttribute();

  String getAttributeName();
}
