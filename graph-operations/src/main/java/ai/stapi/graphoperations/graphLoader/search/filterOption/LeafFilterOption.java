package ai.stapi.graphoperations.graphLoader.search.filterOption;

public interface LeafFilterOption<ParametersType extends LeafFilterOptionParameters>
    extends FilterOption<ParametersType> {

  boolean isLeaf();

  boolean isDescribingAttribute();
}
