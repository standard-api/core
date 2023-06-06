package ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.specificDescriptionBuilders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import java.util.List;

public interface SpecificGraphDescriptionBuilder {

  SpecificGraphDescriptionBuilder addChild(SpecificGraphDescriptionBuilder child);

  SpecificGraphDescriptionBuilder setValues(GraphDescription graphDescription);

  boolean represents(GraphDescription graphDescription);

  GraphDescription build();

  SpecificGraphDescriptionBuilder getCopy();

  GraphDescription copyWithNewChildren(
      GraphDescription graphDescription,
      List<GraphDescription> newChildren
  );

  List<SpecificGraphDescriptionBuilder> getChildren();

  SpecificGraphDescriptionBuilder setChildren(List<SpecificGraphDescriptionBuilder> children);

  SpecificGraphDescriptionBuilder setChildren(SpecificGraphDescriptionBuilder... children);
}
