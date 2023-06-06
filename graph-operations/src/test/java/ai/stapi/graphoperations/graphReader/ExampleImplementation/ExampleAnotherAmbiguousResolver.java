package ai.stapi.graphoperations.graphReader.ExampleImplementation;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.GraphDescriptionReadResolver;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExampleAnotherAmbiguousResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository contextualGraph
  ) {
    return null;
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof ExampleAmbiguousGraphDescription;
  }
}
