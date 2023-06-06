package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSpecificDescriptionBuilder
    implements SpecificGraphDescriptionBuilder {

  private List<SpecificGraphDescriptionBuilder> children = new ArrayList<>();

  public List<SpecificGraphDescriptionBuilder> getChildren() {
    return children;
  }

  public SpecificGraphDescriptionBuilder addChild(SpecificGraphDescriptionBuilder child) {
    this.children.add(child);
    return this;
  }

  @Override
  public SpecificGraphDescriptionBuilder setChildren(
      List<SpecificGraphDescriptionBuilder> children) {
    this.children = new ArrayList<>(children);
    return this;
  }

  @Override
  public SpecificGraphDescriptionBuilder setChildren(SpecificGraphDescriptionBuilder... children) {
    this.children = Arrays.stream(children).collect(Collectors.toCollection(ArrayList::new));
    return this;
  }


}
