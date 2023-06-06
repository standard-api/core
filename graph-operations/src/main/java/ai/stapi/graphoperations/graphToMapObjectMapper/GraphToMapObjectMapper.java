package ai.stapi.graphoperations.graphToMapObjectMapper;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.graphToMapObjectMapper.specific.SpecificGraphToMapMapper;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;
import java.util.Map;

public class GraphToMapObjectMapper {

  private final List<SpecificGraphToMapMapper> specificGraphToMapMappers;
  private final GenericGraphMappingProvider genericGraphMappingProvider;

  public GraphToMapObjectMapper(
      List<SpecificGraphToMapMapper> specificGraphToMapMappers,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    this.specificGraphToMapMappers = specificGraphToMapMappers;
    this.genericGraphMappingProvider = genericGraphMappingProvider;
  }

  public Map<String, Object> map(
      TraversableNode element,
      Graph graph
  ) {
    if (!genericGraphMappingProvider.supports(element.getType())) {
      throw GraphToMapObjectMapperException
          .becauseNodeTypeIsNotValidSerializationType(element.getType());
    }
    if (graph.getAllNodes().isEmpty()) {
      throw GraphToMapObjectMapperException.becauseProvidedGraphIsEmpty();
    }
    var ogm = genericGraphMappingProvider.provideGraphMapping(element.getType());
    return (Map<String, Object>) this.resolveInternally(
        List.of(element),
        ogm,
        graph
    );
  }

  public Object resolveInternally(
      List<TraversableGraphElement> traversableGraphElements,
      ObjectGraphMapping objectGraphMapping,
      Graph contextualGraph
  ) {
    var specificMapper = this.getSupportingMapper(objectGraphMapping);
    return specificMapper.resolve(
        traversableGraphElements,
        objectGraphMapping,
        contextualGraph
    );
  }

  private SpecificGraphToMapMapper getSupportingMapper(ObjectGraphMapping mapping) {
    var specificMapper = this.specificGraphToMapMappers.stream()
        .filter(mapper -> mapper.supports(mapping))
        .toList();
    if (specificMapper.isEmpty()) {
      throw GraphToMapObjectMapperException.becauseOgmIsNotSupported(mapping);
    } else if (specificMapper.size() > 1) {
      throw GraphToMapObjectMapperException.becauseOgmIsSupportedByMultipleMappers(mapping);
    } else {
      return specificMapper.get(0);
    }
  }
}
