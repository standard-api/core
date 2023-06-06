package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import java.util.List;

public interface SpecificGraphToObjectDeserializer {

  Object deserialize(
      List<TraversableGraphElement> elements,
      GraphDescription lastGraphDescription,
      ObjectGraphMapping graphMapping,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingTraversalTargetResolvingStrategy
  );

  default Object deserialize(
      List<TraversableGraphElement> elements,
      GraphDescription lastGraphDescription,
      ObjectGraphMapping graphMapping,
      InMemoryGraphRepository contextualGraph
  ) {
    return this.deserialize(
        elements,
        lastGraphDescription,
        graphMapping,
        contextualGraph,
        MissingTraversalTargetResolvingStrategy.STRICT
    );
  }

  ;

  boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  );
}
