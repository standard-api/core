package ai.stapi.graphoperations.graphLoader.search.filterOption;

public abstract class AbstractLeafFilterOption<ParametersType extends LeafFilterOptionParameters>
    extends AbstractFilterOption<ParametersType>
    implements LeafFilterOption<ParametersType> {


  protected AbstractLeafFilterOption() {
  }

  protected AbstractLeafFilterOption(String strategy, ParametersType parameters) {
    super(strategy, parameters);
  }

  @Override
  public boolean isLeaf() {
    return this.getParameters().isLeaf();
  }

  @Override
  public boolean isDescribingAttribute() {
    return this.getParameters().isDescribingAttribute();
  }
}
