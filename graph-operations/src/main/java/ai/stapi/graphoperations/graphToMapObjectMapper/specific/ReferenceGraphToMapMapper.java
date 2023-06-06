package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ReferenceObjectGraphMapping;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphReader.exception.GraphReaderException;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;

public class ReferenceGraphToMapMapper extends AbstractSpecificGraphToMapMapper {

  public ReferenceGraphToMapMapper(GraphReader graphReader,
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
    List<TraversableGraphElement> traversedInterface;
    try {
      traversedInterface = this.traverseGraph(
          mapping.getGraphDescription(),
          elements,
          graph
      );
    } catch (GraphReaderException exception) {
      return null;
    }
    if (traversedInterface.isEmpty()) {
      return null;
    }
    var referenceOgm = (ReferenceObjectGraphMapping) mapping;
    var ogm = this.genericGraphMappingProvider.provideGraphMapping(
        referenceOgm.getReferencedSerializationType()
    );
    return this.graphToMapObjectMapper.resolveInternally(
        traversedInterface,
        ogm,
        graph
    );
  }

  @Override
  public boolean supports(ObjectGraphMapping mapping) {
    return mapping instanceof ReferenceObjectGraphMapping;
  }
}
