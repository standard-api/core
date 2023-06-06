package ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractNodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import java.util.List;

public class NodeQueryGraphDescription extends AbstractNodeDescription
    implements GraphElementQueryDescription {

  public static final String SERIALIZATION_TYPE = "72ba0bef-5f38-489b-bbbf-8212191f1f92";

  private SearchQueryParameters searchQueryParameters;

  protected NodeQueryGraphDescription() {
    super();
  }

  public NodeQueryGraphDescription(NodeDescription nodeDescription) {
    super(
        SERIALIZATION_TYPE,
        (NodeDescriptionParameters) nodeDescription.getParameters(),
        nodeDescription.getChildGraphDescriptions()
    );
    this.searchQueryParameters = new SearchQueryParameters();
  }

  public NodeQueryGraphDescription(
      NodeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      List<GraphDescription> childDeclarations
  ) {
    super(SERIALIZATION_TYPE, parameters, childDeclarations);
    this.parameters = parameters;
    this.searchQueryParameters = searchQueryParameters;
  }

  public NodeQueryGraphDescription(
      NodeDescriptionParameters parameters,
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, parameters, childDeclarations);
    this.parameters = parameters;
    this.searchQueryParameters = new SearchQueryParameters();
  }

  public NodeQueryGraphDescription(
      NodeDescriptionParameters parameters
  ) {
    super(SERIALIZATION_TYPE, parameters);
    this.searchQueryParameters = new SearchQueryParameters();
  }

  public NodeQueryGraphDescription(
      NodeDescriptionParameters parameters,
      SearchQueryParameters searchQueryParameters,
      GraphDescription... childDeclarations
  ) {
    super(SERIALIZATION_TYPE, parameters, childDeclarations);
    this.searchQueryParameters = searchQueryParameters;
  }

  @Override
  public SearchQueryParameters getSearchQueryParameters() {
    return this.searchQueryParameters;
  }
}
