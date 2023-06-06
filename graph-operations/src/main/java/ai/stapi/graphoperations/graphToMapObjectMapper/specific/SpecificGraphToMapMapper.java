package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import java.util.List;

public interface SpecificGraphToMapMapper {

  Object resolve(
      List<TraversableGraphElement> elements,
      ObjectGraphMapping mapping,
      Graph graph
  );

  boolean supports(ObjectGraphMapping mapping);
}
