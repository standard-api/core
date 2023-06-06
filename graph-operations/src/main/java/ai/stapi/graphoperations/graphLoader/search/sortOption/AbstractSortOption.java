package ai.stapi.graphoperations.graphLoader.search.sortOption;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AttributeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLoader.search.AbstractSearchOption;
import ai.stapi.graphoperations.graphLoader.search.exceptions.SearchOptionResolverRuntimeException;

public abstract class AbstractSortOption extends AbstractSearchOption<PositiveGraphDescription>
    implements SortOption {

  public static final String OPTION_TYPE = "sort";

  protected AbstractSortOption() {
  }

  protected AbstractSortOption(String strategy, String attributeName) {
    super(
        AbstractSortOption.OPTION_TYPE,
        strategy,
        new AttributeQueryDescription(attributeName)
    );
  }

  protected AbstractSortOption(String strategy, PositiveGraphDescription attributeNamePath) {
    super(
        AbstractSortOption.OPTION_TYPE,
        strategy,
        attributeNamePath
    );
    if (!GraphDescriptionBuilder.isGraphDescriptionSinglePath(attributeNamePath)) {
      throw SearchOptionResolverRuntimeException.becauseGraphDescriptionInsideSortOptionMustBeSinglePath();
    }
    if (!GraphDescriptionBuilder.isGraphDescriptionEndingWithAttributeDescription(attributeNamePath)) {
      throw SearchOptionResolverRuntimeException.becauseGraphDescriptionInsideSortOptionMustEndWithValueDescription();
    }
  }

  public boolean isLeaf() {
    return this.getParameters() instanceof AbstractAttributeDescription;
  }

  public String getAttributeName() {
    var flat = GraphDescriptionBuilder.getGraphDescriptionAsStream(this.getParameters()).toList();
    var last = (AttributeDescriptionParameters) flat.get(flat.size() - 1).getParameters();
    return last.getAttributeName();
  }
}
