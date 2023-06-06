package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;

public class InterfaceGraphToMapMapper extends AbstractSpecificGraphToMapMapper {

  public InterfaceGraphToMapMapper(
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
    var traversedInterface = this.traverseGraph(
        mapping.getGraphDescription(),
        elements,
        graph
    );
    if (traversedInterface.size() > 1) {
      throw GraphToMapObjectMapperException
          .becauseObjectsCannotSplitIntoMultipleBranchedInsideObjectOgm();
    }
    if (traversedInterface.isEmpty()) {
      return null;
    }
    var newSerializationType = this.resolveSerializationType(traversedInterface.get(0));
    var ogm = this.genericGraphMappingProvider.provideGraphMapping(newSerializationType);
    return this.graphToMapObjectMapper.resolveInternally(
        traversedInterface,
        ogm,
        graph
    );
  }

  @Override
  public boolean supports(ObjectGraphMapping mapping) {
    return mapping instanceof InterfaceObjectGraphMapping;
  }
}
