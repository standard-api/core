package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import java.util.List;

public class OutgoingEdgeQueryDescription extends AbstractEdgeDescription
    implements EdgeQueryDescription {

  public static final String SERIALIZATION_TYPE = "399c927c-fa45-435d-bc83-66918b18482c";

  private SearchQueryParameters searchQueryParameters;
  private CollectionComparisonOperator collectionComparisonOperator;
  private boolean isCompact;

  protected OutgoingEdgeQueryDescription() {
    super();
  }

  public OutgoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      CollectionComparisonOperator collectionComparisonOperator,
      List<GraphDescription> childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.searchQueryParameters = searchQueryParameters;
    this.collectionComparisonOperator = collectionComparisonOperator;
    this.isCompact = true;
  }

  public OutgoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      List<GraphDescription> childDeclarations
  ) {
    this(parameters, searchQueryParameters, CollectionComparisonOperator.ANY, childDeclarations);
  }

  public OutgoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      CollectionComparisonOperator collectionComparisonOperator,
      GraphDescription... childDeclarations
  ) {
    super(parameters, SERIALIZATION_TYPE, childDeclarations);
    this.searchQueryParameters = searchQueryParameters;
    this.collectionComparisonOperator = collectionComparisonOperator;
    this.isCompact = true;
  }

  public OutgoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      GraphDescription... childDeclarations
  ) {
    this(parameters, searchQueryParameters, CollectionComparisonOperator.ANY, childDeclarations);
  }

  public OutgoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters
  ) {
    super(parameters, SERIALIZATION_TYPE);
    this.searchQueryParameters = new SearchQueryParameters();
    this.isCompact = true;
  }

  public static OutgoingEdgeQueryDescription asConnections(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      GraphDescription... childDeclarations
  ) {
    var outgoingEdgeQueryDescription = new OutgoingEdgeQueryDescription(
        parameters,
        searchQueryParameters,
        childDeclarations
    );
    outgoingEdgeQueryDescription.isCompact = false;
    return outgoingEdgeQueryDescription;
  }

  public static OutgoingEdgeQueryDescription asConnections(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      CollectionComparisonOperator collectionComparisonOperator,
      GraphDescription... childDeclarations
  ) {
    var outgoingEdgeQueryDescription = new OutgoingEdgeQueryDescription(
        parameters,
        searchQueryParameters,
        collectionComparisonOperator,
        childDeclarations
    );
    outgoingEdgeQueryDescription.isCompact = false;
    return outgoingEdgeQueryDescription;
  }


  @Override
  public boolean isOutgoing() {
    return true;
  }

  @Override
  public SearchQueryParameters getSearchQueryParameters() {
    return this.searchQueryParameters;
  }

  @Override
  public boolean isCompact() {
    return isCompact;
  }

  public void setIsCompact(boolean isCompact) {
    this.isCompact = isCompact;
  }

  public CollectionComparisonOperator getCollectionComparisonOperator() {
    return collectionComparisonOperator;
  }
}
