package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import java.util.List;

public interface GraphDescriptionReadResolver {

  List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  );

  boolean supports(PositiveGraphDescription graphDescription);
}
