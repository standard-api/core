package ai.stapi.graphoperations.graphReader.mappingPartReadResolvers;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graphoperations.graphReader.readResults.ReadResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueDescriptionReadResolver implements GraphDescriptionReadResolver {

  @Override
  public List<ReadResult> resolve(
      ReadResult previousResult,
      PositiveGraphDescription graphDescription,
      InMemoryGraphRepository graph
  ) {
    return List.of(previousResult);
  }

  @Override
  public boolean supports(PositiveGraphDescription graphDescription) {
    return graphDescription instanceof AbstractAttributeValueDescription;
  }
}
