package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeafGraphToMapMapper extends AbstractSpecificGraphToMapMapper {

  public LeafGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    super(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Override
  public Object resolve(
      List<TraversableGraphElement> elements,
      ObjectGraphMapping mapping,
      Graph graph
  ) {
    if (elements.size() > 1) {
      throw GraphToMapObjectMapperException.becauseObjectCannotBeResolvedFromMultipleStartingElements(elements);
    }
    return this.traverseGraphToValue(
        mapping.getGraphDescription(),
        elements.get(0),
        graph
    );
  }

  @Override
  public boolean supports(ObjectGraphMapping mapping) {
    return mapping instanceof LeafObjectGraphMapping;
  }
}
