package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import java.util.List;

public class IngoingEdgeQueryDescription extends AbstractEdgeDescription
    implements EdgeQueryDescription {

  public static final String SERIALIZATION_TYPE = "16843ef0-b5a1-4d45-b59c-962294de5e6e";

  private SearchQueryParameters searchQueryParameters;

  private boolean isCompact;
  private CollectionComparisonOperator collectionComparisonOperator;

  protected IngoingEdgeQueryDescription() {
    super();
  }

  public IngoingEdgeQueryDescription(
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

  public IngoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      List<GraphDescription> childDeclarations
  ) {
    this(parameters, searchQueryParameters, CollectionComparisonOperator.ANY, childDeclarations);
  }

  public IngoingEdgeQueryDescription(
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

  public IngoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      GraphDescription... childDeclarations
  ) {
    this(parameters, searchQueryParameters, CollectionComparisonOperator.ANY, childDeclarations);
  }

  public IngoingEdgeQueryDescription(
      EdgeDescriptionParameters parameters
  ) {
    super(parameters, SERIALIZATION_TYPE);
    this.searchQueryParameters = new SearchQueryParameters();
    this.isCompact = true;
  }

  public static IngoingEdgeQueryDescription asConnections(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      GraphDescription... childDeclarations
  ) {
    var ingoingEdgeQueryDescription = new IngoingEdgeQueryDescription(
        parameters,
        searchQueryParameters,
        childDeclarations
    );
    ingoingEdgeQueryDescription.isCompact = false;
    return ingoingEdgeQueryDescription;
  }

  public static IngoingEdgeQueryDescription asConnections(
      EdgeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      CollectionComparisonOperator collectionComparisonOperator,
      GraphDescription... childDeclarations
  ) {
    var ingoingEdgeQueryDescription = new IngoingEdgeQueryDescription(
        parameters,
        searchQueryParameters,
        collectionComparisonOperator,
        childDeclarations
    );
    ingoingEdgeQueryDescription.isCompact = false;
    return ingoingEdgeQueryDescription;
  }

  @Override
  public boolean isOutgoing() {
    return false;
  }

  @Override
  public SearchQueryParameters getSearchQueryParameters() {
    return this.searchQueryParameters;
  }

  @Override
  public boolean isCompact() {
    return this.isCompact;
  }

  public void setIsCompact(boolean isCompact) {
    this.isCompact = isCompact;
  }

  public CollectionComparisonOperator getCollectionComparisonOperator() {
    return collectionComparisonOperator;
  }
}
